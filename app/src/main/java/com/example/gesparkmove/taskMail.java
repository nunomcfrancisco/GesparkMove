package com.example.gesparkmove;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

//asyncTask para enviar mails
public class taskMail extends AsyncTask<String, String, String> {
    //declaração de variáveis
    @SuppressLint("StaticFieldLeak")
    Context ctx;
    Globals g = new Globals();
    //contrutor
    taskMail(Context ctx){
        this.ctx = ctx;
    }

    @Override
    protected String doInBackground(String... params){
        try {
            if(params[3].equals("1")){
                //enviar mail de ativação de conta
                MailSender sender = new MailSender(ctx, g.getMailUsername(), g.getMailPass());
                sender.sendActivateMail(params[0], params[1], params[0], params[2]);
            }else{
                //enviar mail de recuperação de password
                MailSender sender = new MailSender(ctx, g.getMailUsername(), g.getMailPass());
                sender.sendRecoveryMail(params[0], params[1], params[2]);
            }
        }catch (Exception e){
            Log.println(Log.INFO, "ErrorMessage", String.valueOf(e));
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s){
        super.onPostExecute(s);
    }
}
