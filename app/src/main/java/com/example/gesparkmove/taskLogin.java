package com.example.gesparkmove;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class taskLogin extends AsyncTask<String, Integer, String> {
    @SuppressLint("StaticFieldLeak")
    Context ctx;
    Handler handler;
    Intent intent;
    Globals g = new Globals();
    Utilizador user;
    AlertDialog ppm;

    taskLogin(Context ctx, Handler handler){
        this.ctx = ctx;
        this.handler = handler;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params){
        StringBuilder queryResult = new StringBuilder();
        publishProgress(0);
            try {
                JSch jsch = new JSch();
                Session session = jsch.getSession(g.getSshUsername(), g.getSshHost(), g.getSshPort());
                session.setPassword(g.getSshPass());
                session.setPortForwardingL(g.getSshPFLPort(), g.getSshPFHost(), g.getSshPFRPort());
                Properties prop = new Properties();
                prop.put("StrictHostKeyChecking", "no");
                session.setConfig(prop);
                session.connect();
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection connection = (Connection) DriverManager.getConnection(g.getMySqlUrl(), g.getMySqlUsername(), g.getMySqlPass());
                    Statement statement = (Statement) connection.createStatement();
                    ResultSet rs = statement.executeQuery(params[0]);
                    while (rs.next()) {
                        user = new Utilizador(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), 5, rs.getString(6), rs.getInt(7));
                        queryResult.append(rs.getString(5));
                    }
                    if (queryResult.length() > 0) {
                        rs = statement.executeQuery(params[2]);
                        while (rs.next()) {
                            user.setCarros(rs.getInt(1));
                        }
                    }
                    connection.close();
                } catch (ClassNotFoundException | SQLException e) {
                    Log.println(Log.INFO, "ErrorMessage", String.valueOf(e));
                }
                session.disconnect();
            } catch (JSchException e) {
                Log.println(Log.INFO, "ErrorMessage", String.valueOf(e));
            }
            if (queryResult.toString().equals(""))
                return "u";
            else if (user.getAtivo() == 0)
                return "a";
            else if (queryResult.toString().equals(new md5Tools().encode(params[1]))) {
                try {
                    JSch jsch = new JSch();
                    Session session = jsch.getSession(g.getSshUsername(), g.getSshHost(), g.getSshPort());
                    session.setPassword(g.getSshPass());
                    session.setPortForwardingL(g.getSshPFLPort(), g.getSshPFHost(), g.getSshPFRPort());
                    Properties prop = new Properties();
                    prop.put("StrictHostKeyChecking", "no");
                    session.setConfig(prop);
                    session.connect();
                    try {
                        Class.forName("com.mysql.jdbc.Driver");
                        Connection connection = (Connection) DriverManager.getConnection(g.getMySqlUrl(), g.getMySqlUsername(), g.getMySqlPass());
                        Statement statement = (Statement) connection.createStatement();
                        statement.executeUpdate("UPDATE utilizadores SET dataUltimoAcesso = NOW() WHERE id = " + user.getId());
                        connection.close();
                    } catch (ClassNotFoundException | SQLException e) {
                        e.printStackTrace();
                    }
                    session.disconnect();
                } catch (JSchException e) {
                    e.printStackTrace();
                }
                return "y";
            } else {
                return "p";
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values){
        handler.post(new Runnable() {
            @Override
            public void run() {
                ppm = new AlertDialog.Builder(ctx)
                        .setMessage("Loading")
                        .setCancelable(false)
                        .show();
            }
        });
    }

    @Override
    protected void onPostExecute(final String result){
        switch (result) {
            case "u":
                if (ppm.isShowing()) ppm.dismiss();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ppm = new AlertDialog.Builder(ctx)
                                .setTitle("Erro!")
                                .setMessage("Utilizador inválido!")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .show();
                    }
                });
                break;
            case "a":
                if (ppm.isShowing()) ppm.dismiss();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ppm = new AlertDialog.Builder(ctx)
                                .setTitle("Erro!")
                                .setMessage("Conta não ativa.")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .show();
                    }
                });
                break;
            case "p":
                if (ppm.isShowing()) ppm.dismiss();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ppm = new AlertDialog.Builder(ctx)
                                .setTitle("Erro!")
                                .setMessage("Password inválida!")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .show();
                    }
                });
                break;
            case "y":
                if (ppm.isShowing()) ppm.dismiss();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(ctx, UtilizadorActivity.class);
                        intent.putExtra("USER", user);
                        ctx.startActivity(intent);
                    }
                });
                break;
        }
    }
}
