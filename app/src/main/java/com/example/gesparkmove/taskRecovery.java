package com.example.gesparkmove;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import java.util.Random;

//asyncTask para efetuar o reset à password do utilizador
public class taskRecovery extends AsyncTask<String, Integer, Void> {
    //declaração de variáveis
    AlertDialog ad;
    Context ctx;
    Handler handler;
    Random generator = new Random();
    StringBuilder randomStringBuilder = new StringBuilder();
    char tempChar;
    int control = 0;
    Globals g = new Globals();
    mailHelper mh = new mailHelper();
    //Contrutor
    taskRecovery(Context ctx, Handler handler){
        this.ctx = ctx;
        this.handler = handler;
    }

    @Override
    protected Void doInBackground(String... params) {
        publishProgress(0);
        //gera uma password aleatória com 10 caracteres
        for(int i = 0; i < 10; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append((tempChar));
        }
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
                //query para ir buscar o nome do utilizador
                ResultSet rs = statement.executeQuery("SELECT nome FROM utilizadores WHERE email = '" + params[0] + "'");
                if(rs.next()){
                    mh.user = rs.getString(1);
                    mh.mail = params[0];
                    //update da password na base de dados
                    statement.executeUpdate("UPDATE utilizadores SET password = '" + new md5Tools().encode(randomStringBuilder.toString()) + "' WHERE email = '" + params[0] + "'");
                }else{
                    control = 1;
                }
                //fechar ligação à base de dados
                connection.close();
            }catch (ClassNotFoundException | SQLException e){
                Log.println(Log.INFO, "SQL Exception: ERRO", e.toString());
            }
            //fechar tunel SSH
            session.disconnect();
        }catch (
        JSchException e){
            Log.println(Log.INFO, "JSch Exception: ", e.toString());
        }
        return null;
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
    protected void onPostExecute(Void aVoid) {
        if (control == 0) {
            //se o email existir na base de dados é enviado email com a password
            taskMail tm = new taskMail(ctx);
            tm.execute(mh.mail, mh.user, randomStringBuilder.toString(), "2");
            handler.post(new Runnable() {
                @Override
                public void run() {
                    ad = new AlertDialog.Builder(ctx)
                            .setMessage("Consulte o seu email.")
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
        } else {
            //se o email for desconhecido abre janela com aviso
            handler.post(new Runnable() {
                @Override
                public void run() {
                    ad = new AlertDialog.Builder(ctx)
                            .setMessage("Email desconhecido.")
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
}
