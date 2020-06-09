package com.example.gesparkmove;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class MainActivity extends AppCompatActivity{
    EditText editTextMainUtilizador;
    EditText editTextMainPassword;
    Button buttonMainLogin;
    Button buttonMainRegistar;
    Button buttonMainRecuperar;
    Globals g = new Globals();
    private Handler mainHandler = new Handler();
    public static boolean taskFinish = false;

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
                taskLogin tl = new taskLogin(MainActivity.this, mainHandler);
                tl.execute("SELECT id, nif, nome, email, password, avatar, activo FROM utilizadores WHERE email = \""
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
            Objects.requireNonNull(imm).hideSoftInputFromWindow(view.getWindowToken(), 0);
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
            buttonMainLogin.setEnabled(!usernameInput.isEmpty() && !passwordInput.isEmpty() && s.length() > 7);
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };
}
