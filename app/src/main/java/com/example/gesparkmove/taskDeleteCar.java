package com.example.gesparkmove;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Properties;

public class taskDeleteCar extends AsyncTask<String, Integer, Void> {
    //declaração das variaveis
    AlertDialog ppm;
    Context ctx;
    Handler handler;
    Globals g = new Globals();
    FragmentManager manager;
    Activity activity;
    String idVeiculo;

    //construtor
    taskDeleteCar(Context ctx, Handler handler, FragmentManager manager, Activity activity){
        this.ctx = ctx;
        this.handler = handler;
        this.manager = manager;
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(String... params) {
        publishProgress(0);
        idVeiculo = params[0];
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
                //query para apagar uma viatura da base de dados
                statement.execute("DELETE FROM planoAcessoUtilizador WHERE id_veiculo = " + params[0]);
                statement.execute("DELETE FROM veiculos WHERE id = " + params[0]);
                connection.close();
            } catch (ClassNotFoundException | SQLException e){
                Log.println(Log.INFO, "SQL EXCEPTION: ", e.toString());
            }
            session.disconnect();
        }catch (JSchException e){
            Log.println(Log.INFO, "JSCH EXCEPTION: ", e.toString());
        }
        return null;
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
    protected void onPostExecute(Void aVoid) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (ppm.isShowing()) ppm.dismiss();
                Intent intent = activity.getIntent();
                User user = Objects.requireNonNull(intent.getExtras()).getParcelable("USER");
                user.setCars(user.getCars() - 1);
                intent.putExtra("USER", user);
                Toast.makeText(ctx, "Veiculo Apagado", Toast.LENGTH_SHORT).show();
                fragmentListCar cFragment = new fragmentListCar();
                manager.beginTransaction()
                        .replace(R.id.containerFragment, cFragment, "consultar")
                        .commit();
            }
        });
    }
}
