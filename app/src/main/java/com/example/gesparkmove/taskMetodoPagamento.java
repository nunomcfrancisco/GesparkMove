package com.example.gesparkmove;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class taskMetodoPagamento extends AsyncTask<String, Integer, Void>{
    AlertDialog ppm;
    Context ctx;
    Handler handler;
    Globals g = new Globals();

    taskMetodoPagamento(Context ctx, Handler handler){
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
                ResultSet rs = statement.executeQuery("SELECT id_metodoPagamento FROM metodosPagamentoUtilizador WHERE id_utilizador = " + params[0]);
                rs.next();
                switch (rs.getString(1)){
                    case "1":
                        rs = statement.executeQuery("SELECT nomeCartaoCredito, numeroCartaoCredito, dataValidadeCartaoCredito FROM metodosPagamentoUtilizador WHERE id_utilizador = " + params[0]);
                    break;
                    case "2":
                        rs = statement.executeQuery("SELECT telefoneMbway FROM metodosPagamentoUtilizador WHERE id_utilizador = " + params[0]);
                    break;
                    case "3":
                        rs = statement.executeQuery("SELECT dDirectoNome, dDirectoIban FROM metodosPagamentoUtilizador WHERE id_utilizador = " + params[0]);
                    break;
                }

            }catch (ClassNotFoundException | SQLException e){
                Log.println(Log.INFO, "SQL Exception: ", e.toString());
            }
        return null;
        }catch (JSchException e){
            Log.println(Log.INFO, "JSch Exception: ", e.toString());
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
