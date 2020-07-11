package com.example.gesparkmove;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.Objects;
import java.util.Properties;

//asynTask para adicionar um veículo à base de dados
public class taskAddCar extends AsyncTask<String, Integer, Void> {
    //declaração das variaveis
    AlertDialog ad;
    Context ctx;
    Handler handler;
    Activity activity;
    FragmentManager manager;
    ArrayList<Car> veiculo = new ArrayList<>();
    Globals g = new Globals();

    //construtor
    taskAddCar(Context ctx, Handler handler, Activity activity, FragmentManager manager){
        this.ctx = ctx;
        this.handler = handler;
        this.activity = activity;
        this.manager = manager;
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
                //abrir ligação para a base de dados
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = (Connection) DriverManager.getConnection(g.getMySqlUrl(), g.getMySqlUsername(), g.getMySqlPass());
                Statement statement = (Statement) connection.createStatement();
                //query para inserir um veiculo na base de dados
                statement.execute("INSERT INTO veiculos (matricula, id_utilizador, id_marca, id_modelo, cor, activo, imagem) VALUES('"
                        + params[0] +  "', " + params[1] + "," + params[2] + ", " + params[3] +  ", '" + params[4] + "', 0, NULL)");
                //query para ir buscar a lista de veiculos atualizada do utilizador
                ResultSet rs = statement.executeQuery("SELECT veiculos.id, veiculos.matricula, marcas.marca, modelo.modelo, cor, estacionado, veiculos.activo, veiculos.imagem FROM veiculos inner join marcas inner join modelo WHERE id_utilizador = " + params[1] + " AND veiculos.id_marca = marcas.id AND veiculos.id_modelo = modelo.id");
                while(rs.next())
                    //adiciona cada veículo do utilizador à lista de veículos
                    veiculo.add(new Car(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6), rs.getInt(7), rs.getString(8)));
                //fecha a ligação à base de dados
                connection.close();
            } catch (ClassNotFoundException | SQLException e) {
                Log.println(Log.INFO, "SQL EXCEPTION: ", e.toString());
            }
            //fecha o tunel SSH
            session.disconnect();
        }catch (JSchException e){
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
        ad.dismiss();
        handler.post(new Runnable() {
            @Override
            public void run() {
                Intent intent = activity.getIntent();
                //guarda "em memoria" a lista dos veiculos do utilizador
                intent.putExtra("VEICULO", veiculo);
                User user = Objects.requireNonNull(intent.getExtras()).getParcelable("USER");
                user.setCars(user.getCars() + 1);
                intent.putExtra("USER", user);
                Toast.makeText(ctx, "Matricula adicionada", Toast.LENGTH_SHORT).show();
                fragmentListCar cFragment = new fragmentListCar();
                manager.beginTransaction()
                        .replace(R.id.containerFragment, cFragment, "consultar")
                        .commit();
            }
        });
    }
}