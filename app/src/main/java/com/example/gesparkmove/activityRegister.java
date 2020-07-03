package com.example.gesparkmove;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class activityRegister extends AppCompatActivity {
    //declaração de variaveis
    TextView editTextRegisterName, editTextRegisterAddress,
            editTextRegisterPostalCode, editTextRegisterContacto,
            editTextRegisterFiscalCode, editTextRegisterMail,
            editTextRegisterPassword01, editTextRegisterPassword02;
    Button buttonRegisterRegister, buttonRegisterBack;
    private Handler registarHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //associação das varáveis aos elementos visuais
        editTextRegisterName = findViewById(R.id.editTextRegisterName);
        editTextRegisterName.addTextChangedListener(registerTextWatcher);
        editTextRegisterAddress = findViewById(R.id.editTextRegisterAddress);
        editTextRegisterAddress.addTextChangedListener(registerTextWatcher);
        editTextRegisterPostalCode = findViewById(R.id.editTextRegisterPostalCode);
        editTextRegisterPostalCode.addTextChangedListener(registerTextWatcher);
        editTextRegisterContacto = findViewById(R.id.editTextRegisterContact);
        editTextRegisterContacto.addTextChangedListener(registerTextWatcher);
        editTextRegisterFiscalCode = findViewById(R.id.editTextRegisterFiscalCode);
        editTextRegisterFiscalCode.addTextChangedListener(registerTextWatcher);
        editTextRegisterMail = findViewById(R.id.editTextRegisterMail);
        editTextRegisterMail.addTextChangedListener(registerTextWatcher);
        editTextRegisterPassword01 = findViewById(R.id.editTextRegisterPassword01);
        editTextRegisterPassword01.addTextChangedListener(registerTextWatcher);
        editTextRegisterPassword02 = findViewById(R.id.editTextRegisterPassword02);
        editTextRegisterPassword02.addTextChangedListener(registerTextWatcher);
        buttonRegisterRegister = findViewById(R.id.buttonRegisterRegister);
        buttonRegisterBack = findViewById(R.id.buttonRegisterBack);

        //ação do botão registar
        buttonRegisterRegister.setOnClickListener(new View.OnClickListener(){
            AlertDialog ad;
            @Override
            public void onClick(View view){
                //verifica se as duas passwords introduzidas são iguais
                if(!editTextRegisterPassword01.getText().toString().equals(editTextRegisterPassword02.getText().toString())) {
                    ad = new AlertDialog.Builder(activityRegister.this).setTitle("Erro")
                            .setMessage("As password não são iguais!").setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                }else {
                    //asynctask para guardar os dados do novo utilizador
                    new taskRegister(activityRegister.this, registarHandler)
                    .execute(editTextRegisterFiscalCode.getText().toString(), editTextRegisterName.getText().toString(),
                            editTextRegisterAddress.getText().toString(), editTextRegisterPostalCode.getText().toString(),
                            editTextRegisterMail.getText().toString(), editTextRegisterPassword01.getText().toString(),
                            editTextRegisterContacto.getText().toString());
                }
            }
        });
        //ação do botão voltar
        buttonRegisterBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(activityRegister.this, activityMain.class));
                finish();
            }
        });
    }

    //Textwatcher faz enable to botão registar quando todos os campos estão preenchidos
    private TextWatcher registerTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String nameInput = editTextRegisterName.getText().toString().trim();
            String addressInput = editTextRegisterAddress.getText().toString().trim();
            String postalCodeInput = editTextRegisterPostalCode.getText().toString().trim();
            String contactoInput = editTextRegisterContacto.getText().toString().trim();
            String fiscalCodeInput = editTextRegisterFiscalCode.getText().toString().trim();
            String mailInput = editTextRegisterMail.getText().toString().trim();
            String password01Input = editTextRegisterPassword01.getText().toString().trim();
            String password02Input = editTextRegisterPassword02.getText().toString().trim();
            buttonRegisterRegister.setEnabled(!nameInput.isEmpty() && !addressInput.isEmpty() && !postalCodeInput.isEmpty()
                && !contactoInput.isEmpty() && !fiscalCodeInput.isEmpty() && !mailInput.isEmpty() && !password01Input.isEmpty()
                && !password02Input.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    @Override
    public void onBackPressed() {
    }
}
