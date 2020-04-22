package com.example.gesparkmove;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    EditText editTextMainUtilizador;
    EditText editTextMainPassword;
    Button buttonMainLogin;
    Button buttonMainRegistar;
    Button buttonMainRecuperar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextMainUtilizador = (EditText) findViewById(R.id.editTextMainUtilizador);
        editTextMainPassword = (EditText) findViewById(R.id.editTextMainPassword);
        buttonMainLogin = (Button) findViewById(R.id.buttonMainLogin);
        buttonMainRegistar = (Button) findViewById(R.id.buttonMainRegistar);
        buttonMainRecuperar = (Button) findViewById(R.id.buttonMainRecuperar);

        buttonMainLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(editTextMainUtilizador.getText().toString().equals("xxx") && editTextMainPassword.getText().toString().equals("xxx")){
                    startActivity(new Intent(MainActivity.this, UtilizadorActivity.class));
                    finish();
                }
            }
        });

        buttonMainRegistar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(MainActivity.this, RegistarActivity.class));
            }
        });
    }
}
