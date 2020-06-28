package com.example.gesparkmove;

import android.content.Context;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class taskPayment extends AsyncTask<String, Integer, List<String>>{
    AlertDialog ppm;
    Context ctx;
    Handler handler;
    List<String> data = new ArrayList<>();
    Globals g = new Globals();
    private final onPaymentListener listener;

    taskPayment(Context ctx, Handler handler, onPaymentListener listener){
        this.ctx = ctx;
        this.handler = handler;
        this.listener = listener;
    }

    @Override
    protected List<String> doInBackground(String... params) {
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
                        rs.next();
                        data.add(rs.getString(1));
                        data.add(rs.getString(2));
                        data.add(rs.getString(3));
                    break;
                    case "2":
                        rs = statement.executeQuery("SELECT telefoneMbway FROM metodosPagamentoUtilizador WHERE id_utilizador = " + params[0]);
                        rs.next();
                        data.add(rs.getString(1));
                    break;
                    case "3":
                        rs = statement.executeQuery("SELECT dDirectoNome, dDirectoIban FROM metodosPagamentoUtilizador WHERE id_utilizador = " + params[0]);
                        rs.next();
                        data.add(rs.getString(1));
                        data.add(rs.getString(2));
                    break;
                }
                connection.close();
            }catch (ClassNotFoundException | SQLException e){
                Log.println(Log.INFO, "SQL Exception: ", e.toString());
            }
            session.disconnect();
        }catch (JSchException e){
            Log.println(Log.INFO, "JSch Exception: ", e.toString());
        }
        return data;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                ppm = new AlertDialog.Builder(ctx, R.style.AlertDialogCustom).setView(R.layout.progress_bar).setCancelable(false).show();
            }
        });
    }

    @Override
    protected void onPostExecute(List<String> data) {
        listener.onPagamentosCompleted(data);
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(ppm.isShowing()) ppm.dismiss();
            }
        });
    }
}
