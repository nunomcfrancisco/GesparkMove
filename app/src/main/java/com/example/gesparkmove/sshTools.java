package com.example.gesparkmove;

import android.os.AsyncTask;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.util.Properties;

public class sshTools {
    Globals g = new Globals();
    Session session;
    class Task extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids){
            try {
                JSch jsch = new JSch();
                session = jsch.getSession(g.getSshUsername(), g.getSshHost(), g.getSshPort());
                session.setPassword(g.getSshPass());
                session.setPortForwardingL(g.getSshPFLPort(), g.getSshPFHost(), g.getSshPFRPort());
                Properties prop = new Properties();
                prop.put("StrictHostKeyChecking", "no");
                session.setConfig(prop);
                session.connect();
            } catch (JSchException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void doAfter(){
            session.disconnect();
        }
    }

    public void sshConnect(){
        Task task = new Task();
        task.doInBackground();
    }

    public void sshDisconnect(){
        Task task = new Task();
        task.doAfter();
    }
}
