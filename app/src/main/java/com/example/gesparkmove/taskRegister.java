package com.example.gesparkmove;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import java.util.Random;

//asyncTask para efetuar o registo de utilizador
public class taskRegister extends AsyncTask<String, Integer, Void> {
    //declaração de variáveis
    @SuppressLint("StaticFieldLeak")
    Context ctx;
    AlertDialog ppm;
    Handler handler;
    Globals g = new Globals();
    mailHelper mh = new mailHelper();
    int codAtiv;
    //asyncTask
    taskRegister(Context ctx, Handler handler){
        this.ctx = ctx;
        this.handler = handler;
    }

    @Override
    protected Void doInBackground(String... params){
        publishProgress(0);
        int id = 0;
        //gera código de ativação que vai ser enviado para o novo utilizador
        codAtiv = new Random().nextInt(99999999) + 10000001;
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
                //query para gravar a informação do novo utilizador
                statement.executeUpdate("INSERT INTO utilizadores (nif, nome, morada, codigoPostal, email, password, dataRegisto, nivelAcesso, activo, codigoActivacao) VALUES ("
                        + params[0] + ", '" + params[1] + "', '" + params[2] + "', " + params[3] + ", '" + params[4] + "', '" + new md5Tools().encode(params[5])
                        + "', NOW(), 0, 0, " + codAtiv + ")");
                mh.mail = params[4];
                mh.user = params[1];
                //query para obter o id do novo utilizador
                ResultSet rs = statement.executeQuery("SELECT id FROM utilizadores WHERE email = " + params[4]);
                while(rs.next()){
                    id = rs.getInt(1);
                }
                //query para gravar o contato do novo utilizador
                statement.executeUpdate("INSERT INTO contactos (id_utilizador, contacto) VALUES (" + id + ", " + params[6] + ")");
                //fechar ligação à base de dados
                connection.close();
            }catch (ClassNotFoundException | SQLException e){
                Log.println(Log.INFO, "ErrorMessage", String.valueOf(e));
            }
            //fechar tunel SSH
            session.disconnect();
        } catch (JSchException e){
            Log.println(Log.INFO, "ErrorMessage", String.valueOf(e));
        }
        return null;
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
    protected void onPostExecute(Void aVoid){
        //envio de email com o código de ativação de conta
        taskMail tm = new taskMail(ctx);
        tm.execute(mh.mail, mh.user, Integer.toString(codAtiv), "1");
        handler.post(new Runnable() {
            @Override
            public void run() {
                ppm = new AlertDialog.Builder(ctx)
                        .setMessage("Consulte o seu email para ativar a conta.")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(ctx, activityMain.class);
                                ctx.startActivity(intent);
                            }
                        })
                        .show();
            }
        });
    }
}
