package com.example.gesparkmove;

import android.content.Context;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class taskData extends AsyncTask<Void, Integer, Void>{
    AlertDialog ppm;
    Context ctx;
    Handler handler;
    Globals g = new Globals();
    ArrayList<Marcas> alMarcas;
    ArrayList<Modelos> alModelos;

    taskData(Context ctx, ArrayList<Marcas> alMarcas, ArrayList<Modelos> alModelos, Handler handler){
        this.ctx = ctx;
        this.alMarcas = alMarcas;
        this.alModelos = alModelos;
        this.handler = handler;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
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
                    alMarcas.add(new Marcas(rsMarcas.getInt(1), rsMarcas.getString(2)));
                }
                ResultSet rsModelo = statement.executeQuery("SELECT * FROM modelo");
                while(rsModelo.next()){
                    alModelos.add(new Modelos(rsModelo.getInt(1), rsModelo.getString(2), rsModelo.getInt(3)));
                }
                connection.close();
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
    protected void onProgressUpdate(Integer... values) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                ppm = new AlertDialog.Builder(ctx)
                        .setMessage("Loading")
                        .setCancelable(false)
                        .show();
            }
        });
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        ppm.dismiss();
    }
}
