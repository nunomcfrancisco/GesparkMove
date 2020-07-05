package com.example.gesparkmove;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class taskActivatePayment extends AsyncTask<String, Integer, Void> {
    AlertDialog ad;
    Context ctx;
    Handler handler;
    FragmentManager manager;
    Globals g = new Globals();

    taskActivatePayment(Context ctx, Handler handler, FragmentManager manager){
        this.ctx = ctx;
        this.handler = handler;
        this.manager = manager;
    }

    @Override
    protected Void doInBackground(String... params) {
        publishProgress(0);
        try {
            //abre o tunel SSH
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
                statement.execute("UPDATE metodosPagamentoUtilizador SET activo = " + params[0] + " WHERE id_utilizador = " + params[1]);
                connection.close();
            } catch (ClassNotFoundException | SQLException e) {
                Log.println(Log.INFO, "SQL EXCEPTION: ", e.toString());
            }
            //fecha o tunel SSH
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
                ad = new AlertDialog.Builder(ctx, R.style.AlertDialogCustom).setView(R.layout.progress_bar).setCancelable(false).show();
            }
        });
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        ad.dismiss();
        handler.post(new Runnable() {
            @Override
            public void run() {
                fragmentPayment pFragment = new fragmentPayment();
                manager.beginTransaction()
                        .replace(R.id.containerFragment, pFragment, "pagamentos")
                        .commit();
            }
        });
    }
}
