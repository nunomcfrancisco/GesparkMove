package com.example.gesparkmove;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.EditText;

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
    Context ctx;
    Handler handler;
    Globals g = new Globals();
    User user;
    AlertDialog ad;
    Activity act;

    taskLogin(Context ctx, Handler handler, Activity act){
        this.ctx = ctx;
        this.handler = handler;
        this.act = act;
    }

    @Override
    protected String doInBackground(String... params){
        StringBuilder queryResult = new StringBuilder();
        publishProgress(0);
            try {
                //abrir tunnel SSH
                JSch jsch = new JSch();
                Session session = jsch.getSession(g.getSshUsername(), g.getSshHost(), g.getSshPort());
                session.setPassword(g.getSshPass());
                session.setPortForwardingL(g.getSshPFLPort(), g.getSshPFHost(), g.getSshPFRPort());
                Properties prop = new Properties();
                prop.put("StrictHostKeyChecking", "no");
                session.setConfig(prop);
                session.connect();
                try {
                    //abrir ligação para a base de dados
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection connection = (Connection) DriverManager.getConnection(g.getMySqlUrl(), g.getMySqlUsername(), g.getMySqlPass());
                    Statement statement = (Statement) connection.createStatement();
                    //query para ir buscar / verificar o utilizador que está a fazer login
                    ResultSet rs = statement.executeQuery("SELECT id, nif, nome, email, password, avatar, activo FROM utilizadores WHERE email = '" + params[0] + "'");
                    while (rs.next()) {
                        user = new User(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), 5, rs.getString(6), rs.getInt(7), 0);
                        queryResult.append(rs.getString(5));
                    }
                    if (queryResult.length() > 0) {
                        rs = statement.executeQuery("SELECT COUNT(matricula) FROM veiculos WHERE id_utilizador = (SELECT id FROM utilizadores WHERE email = '" + params[0] + "')");
                        while (rs.next())
                            user.setCars(rs.getInt(1));
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
            else if (user.getActive() == 0)
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
                        ResultSet rs = statement.executeQuery("SELECT SUM(estacionamento.valor) AS Total FROM estacionamento INNER JOIN veiculos ON estacionamento.id_matricula = veiculos.id " +
                                        "INNER JOIN utilizadores ON veiculos.id_utilizador = utilizadores.id WHERE utilizadores.id = " + user.getId());
                        while(rs.next())
                            user.setValue(rs.getDouble(1));
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
                ad = new AlertDialog.Builder(ctx, R.style.AlertDialogCustom).setView(R.layout.progress_bar).setCancelable(false).show();
            }
        });
    }

    @Override
    protected void onPostExecute(final String result){
        switch (result) {
            case "u":
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (ad.isShowing())
                            ad.dismiss();
                        ad = new AlertDialog.Builder(ctx)
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
                if (ad.isShowing())
                    ad.dismiss();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ad = new AlertDialog.Builder(ctx)
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
                if (ad.isShowing())
                    ad.dismiss();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ad = new AlertDialog.Builder(ctx)
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
                if (ad.isShowing())
                    ad.dismiss();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(ctx, activityUser.class);
                        //coloca "em memória" a informação do utilizador
                        intent.putExtra("USER", user);
                        EditText etUser = act.findViewById(R.id.editTextMainUser);
                        EditText etPassword = act.findViewById(R.id.editTextMainPassword);
                        etUser.setText("");
                        etPassword.setText("");
                        ctx.startActivity(intent);
                    }
                });
                break;
        }
    }
}
