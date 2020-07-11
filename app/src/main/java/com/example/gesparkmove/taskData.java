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

//asyncTask para ir buscar as marcas e modelos de veículos
public class taskData extends AsyncTask<Void, Integer, ArrayList<ArrayList>>{
    //declaração de variáveis
    AlertDialog ad;
    Context ctx;
    Handler handler;
    Globals g = new Globals();
    ArrayList<ArrayList> data = new ArrayList<>();
    ArrayList<Brand> tempBrands = new ArrayList<>();
    ArrayList<Model> tempModel = new ArrayList<>();
    private final onBrandModelListener listener;
    //contrutor
    taskData(Context ctx, onBrandModelListener listener, Handler handler){
        this.ctx = ctx;
        this.listener = listener;
        this.handler = handler;
    }

    @Override
    protected ArrayList<ArrayList> doInBackground(Void... voids) {
        publishProgress(0);
        try {
            //abrir tunel SSH
            JSch jsch = new JSch();
            Session session = jsch.getSession(g.getSshUsername(), g.getSshHost(), g.getSshPort());
            session.setPassword(g.getSshPass());
            session.setPortForwardingL(g.getSshPFLPort(), g.getSshPFHost(), g.getSshPFRPort());
            Properties prop = new Properties();
            prop.put("StrictHostKeyChecking", "no");
            session.setConfig(prop);
            session.connect();
            try {
                //abrir ligação à base de dados
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = (Connection) DriverManager.getConnection(g.getMySqlUrl(), g.getMySqlUsername(), g.getMySqlPass());
                Statement statement = (Statement) connection.createStatement();
                //obter as marcas
                ResultSet rsBrands = statement.executeQuery("SELECT * FROM marcas");
                while (rsBrands.next()){
                    tempBrands.add(new Brand(rsBrands.getInt(1), rsBrands.getString(2)));
                }
                data.add(tempBrands);
                //obter os modelos
                ResultSet rsModel = statement.executeQuery("SELECT * FROM modelo");
                while(rsModel.next()){
                    tempModel.add(new Model(rsModel.getInt(1), rsModel.getString(2), rsModel.getInt(3)));
                }
                data.add(tempModel);
                //fechar ligação à base de dados
                connection.close();
            }catch (ClassNotFoundException | SQLException e){
                Log.println(Log.INFO, "ErrorMessage", String.valueOf(e));
            }
            //fechar tunel SSH
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
                ad = new AlertDialog.Builder(ctx, R.style.AlertDialogCustom).setView(R.layout.progress_bar).setCancelable(false).show();
            }
        });
    }

    @Override
    protected void onPostExecute(ArrayList<ArrayList> data) {
        listener.onBrandModelCompleted(data);
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(ad.isShowing())
                    ad.dismiss();
            }
        });
    }
}
