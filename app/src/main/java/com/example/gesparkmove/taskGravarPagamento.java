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

public class taskGravarPagamento extends AsyncTask<String, Integer, String> {
    AlertDialog ad;
    Context ctx;
    Handler handler;
    Globals g = new Globals();

    taskGravarPagamento(Context ctx, Handler handler){
        this.ctx = ctx;
        this.handler = handler;
    }

    @Override
    protected String doInBackground(String... params) {
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
                ResultSet rs = statement.executeQuery("SELECT * FROM metodosPagamentoUtilizador WHERE id_utilizador = " + params[0]);
                if (!rs.next()) {
                    switch (params[1]) {
                        case "1":
                            statement.execute("INSERT INTO metodosPagamentoUtilizador (id_utilizador, id_metodoPagamento, nomeCartaoCredito, numeroCartaoCredito, dataValidadeCartaoCredito, codigoCvvCredito) VALUES (" + params[0] + ", " + params[1] + ", '"
                                    + params[2] + "', " + params[3] + ", " + params[4] + ", " + params[5] + ")");
                            break;
                        case "2":
                            statement.execute("INSERT INTO metodosPagamentoUtilizador (id_utilizador, id_metodoPagamento, telefoneMbway) VALUES (" + params[0] + ", " + params[1] + ", " + params[2] + ")");
                            break;
                        case "3":
                            statement.execute("INSERT INTO metodosPagamentoUtilizador (id_utilizador, id_metodoPagamento, dDirectoNome, dDirectoIban) VALUES (" + params[0] + ", " + params[1] + ", '" + params[2] + "', '" + params[3] + "')");
                            break;
                    }
                } else {
                    switch (params[1]) {
                        case "1":
                            statement.execute("UPDATE metodosPagamentoUtilizador SET id_metodoPagamento = " + params[1] + ", nomeCartaoCredito = '" + params[2] + "', numeroCartaoCredito = "
                                    + params[3] + ", dataValidadeCartaoCredito = " + params[4] + ", codigoCvvCredito= " + params[5] + " WHERE id_utilizador = " + params[0]);
                            break;
                        case "2":
                            statement.execute("UPDATE metodosPagamentoUtilizador SET id_metodoPagamento = " + params[1] + ", telefoneMbway = " + params[2] + " WHERE id_utilizador = " + params[0]);
                            break;
                        case "3":
                            statement.execute("UPDATE metodosPagamentoUtilizador SET id_metodoPagamento = " + params[1] + ", dDirectoNome = '" + params[2] + "', dDirectoIban = " + params[3]);
                            break;
                    }
                }
                connection.close();
            } catch (ClassNotFoundException | SQLException e) {
                Log.println(Log.INFO, "SQL EXCEPTION: ", e.toString());
            }
        }catch (JSchException e){
            Log.println(Log.INFO, "JSch Exception: ", e.toString());
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                ad = new AlertDialog.Builder(ctx, R.style.AlertDialogCustom).setView(R.layout.progress_bar).setCancelable(false).show();
            }
        });
    }

    @Override
    protected void onPostExecute(String s) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (ad.isShowing())
                    ad.dismiss();
            }
        });
    }
}
