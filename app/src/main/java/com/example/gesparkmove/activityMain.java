package com.example.gesparkmove;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class activityMain extends AppCompatActivity{
    //declaração de variáveis
    EditText editTextMainUser;
    EditText editTextMainPassword;
    String email;
    Button buttonMainLogin;
    Button buttonMainRegister;
    Button buttonMainRecover;
    private Handler mainHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //associação das variáveis aos elementos visuais
        editTextMainUser = findViewById(R.id.editTextMainUser);
        editTextMainPassword = findViewById(R.id.editTextMainPassword);
        buttonMainLogin = findViewById(R.id.buttonMainLogin);
        buttonMainRegister = findViewById(R.id.buttonMainRegister);
        buttonMainRecover = findViewById(R.id.buttonMainRecover);
        editTextMainUser.addTextChangedListener(loginTextWatcher);
        editTextMainPassword.addTextChangedListener(loginTextWatcher);

        //ação do botão de login
        buttonMainLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                closeKeyboard();
                //asynctask para verificar a identidade do utilizador e fazer login
                new taskLogin(activityMain.this, mainHandler, activityMain.this).execute(editTextMainUser.getText().toString(), editTextMainPassword.getText().toString());
            }
        });
        //ação do botão de registar
        buttonMainRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(activityMain.this, activityRegister.class));
                finish();
            }
        });
        //ação do botão de recuperar
        buttonMainRecover.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AlertDialog.Builder ap = new AlertDialog.Builder(activityMain.this);
                ap.setTitle("Recuperar Password");
                ap.setMessage("Introduza email");
                final EditText editTextRecover = new EditText(activityMain.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                editTextRecover.setLayoutParams(lp);
                ap.setView(editTextRecover);
                ap.setPositiveButton("Enviar",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which){
                                email = editTextRecover.getText().toString();
                                new taskRecovery(activityMain.this, mainHandler).execute(email);
                            }
                        });
                ap.setNegativeButton("Voltar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                ap.show();
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
            String usernameInput = editTextMainUser.getText().toString().trim();
            String passwordInput = editTextMainPassword.getText().toString().trim();
            buttonMainLogin.setEnabled(!usernameInput.isEmpty() && !passwordInput.isEmpty() && s.length() > 7);
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };
}
