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
import java.util.Properties;

public class taskVeiculosEstacionados extends AsyncTask<String, Integer, Void> {
    AlertDialog ppm;
    Context ctx;
    Handler handler;
    Globals g = new Globals();

    taskVeiculosEstacionados(Context ctx, Handler handler) {
        this.ctx = ctx;
        this.handler = handler;
    }

    @Override
    protected Void doInBackground(String... params) {
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
                //query para ir buscar / verificar o utilizador que está a fazer login
                ResultSet rs = statement.executeQuery("SELECT DISTINCT veiculos.matricula, parque.nome, parque.localizacao, estacionamento.dataEntrada, estacionamento.dataSaida, plano.tipo, estacionamento.valor " +
                                "FROM plano INNER JOIN planoAcessoUtilizador ON plano.id = planoAcessoUtilizador.id_plano " +
                                "INNER JOIN utilizadores ON planoAcessoUtilizador.id_utilizador = utilizadores.id " +
                                "INNER JOIN veiculos ON utilizadores.id = veiculos.id_utilizador " +
                                "INNER JOIN estacionamento ON veiculos.id = estacionamento.id_matricula " +
                                "INNER JOIN parque ON estacionamento.id_parque = parque.id " +
                                "WHERE utilizadores.id = " + params[0]);
                connection.close();
            } catch (ClassNotFoundException | SQLException e) {
                Log.println(Log.INFO, "SQL EXCEPTION: ", e.toString());
            }
            session.disconnect();
        } catch (JSchException e) {
            Log.println(Log.INFO, "JSCH EXCEPTION: ", e.toString());
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values){
        handler.post(new Runnable() {
            @Override
            public void run() {
                ppm = new AlertDialog.Builder(ctx)
                        .setMessage("A adicionar...")
                        .setCancelable(false)
                        .show();
            }
        });
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (ppm.isShowing()) ppm.dismiss();
            }
        });
    }
}
