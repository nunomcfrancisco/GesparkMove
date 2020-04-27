package com.example.gesparkmove;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
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

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import com.mysql.jdbc.Util;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class MainActivity extends AppCompatActivity {
    EditText editTextMainUtilizador;
    EditText editTextMainPassword;
    Button buttonMainLogin;
    Button buttonMainRegistar;
    Button buttonMainRecuperar;
    Globals g = new Globals();
    Utilizador user;
    private Handler mainHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextMainUtilizador = (EditText) findViewById(R.id.editTextMainUtilizador);
        editTextMainPassword = (EditText) findViewById(R.id.editTextMainPassword);
        buttonMainLogin = (Button) findViewById(R.id.buttonMainLogin);
        buttonMainRegistar = (Button) findViewById(R.id.buttonMainRegistar);
        buttonMainRecuperar = (Button) findViewById(R.id.buttonMainRecuperar);
        editTextMainUtilizador.addTextChangedListener(loginTextWatcher);
        editTextMainPassword.addTextChangedListener(loginTextWatcher);

        buttonMainLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                closeKeyboard();
                new loginTask().execute("SELECT * FROM utilizadores WHERE email = \""
                        + editTextMainUtilizador.getText().toString() + "\"",
                        editTextMainPassword.getText().toString());
            }
        });

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
        Bundle bundle = new Bundle();
        AlertDialog ppm;
        @Override
        protected void onPreExecute(){
        }

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
                        bundle.putInt("id", rs.getInt(1));
                        queryResult += rs.getString(2);
                    }
                    connection.close();
                }catch (ClassNotFoundException | SQLException e){}
                session.disconnect();
            } catch (JSchException e){}
            if(queryResult.equals("")){
                return "u";
            }
            else if(queryResult.equals(new md5Tools().encode(params[1]))){
                return "y";
            }else{
                return "n";
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
            if(result.equals("u")){
                ppm.dismiss();
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ppm = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Erro!")
                                .setMessage("Utilizador inválido!")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {}
                                })
                                .show();
                    }
                });
            }else if(result.equals("n")){
                ppm.dismiss();
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ppm = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Erro!")
                                .setMessage("Password Inválida!")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {}
                                })
                                .show();
                    }
                });
            }else if(result.equals("y")){
                ppm.dismiss();
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainActivity.this, UtilizadorActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }
    }
}
