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

//task para ir buscar à base de dados a informação de um utilizador
public class taskUserData extends AsyncTask<String, Integer, ArrayList<String>> {
    //declaração das variáveis
    AlertDialog ad;
    Handler handler;
    Context ctx;
    Globals g = new Globals();
    ArrayList<String> data = new ArrayList<>();
    private final onUserListener listener;

    //construtor
    taskUserData(Context ctx, onUserListener listener, Handler handler){
        this.ctx = ctx;
        this.listener = listener;
        this.handler = handler;
    }

    @Override
    protected ArrayList<String> doInBackground(String... params) {
        publishProgress(0);
        //abre o tunel SSH
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
                //abre a ligação à base de dados
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = (Connection) DriverManager.getConnection(g.getMySqlUrl(), g.getMySqlUsername(), g.getMySqlPass());
                Statement statement = (Statement) connection.createStatement();
                //query para ir buscar o nif, nome, morada e condigoPostal do utilizador
                ResultSet rs = statement.executeQuery("SELECT nif, nome, morada, codigoPostal FROM utilizadores WHERE id = " + params[0]);
                while (rs.next()) {
                    data.add(String.valueOf(rs.getInt(1)));
                    data.add(rs.getString(2));
                    data.add(rs.getString(3));
                    data.add(rs.getString(4));
                }
                //query para ir buscar o contacto do utilizador
                rs = statement.executeQuery("SELECT contacto FROM contactos WHERE id_utilizador = " + params[0]);
                while (rs.next()) {
                    data.add(rs.getString(1));
                }
                //query para ir buscar o avatar do utilizador
                rs = statement.executeQuery("SELECT avatar FROM utilizadores WHERE id = " + params[0]);
                while(rs.next()){
                    data.add(rs.getString(1));
                }
                //fechar ligação à base de dados
                connection.close();
            } catch (ClassNotFoundException | SQLException e) {
                Log.println(Log.INFO, "ErrorMessage", String.valueOf(e));
            }
            //fechar tunel SSH
            session.disconnect();
        }catch (JSchException e){
            Log.println(Log.INFO, "ErrorMessage", String.valueOf(e));
        }
        return data;
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
    protected void onPostExecute(ArrayList<String> data) {
        listener.onUserCompleted(data);
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(ad.isShowing())
                    ad.dismiss();
            }
        });
    }
}
