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
import java.util.Properties;

public class taskData extends AsyncTask<Void, Integer, ArrayList<ArrayList>>{
    AlertDialog ppm;
    Context ctx;
    Handler handler;
    Globals g = new Globals();
    ArrayList<ArrayList> data = new ArrayList<>();
    ArrayList<Marca> tempMarcas = new ArrayList<>();
    ArrayList<Modelo> tempModelos = new ArrayList<>();
    private final onMarcasModelosListener listener;

    taskData(Context ctx, onMarcasModelosListener listener, Handler handler){
        this.ctx = ctx;
        this.listener = listener;
        this.handler = handler;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
    }

    @Override
    protected ArrayList<ArrayList> doInBackground(Void... voids) {
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
                ResultSet rsMarcas = statement.executeQuery("SELECT * FROM marcas");
                while (rsMarcas.next()){
                    tempMarcas.add(new Marca(rsMarcas.getInt(1), rsMarcas.getString(2)));
                }
                data.add(tempMarcas);
                ResultSet rsModelo = statement.executeQuery("SELECT * FROM modelo");
                while(rsModelo.next()){
                    tempModelos.add(new Modelo(rsModelo.getInt(1), rsModelo.getString(2), rsModelo.getInt(3)));
                }
                data.add(tempModelos);
                connection.close();
            }catch (ClassNotFoundException | SQLException e){
                Log.println(Log.INFO, "ErrorMessage", String.valueOf(e));
            }
            session.disconnect();
        } catch (JSchException e){
            Log.println(Log.INFO, "ErrorMessage", String.valueOf(e));
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
    protected void onPostExecute(ArrayList<ArrayList> data) {
        listener.onMarcasModelosCompleted(data);
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(ppm.isShowing())
                    ppm.dismiss();
            }
        });

    }
}
