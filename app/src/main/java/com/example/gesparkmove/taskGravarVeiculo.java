package com.example.gesparkmove;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class taskGravarVeiculo extends AsyncTask<String, Integer, Boolean> {
    AlertDialog ppm;
    Context ctx;
    Handler handler;
    Globals g = new Globals();
    FragmentManager manager;

    taskGravarVeiculo(Context ctx, Handler handler, FragmentManager manager){
        this.ctx = ctx;
        this.handler = handler;
        this.manager = manager;
    }

    @Override
    protected Boolean doInBackground(String... params) {
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
                ResultSet rs = statement.executeQuery("SELECT DISTINCT(id_utilizador), IF(id_utilizador = " + params[3] + ", 'TRUE', 'FALSE') FROM metodosPagamentoUtilizador");
                //Log.println(Log.INFO, "RESULTADO T/F :::", rs.getString(2));
                rs.next();
                if(rs.getString(2).equals("TRUE")){
                    statement.executeUpdate("UPDATE planoAcessoUtilizador SET id_plano = " + params[2] + " WHERE id_veiculo = " + params[0]);
                    statement.execute("UPDATE veiculos SET activo = " + params[1] + " WHERE id = " + params[0]);
                    connection.close();
                }else{
                    connection.close();
                    session.disconnect();
                    return false;
                }
            } catch (ClassNotFoundException | SQLException e){
                Log.println(Log.INFO, "SQL EXCEPTION: ", e.toString());
            }
            session.disconnect();
        }catch (JSchException e){
            Log.println(Log.INFO, "JSCH EXCEPTION: ", e.toString());
        }
        return true;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                ppm = new AlertDialog.Builder(ctx)
                        .setMessage("A gravar...")
                        .setCancelable(false)
                        .show();
            }
        });
    }

    @Override
    protected void onPostExecute(Boolean value) {
        if(value) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (ppm.isShowing())
                        ppm.dismiss();
                }
            });
        }else{
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (ppm.isShowing())
                        ppm.dismiss();
                    new AlertDialog.Builder(ctx)
                            .setTitle("Aviso!")
                            .setMessage("Método de pagamento não definido.\nAceda a gespark.pt para configurar um método de pagamento.")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which){
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            fragmentConsultar cFragment = new fragmentConsultar();
                                            manager.beginTransaction()
                                                    .replace(R.id.containerFragment, cFragment, "consultar")
                                                    .commit();
                                        }
                                    });
                                }
                            })
                            .show();
                }
            });
        }
    }
}
