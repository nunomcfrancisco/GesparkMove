package com.example.gesparkmove;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class taskAdicionarMatricula extends AsyncTask<String, Integer, Void> {
    AlertDialog ppm;
    Context ctx;
    Handler handler;
    Globals g = new Globals();

    taskAdicionarMatricula(Context ctx, Handler handler){
        this.ctx = ctx;
        this.handler = handler;
    }

    @Override
    protected Void doInBackground(String... params) {
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
                statement.execute("INSERT INTO veiculos (matricula, id_utilizador, id_marca, id_modelo, cor, activo, imagem) VALUES(" + params[0] +  ", " + params[1] + "," + params[2] + ", " + params[3] +  ", " + params[4] + ", 0, NULL)");
                connection.close();
            } catch (ClassNotFoundException | SQLException e) {
                Log.println(Log.INFO, "SQL Exception: ", e.toString());
            }
            session.disconnect();
        }catch (JSchException e){
            Log.println(Log.INFO, "JSch Exception: ", e.toString());
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values){
        handler.post(new Runnable() {
            @Override
            public void run() {
                ppm = new AlertDialog.Builder(ctx)
                        .setMessage("A adicionar...")
                        .setCancelable(false)
                        .show();
            }
        });
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        ppm.dismiss();
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ctx, "Matricula adicionada", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
