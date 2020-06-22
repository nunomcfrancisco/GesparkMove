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
                Connection connection = (Connection) DriverManager.getConnection(g.getMySqlUrl(), g.getMySqlUsername(), g.getMySqlPass());
                Statement statement = (Statement) connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT v.id, v.matricula, ma.marca, mo.modelo, cor, estacionado, v.activo, v.imagem FROM veiculos v inner join marcas ma inner join modelo mo WHERE id_utilizador = " + params[0] + " AND v.id_marca = ma.id AND v.id_modelo = mo.id ORDER BY v.matricula");
                while(rs.next())
                    data.add(new Veiculo(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6), rs.getInt(7), rs.getString(8)));
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
                ppm = new AlertDialog.Builder(ctx, R.style.AlertDialogCustom).setView(R.layout.progress_bar).setCancelable(false).show();
            }
        });
    }

    @Override
    protected void onPostExecute(ArrayList<Veiculo> data) {
        listener.onConsultarCompleted(data);
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(ppm.isShowing()) ppm.dismiss();
            }
        });
    }
}
