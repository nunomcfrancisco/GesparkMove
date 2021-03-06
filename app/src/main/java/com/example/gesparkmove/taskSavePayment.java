package com.example.gesparkmove;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

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

//task para salvar o método de pagamento
public class taskSavePayment extends AsyncTask<String, Integer, String> {
    //declaração das variáveis
    AlertDialog ad;
    Context ctx;
    Handler handler;
    FragmentManager manager;
    Globals g = new Globals();

    //construtor
    taskSavePayment(Context ctx, Handler handler, FragmentManager manager){
        this.ctx = ctx;
        this.handler = handler;
        this.manager = manager;
    }

    @Override
    protected String doInBackground(String... params) {
        publishProgress(0);
        //abre o tunel SSH
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(g.getSshUsername(), g.getSshHost(), g.getSshPort());
            session.setPassword(g.getSshPass());
            session.setPortForwardingL(g.getSshPFLPort(), g.getSshPFHost(), g.getSshPFRPort());
            Properties prop = new Properties();
            prop.put("StrictHostKeyChecking", "no");
            session.setConfig(prop);
            session.connect();
            //abre a ligação à base de dados
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = (Connection) DriverManager.getConnection(g.getMySqlUrl(), g.getMySqlUsername(), g.getMySqlPass());
                Statement statement = (Statement) connection.createStatement();
                //query para obter o método de pagamento atual do utilizador
                ResultSet rs = statement.executeQuery("SELECT * FROM metodosPagamentoUtilizador WHERE id_utilizador = " + params[0]);
                if (!rs.next()) {
                    switch (params[1]) {
                        case "1":
                            //query para guardar os dados do método de pagamento caso seja cartão de crédito
                            statement.execute("INSERT INTO metodosPagamentoUtilizador (id_utilizador, id_metodoPagamento, nomeCartaoCredito, numeroCartaoCredito, dataValidadeCartaoCredito, codigoCvvCredito) VALUES (" + params[0] + ", " + params[1] + ", '"
                                    + params[2] + "', " + params[3] + ", '" + params[4] + "', " + params[5] + ")");
                            break;
                        case "2":
                            //query para guardar os dados do método de pagamento caso seja MBWay
                            statement.execute("INSERT INTO metodosPagamentoUtilizador (id_utilizador, id_metodoPagamento, telefoneMbway) VALUES (" + params[0] + ", " + params[1] + ", " + params[2] + ")");
                            break;
                        case "3":
                            //query para guardar os dados do método de pagamento caso seja débito direto
                            statement.execute("INSERT INTO metodosPagamentoUtilizador (id_utilizador, id_metodoPagamento, dDirectoNome, dDirectoIban) VALUES (" + params[0] + ", " + params[1] + ", '" + params[2] + "', '" + params[3] + "')");
                            break;
                    }
                } else {
                    switch (params[1]) {
                        case "1":
                            //query para atualizar os dados do método de pagamento caso seja cartão de crédito
                            statement.execute("UPDATE metodosPagamentoUtilizador SET id_metodoPagamento = " + params[1] + ", nomeCartaoCredito = '" + params[2] + "', numeroCartaoCredito = "
                                    + params[3] + ", dataValidadeCartaoCredito = '" + params[4] + "', codigoCvvCredito= " + params[5] + " WHERE id_utilizador = " + params[0]);
                            break;
                        case "2":
                            //query para atualizar os dados do método de pagamento caso seja MBWay
                            statement.execute("UPDATE metodosPagamentoUtilizador SET id_metodoPagamento = " + params[1] + ", telefoneMbway = " + params[2] + " WHERE id_utilizador = " + params[0]);
                            break;
                        case "3":
                            //query para atualizar os dados do método de pagamento caso seja debito direto
                            statement.execute("UPDATE metodosPagamentoUtilizador SET id_metodoPagamento = " + params[1] + ", dDirectoNome = '" + params[2] + "', dDirectoIban = '" + params[3] + "' WHERE id_utilizador = " + params[0]);
                            break;
                    }
                }
                //fechar ligação à base de dados
                connection.close();
            } catch (ClassNotFoundException | SQLException e) {
                Log.println(Log.INFO, "SQL EXCEPTION: ", e.toString());
            }
            //fechar tunel SSH
            session.disconnect();
        }catch (JSchException e){
            Log.println(Log.INFO, "JSch Exception: ", e.toString());
        }
        return null;
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
    protected void onPostExecute(String s) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (ad.isShowing())
                    ad.dismiss();
                fragmentPayment pFragment = new fragmentPayment();
                manager.beginTransaction()
                        .replace(R.id.containerFragment, pFragment, "pagamentos")
                        .commit();
            }
        });
    }
}
