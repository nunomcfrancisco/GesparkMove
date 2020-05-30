package com.example.gesparkmove;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class MainActivity extends AppCompatActivity{
    EditText editTextMainUtilizador;
    EditText editTextMainPassword;
    Button buttonMainLogin;
    Button buttonMainRegistar;
    Button buttonMainRecuperar;
    Globals g = new Globals();
    private Handler mainHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextMainUtilizador = findViewById(R.id.editTextMainUtilizador);
        editTextMainPassword = findViewById(R.id.editTextMainPassword);
        buttonMainLogin = findViewById(R.id.buttonMainLogin);
        buttonMainRegistar = findViewById(R.id.buttonMainRegistar);
        buttonMainRecuperar = findViewById(R.id.buttonMainRecuperar);
        editTextMainUtilizador.addTextChangedListener(loginTextWatcher);
        editTextMainPassword.addTextChangedListener(loginTextWatcher);

        //ação do botão de login
        buttonMainLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                closeKeyboard();
                new loginTask().execute("SELECT id, nif, nome, email, password, avatar, activo FROM utilizadores WHERE email = \""
                        + editTextMainUtilizador.getText().toString() + "\"",
                        editTextMainPassword.getText().toString(),
                        "SELECT COUNT(matricula) FROM veiculos WHERE id_utilizador = (SELECT id FROM utilizadores WHERE email = \""
                                + editTextMainUtilizador.getText().toString() + "\")");
            }
        });
        //ação do botão de registar
        buttonMainRegistar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(MainActivity.this, RegistarActivity.class));
                finish();
            }
        });
    }

    //closeKeyboard fecha o teclado android quando chamado
    private void closeKeyboard(){
        View view = this.getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    //TextWatcher faz enable ao botão de login quando ambas as text box tiverem texto.
    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String usernameInput = editTextMainUtilizador.getText().toString().trim();
            String passwordInput = editTextMainPassword.getText().toString().trim();
            buttonMainLogin.setEnabled(!usernameInput.isEmpty() && !passwordInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };
    //loginTask abre tunel SSH e abre ligação à base de dados para verificar as credenciais do utilizador
    private class loginTask extends AsyncTask<String, Integer, String>{
        AlertDialog ppm;
        Utilizador user;

        @Override
        protected String doInBackground(String... params){
            String queryResult = "";
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
                    ResultSet rs = statement.executeQuery(params[0]);
                    while (rs.next()){
                        user = new Utilizador(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), 5, rs.getString(6), rs.getInt(7));
                        queryResult += rs.getString(5);
                    }
                    if(!queryResult.isEmpty()){
                        rs = statement.executeQuery(params[2]);
                        while(rs.next()){
                            user.setCarros(rs.getInt(1));
                        }
                    }
                    connection.close();
                }catch (ClassNotFoundException | SQLException e){}
                session.disconnect();
            } catch (JSchException e){}
            if(queryResult.equals(""))
                return "u";
            else if(user.getAtivo() == 0)
                return "a";
            else if(queryResult.equals(new md5Tools().encode(params[1]))){
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
                        statement.executeUpdate("UPDATE utilizadores SET dataUltimoAcesso = NOW() WHERE id = " + user.getId());
                        connection.close();
                    } catch (ClassNotFoundException | SQLException e) {
                        e.printStackTrace();
                    }
                    session.disconnect();
                } catch (JSchException e) {
                    e.printStackTrace();
                }
                return "y";
            }else{
                return "p";
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values){
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    ppm = new AlertDialog.Builder(MainActivity.this)
                            .setMessage("Loading")
                            .setCancelable(false)
                            .show();
                }
            });
        }

        @Override
        protected void onPostExecute(final String result){
            if(result.equals("u")) {
                if (ppm.isShowing()) ppm.dismiss();
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ppm = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Erro!")
                                .setMessage("Utilizador inválido!")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .show();
                    }
                });
            }else if(result.equals("a")){
                if (ppm.isShowing()) ppm.dismiss();
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ppm = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Erro!")
                                .setMessage("Conta não ativa.")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .show();
                    }
                });
            }else if(result.equals("p")){
                if(ppm.isShowing()) ppm.dismiss();
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ppm = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Erro!")
                                .setMessage("Password inválida!")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {}
                                })
                                .show();
                    }
                });
            }else if(result.equals("y")){
                if(ppm.isShowing()) ppm.dismiss();
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainActivity.this, UtilizadorActivity.class);
                        intent.putExtra("USER", user);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }
    }
}
