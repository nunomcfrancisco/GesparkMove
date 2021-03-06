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

//asyncTask para obter o historico de estacionamentos de um determinado utilizador
public class taskParked extends AsyncTask<String, Integer, ArrayList<Parked>> {
    //declaração de variáveis
    AlertDialog ad;
    Context ctx;
    Handler handler;
    Globals g = new Globals();
    ArrayList<Parked> data = new ArrayList<>();
    private final onParkingListener listener;
    //contrutor
    taskParked(Context ctx, Handler handler, onParkingListener listener) {
        this.ctx = ctx;
        this.handler = handler;
        this.listener = listener;
    }

    @Override
    protected ArrayList<Parked> doInBackground(String... params) {
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
                //query para obter o histórico de estacionamentos de um determinado utilizador
                ResultSet rs = statement.executeQuery("SELECT DISTINCT veiculos.matricula, parque.nome, parque.localizacao, estacionamento.dataEntrada, estacionamento.dataSaida, plano.tipo, estacionamento.valor, estacionamento.id " +
                                "FROM plano INNER JOIN planoAcessoUtilizador ON plano.id = planoAcessoUtilizador.id_plano " +
                                "INNER JOIN utilizadores ON planoAcessoUtilizador.id_utilizador = utilizadores.id " +
                                "INNER JOIN veiculos ON utilizadores.id = veiculos.id_utilizador " +
                                "INNER JOIN estacionamento ON veiculos.id = estacionamento.id_matricula " +
                                "INNER JOIN parque ON estacionamento.id_parque = parque.id " +
                                "WHERE utilizadores.id = " + params[0] + " ORDER BY estacionamento.id DESC");
                while(rs.next())
                    data.add(new Parked(rs.getString(1), rs.getString(2) + " - " + rs.getString(3), rs.getString(4), rs.getString(5), rs.getDouble(7)));
                //fechar ligação à base de dados
                connection.close();
            } catch (ClassNotFoundException | SQLException e) {
                Log.println(Log.INFO, "SQL EXCEPTION: ", e.toString());
            }
            //fechar tunel SSH
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
    protected void onPostExecute(final ArrayList<Parked> data) {
        listener.onParkingCompleted(data);
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (ad.isShowing()) ad.dismiss();

            }
        });
    }
}
