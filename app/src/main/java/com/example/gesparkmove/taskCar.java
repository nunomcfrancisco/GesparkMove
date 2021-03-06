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

//asyncTask para obter a informação de um determinado veículo
public class taskCar extends AsyncTask<String, Integer, Void> {
    //declaração de variáveis
    Globals g = new Globals();
    AlertDialog ppm;
    Context ctx;
    Handler handler;
    private final onCarListener listener;
    int plan, history;
    //construtor
    public taskCar(Context ctx, Handler handler, onCarListener listener) {
        this.ctx = ctx;
        this.handler = handler;
        this.listener = listener;
    }

    @Override
    protected Void doInBackground(String... params) {
        publishProgress(0);
        try {
            //abre o tunel SSH
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
                //obtem o plano de pagamento associado ao veículo
                ResultSet rs = statement.executeQuery("SELECT id_plano FROM planoAcessoUtilizador WHERE id_veiculo = " + params[0]);
                if(rs.next())
                    while(rs.next())
                        plan = rs.getInt(1);
                else
                    plan = 0;
                //verifica se o veículo tem historico de estacionamentos
                rs = statement.executeQuery("SELECT * FROM estacionamento WHERE id_matricula = " + params[0] + " LIMIT 1");
                if(rs.next())
                    history = 1;
                else
                    history = 0;
                //fecha a ligação à base de dados
                connection.close();
            }catch (ClassNotFoundException | SQLException e){
                Log.println(Log.INFO, "ErrorMessage", String.valueOf(e));
            }
            //fecha o tunel SSH
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
        listener.onCarCompleted(plan, history);
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(ppm.isShowing())
                    ppm.dismiss();
            }
        });
    }
}
