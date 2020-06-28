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
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;

public class taskRegister extends AsyncTask<String, Integer, Void> {
    @SuppressLint("StaticFieldLeak")
    Context ctx;
    AlertDialog ppm;
    Handler handler;
    Globals g = new Globals();
    mailHelper mh = new mailHelper();
    int codAtiv;

    taskRegister(Context ctx, Handler handler){
        this.ctx = ctx;
        this.handler = handler;
    }

    @Override
    protected Void doInBackground(String... params){
        publishProgress(0);
        codAtiv = new Random().nextInt(99999999) + 10000001;
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
                statement.executeUpdate("INSERT INTO utilizadores (nif, nome, morada, codigoPostal, email, password, dataRegisto, nivelAcesso, activo, codigoActivacao) VALUES ("
                        + params[0] + ", '" + params[1] + "', '" + params[2] + "', " + params[3] + ", '" + params[4] + "', '" + new md5Tools().encode(params[5])
                        + "', NOW(), 0, 0, " + codAtiv + ")");
                connection.close();
                mh.mail = params[4];
                mh.user = params[1];
            }catch (ClassNotFoundException | SQLException e){
                Log.println(Log.INFO, "ErrorMessage", String.valueOf(e));
            }

            session.disconnect();
        } catch (JSchException e){
            Log.println(Log.INFO, "ErrorMessage", String.valueOf(e));
        }
        return null;
    }
    @Override
    protected void onProgressUpdate(Integer... values){
        handler.post(new Runnable() {
            @Override
            public void run() {
                ppm = new AlertDialog.Builder(ctx, R.style.AlertDialogCustom).setView(R.layout.progress_bar).setCancelable(false).show();
            }
        });
    }
    @Override
    protected void onPostExecute(Void aVoid){
        taskMail tm = new taskMail(ctx);
        tm.execute(mh.mail, mh.user, Integer.toString(codAtiv));
        handler.post(new Runnable() {
            @Override
            public void run() {
                ppm = new AlertDialog.Builder(ctx)
                        .setMessage("Consulte o seu email para ativar a conta.")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(ctx, activityMain.class);
                                ctx.startActivity(intent);
                            }
                        })
                        .show();
            }
        });
    }
}
