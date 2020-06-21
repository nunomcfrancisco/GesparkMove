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
import java.util.Properties;

public class taskVeiculo extends AsyncTask<String, Integer, Void> {
    Globals g = new Globals();
    AlertDialog ppm;
    Context ctx;
    Handler handler;
    private final onVeiculoListener listener;
    int plano, historico;

    public taskVeiculo(Context ctx, Handler handler, onVeiculoListener listener) {
        this.ctx = ctx;
        this.handler = handler;
        this.listener = listener;
    }

    @Override
    protected Void doInBackground(String... params) {
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
                ResultSet rs = statement.executeQuery("SELECT id_plano FROM planoAcessoUtilizador WHERE id_veiculo = " + params[0]);
                if(rs.next() == true)
                    while(rs.next())
                        plano = rs.getInt(1);
                else
                    plano = 0;
                rs = statement.executeQuery("SELECT * FROM estacionamento WHERE id_matricula = " + params[0] + " LIMIT 1");
                if(rs.next() == true)
                    historico = 1;
                else
                    historico = 0;
                connection.close();
            }catch (ClassNotFoundException | SQLException e){
                Log.println(Log.INFO, "ErrorMessage", String.valueOf(e));
            }
            session.disconnect();
        } catch (JSchException e) {
            Log.println(Log.INFO, "ErrorMessage", String.valueOf(e));
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
        listener.onVeiculoCompleted(plano, historico);
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(ppm.isShowing())
                    ppm.dismiss();
            }
        });
    }
}
