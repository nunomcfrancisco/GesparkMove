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

//asyncTask para gravar alterações feitas em um veículo
public class taskSaveCar extends AsyncTask<String, Integer, Boolean> {
    //declaração de variáveis
    AlertDialog ppm;
    Context ctx;
    Handler handler;
    Globals g = new Globals();
    FragmentManager manager;
    //contrutor
    taskSaveCar(Context ctx, Handler handler, FragmentManager manager){
        this.ctx = ctx;
        this.handler = handler;
        this.manager = manager;
    }

    @Override
    protected Boolean doInBackground(String... params) {
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
                //verifica se o utilizar tem um metodo de pagamento configurado
                ResultSet rs = statement.executeQuery("SELECT EXISTS(SELECT id_utilizador FROM metodosPagamentoUtilizador WHERE id_utilizador = " + params[3] + ")");
                rs.next();
                if(rs.getString(1).equals("1")){
                    //gravar o ativo/desativo
                    statement.execute("UPDATE veiculos SET activo = " + params[1] + " WHERE id = " + params[0]);
                    //verifica se o carro já tem entrada na tabela planoAcessoUtilizador, se sim atualiza o plano, senão faz insert
                    rs = statement.executeQuery("SELECT EXISTS(SELECT id_veiculo FROM planoAcessoUtilizador WHERE id_veiculo = " + params[0] + ")");
                    rs.next();
                    if(rs.getString(1).equals("1"))
                        //atualiza o plano de pagamento para um determinado veículo
                        statement.executeUpdate("UPDATE planoAcessoUtilizador SET id_plano = " + params[2] + " WHERE id_veiculo = " + params[0]);
                    else
                        //grava o plano de pagamento para um determinado veículo
                        statement.execute("INSERT INTO planoAcessoUtilizador (id_utilizador, id_plano, id_veiculo, activo) VALUES (" + params[3] + ", " + params[2] + ", " + params[0] + ", 1)");
                    //fechar ligação à base de dados
                    connection.close();
                }else{
                    //fechar ligação à base de dados
                    connection.close();
                    //fechar tunel SSH
                    session.disconnect();
                    return false;
                }
            } catch (ClassNotFoundException | SQLException e){
                Log.println(Log.INFO, "SQL EXCEPTION: ", e.toString());
            }
            //fechar tunel SSH
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
                ppm = new AlertDialog.Builder(ctx, R.style.AlertDialogCustom).setView(R.layout.progress_bar).setCancelable(false).show();
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
                                            fragmentListCar cFragment = new fragmentListCar();
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
