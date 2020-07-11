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
import java.util.ArrayList;
import java.util.Properties;

public class taskContacts extends AsyncTask<Void, Integer, ArrayList<Park>>{
    AlertDialog ad;
    Context ctx;
    Handler handler;
    Globals g = new Globals();
    ArrayList<Park> data = new ArrayList<>();
    private final onContactsListener listener;

    taskContacts(Context ctx, Handler handler, onContactsListener listener){
        this.ctx = ctx;
        this.handler = handler;
        this.listener = listener;
    }

    @Override
    protected ArrayList<Park> doInBackground(Void... voids) {
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
                ResultSet rs = statement.executeQuery("SELECT nome, localizacao, telefone, email, latitude, longitude FROM parque");
                while (rs.next())
                    data.add(new Park(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)));
                connection.close();
            }catch (ClassNotFoundException | SQLException e) {
                Log.println(Log.INFO, "SQL EXCEPTION: ", e.toString());
            }
            session.disconnect();
        } catch (JSchException e) {
            Log.println(Log.INFO, "JSCH EXCEPTION: ", e.toString());
        }
        return data;
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
    protected void onPostExecute(final ArrayList<Park> data) {
        listener.onContactsCompleted(data);
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (ad.isShowing()) ad.dismiss();

            }
        });
    }
}
