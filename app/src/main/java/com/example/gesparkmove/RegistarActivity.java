package com.example.gesparkmove;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class RegistarActivity extends AppCompatActivity {
    TextView editTextRegistarNome, editTextRegistarMorada,
            editTextRegistarCodigoPostal, editTextRegistarContato,
            editTextRegistarNumeroFiscal, editTextRegistarMail,
            editTextRegistarPassword01, editTextRegistarPassword02;
    Button buttonRegistarRegistar, buttonRegistarVoltar;
    String records = "";
    Globals g = new Globals();

    private Handler registarHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registar);
        editTextRegistarNome = findViewById(R.id.editTextRegistarNome);
        editTextRegistarNome.addTextChangedListener(registarTextWatcher);
        editTextRegistarMorada = findViewById(R.id.editTextRegistarMorada);
        editTextRegistarMorada.addTextChangedListener(registarTextWatcher);
        editTextRegistarCodigoPostal = findViewById(R.id.editTextRegistarCodigoPostal);
        editTextRegistarCodigoPostal.addTextChangedListener(registarTextWatcher);
        editTextRegistarContato = findViewById(R.id.editTextRegistarContato);
        editTextRegistarContato.addTextChangedListener(registarTextWatcher);
        editTextRegistarNumeroFiscal = findViewById(R.id.editTextRegistarNumeroFiscal);
        editTextRegistarNumeroFiscal.addTextChangedListener(registarTextWatcher);
        editTextRegistarMail = findViewById(R.id.editTextRegistarMail);
        editTextRegistarMail.addTextChangedListener(registarTextWatcher);
        editTextRegistarPassword01 = findViewById(R.id.editTextRegistarPassword01);
        editTextRegistarPassword01.addTextChangedListener(registarTextWatcher);
        editTextRegistarPassword02 = findViewById(R.id.editTextRegistarPassword02);
        editTextRegistarPassword02.addTextChangedListener(registarTextWatcher);
        buttonRegistarRegistar = findViewById(R.id.buttonRegistarRegistar);
        buttonRegistarVoltar = findViewById(R.id.buttonRegistarVoltar);

        buttonRegistarRegistar.setOnClickListener(new View.OnClickListener(){
            AlertDialog ppm;
            @Override
            public void onClick(View view){
                if(!editTextRegistarPassword01.getText().toString().equals(editTextRegistarPassword02.getText().toString())) {
                    ppm = new AlertDialog.Builder(RegistarActivity.this).setTitle("Erro")
                            .setMessage("As password não são iguais!").setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                }else {
                    new registarTask().execute(editTextRegistarNome.getText().toString(), editTextRegistarMorada.getText().toString(),
                            editTextRegistarCodigoPostal.getText().toString(), editTextRegistarContato.getText().toString(),
                            editTextRegistarNumeroFiscal.getText().toString(), editTextRegistarMail.getText().toString(),
                            editTextRegistarPassword01.getText().toString());
                }
            }
        });

        buttonRegistarVoltar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(RegistarActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private TextWatcher registarTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String nomeInput = editTextRegistarNome.getText().toString().trim();
            String moradaInput = editTextRegistarMorada.getText().toString().trim();
            String codigoPostalInput = editTextRegistarCodigoPostal.getText().toString().trim();
            String contatoInput = editTextRegistarContato.getText().toString().trim();
            String numeroFiscalInput = editTextRegistarNumeroFiscal.getText().toString().trim();
            String mailInput = editTextRegistarMail.getText().toString().trim();
            String password01Input = editTextRegistarPassword01.getText().toString().trim();
            String password02Input = editTextRegistarPassword02.getText().toString().trim();
            buttonRegistarRegistar.setEnabled(!nomeInput.isEmpty() && !moradaInput.isEmpty() && !codigoPostalInput.isEmpty()
                && !contatoInput.isEmpty() && !numeroFiscalInput.isEmpty() && !mailInput.isEmpty() && !password01Input.isEmpty()
                && !password02Input.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    class registarTask extends AsyncTask<String, Integer, Void>{
        AlertDialog ppm;
        @Override
        protected Void doInBackground(String... params){
            publishProgress(0);

            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... values){
            registarHandler.post(new Runnable() {
                @Override
                public void run() {
                    ppm = new AlertDialog.Builder(RegistarActivity.this)
                            .setMessage("A registar!")
                            .setCancelable(false)
                            .show();
                }
            });
        }
        @Override
        protected void onPostExecute(Void aVoid){
            super.onPostExecute(aVoid);
        }
    }
}
