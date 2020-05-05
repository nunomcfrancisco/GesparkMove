package com.example.gesparkmove;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Toast;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class GPMTasks extends AsyncTask<String, Void, String>{
    Globals g = new Globals();
    private Utilizador user;
    private ProgressDialog pg;
    private Context ctx;
    private Handler hdl;

    public GPMTasks(Context ctx, Handler hdl){
        this.ctx = ctx;
        this.hdl = hdl;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        pg = ProgressDialog.show(ctx, "Aguarde...", "A entrar...", false, false);
    }

    @Override
    protected String doInBackground(String... params) {
        String queryResult = "";
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
                ResultSet rs = statement.executeQuery(params[0]);
                while (rs.next()){
                    user = new Utilizador(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), 0, rs.getString(6),0);
                    queryResult += rs.getString(5);
                }
                rs = statement.executeQuery(params[2]);
                while(rs.next()) user.setCarros(rs.getInt(1));
                connection.close();
            } catch (ClassNotFoundException | SQLException e) {
                Toast.makeText(ctx, "Erro ao efetuar query", Toast.LENGTH_LONG).show();
            }
            session.disconnect();
        } catch (JSchException e) {
            Toast.makeText(ctx, "Erro a abrir ligação SSH", Toast.LENGTH_LONG).show();
        }
        if(queryResult.equals("")) return "u";
        else if (queryResult.equals(new md5Tools().encode(params[1]))) return "y";
        else return "p";
    }

    @Override
    protected void onPostExecute(final String result){
        pg.dismiss();
        if(result.equals("u")) Toast.makeText(ctx, "Utilizador desconhecido!", Toast.LENGTH_LONG).show();
        else if(result.equals("p")) Toast.makeText(ctx, "Password errada!", Toast.LENGTH_LONG).show();
        else if(result.equals("y")) Toast.makeText(ctx, "Fixe!", Toast.LENGTH_LONG).show();
        }
    }


