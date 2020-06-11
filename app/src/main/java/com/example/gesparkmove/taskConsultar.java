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

public class taskConsultar extends AsyncTask<String, Integer, ArrayList<Veiculo>> {
    AlertDialog ppm;
    Handler handler;
    Context ctx;
    Globals g = new Globals();
    ArrayList<Veiculo> data = new ArrayList<>();
    private final onConsultarListener listener;

    taskConsultar(Context ctx, onConsultarListener listener, Handler handler){
        this.ctx = ctx;
        this.listener = listener;
        this.handler = handler;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
    }

    @Override
    protected ArrayList<Veiculo> doInBackground(String... params) {
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
                com.mysql.jdbc.Connection connection = (Connection) DriverManager.getConnection(g.getMySqlUrl(), g.getMySqlUsername(), g.getMySqlPass());
                com.mysql.jdbc.Statement statement = (Statement) connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT veiculos.id, veiculos.matricula, marcas.marca, modelo.modelo, cor, estacionado, veiculos.activo FROM veiculos inner join marcas inner join modelo WHERE id_utilizador = " + params[0] + " AND veiculos.id_marca = marcas.id AND veiculos.id_modelo = modelo.id");
                //ResultSet rs = statement.executeQuery("SELECT id, matricula, id_marca, id_modelo, cor, estacionado, activo FROM veiculos WHERE id_utilizador =" + params[0]);
                while(rs.next())
                    data.add(new Veiculo(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6), rs.getInt(7)));
                    Log.println(Log.INFO, "TESTE CONSULTAR:  ", rs.getString(2));
                connection.close();
            }catch (ClassNotFoundException | SQLException e){
                Log.println(Log.INFO, "SQL EXCEPTION: ", e.toString());
            }
            session.disconnect();
            }catch (JSchException e){
            Log.println(Log.INFO, "JSCH EXCEPTION: ", e.toString());
        }
        return data;
    }

    @Override
    protected void onProgressUpdate(Integer... values){
        handler.post(new Runnable() {
            @Override
            public void run() {
                ppm = new AlertDialog.Builder(ctx).setMessage("Loading").setCancelable(false).show();
            }
        });
    }

    @Override
    protected void onPostExecute(ArrayList<Veiculo> data) {
        listener.onConsultarCompleted(data);
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(ppm.isShowing())
                    ppm.dismiss();
            }
        });
    }
}
