package com.example.gesparkmove;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;
import java.util.Objects;

public class fragmentPagamentos extends Fragment {
    //declaração das variaveis
    TextView textViewNomeMetodosPagamentos, textViewNumeroMetodosPagamentos, textViewDataMetodosPagamentos;
    EditText editTextNomeMetodosPagamentos, editTextNumeroMetodosPagamentos, editTextCVMetodosPagamentos, editTextDataMetodosPagamentos;
    ImageView imageViewLogoMetodosPagamentos;
    Button buttonGravarMetodosPagamentos;
    Spinner spinnerMetodoMetodosPagamentos;
    Handler pagamentosHandler = new Handler();
    Utilizador user;
    Handler handler = new Handler();
    //interface para trabalhar a informação recebida da taskMetodoPagamento
    onPagamentosListener listener = new onPagamentosListener() {
        @Override
        public void onPagamentosCompleted(List<String> data) {
            switch (data.size()){
                case 1:
                    imageViewLogoMetodosPagamentos.setImageDrawable(getResources().getDrawable(R.drawable.mbway));
                    textViewNomeMetodosPagamentos.setText(data.get(0));
                break;
                case 2:
                    imageViewLogoMetodosPagamentos.setImageDrawable(getResources().getDrawable(R.drawable.db));
                    textViewNomeMetodosPagamentos.setText(data.get(0));
                    textViewNumeroMetodosPagamentos.setText(data.get(1));
                break;
                case 3:
                    imageViewLogoMetodosPagamentos.setImageDrawable(getResources().getDrawable(R.drawable.visa));
                    textViewNomeMetodosPagamentos.setText(data.get(0));
                    textViewNumeroMetodosPagamentos.setText(data.get(1));
                    textViewDataMetodosPagamentos.setText(data.get(2));
                break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pagamentos, container, false);
        //inicialização dos elementos visuais
        textViewNomeMetodosPagamentos = view.findViewById(R.id.textViewNomeMetodosPagamentos);
        textViewNumeroMetodosPagamentos = view.findViewById(R.id.textViewNumeroMetodosPagamentos);
        textViewDataMetodosPagamentos = view.findViewById(R.id.textViewDataMetodosPagamentos);
        imageViewLogoMetodosPagamentos = view.findViewById(R.id.imageViewLogoMetodosPagamentos);
        editTextNomeMetodosPagamentos = view.findViewById(R.id.editTextNomeMetodosPagamentos);
        editTextNumeroMetodosPagamentos = view.findViewById(R.id.editTextNumeroMetodosPagamentos);
        editTextCVMetodosPagamentos = view.findViewById(R.id.editTextCVMetodosPagamentos);
        editTextDataMetodosPagamentos = view.findViewById(R.id.editTextDataMetodosPagamentos);
        buttonGravarMetodosPagamentos = view.findViewById(R.id.buttonGravarMetodosPagamentos);
        spinnerMetodoMetodosPagamentos = view.findViewById(R.id.spinnerMetodoMetodosPagamentos);
        String[] pp = new String[]{"", "Cartão de Crédito", "Débito Direto", "MBWay"};
        ArrayAdapter<String> adapterMetodo = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, pp);
        Bundle bundle = Objects.requireNonNull(getActivity()).getIntent().getExtras();
        spinnerMetodoMetodosPagamentos.setAdapter(adapterMetodo);
        user = Objects.requireNonNull(bundle).getParcelable("USER");

        spinnerMetodoMetodosPagamentos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        editTextNomeMetodosPagamentos.setVisibility(View.GONE);
                        editTextNumeroMetodosPagamentos.setVisibility(View.GONE);
                        editTextCVMetodosPagamentos.setVisibility(View.GONE);
                        editTextDataMetodosPagamentos.setVisibility(View.GONE);
                        buttonGravarMetodosPagamentos.setVisibility(View.GONE);
                        break;
                    case 1:
                        editTextNomeMetodosPagamentos.setVisibility(View.VISIBLE);
                        editTextNomeMetodosPagamentos.setHint("Nome");
                        editTextNumeroMetodosPagamentos.setVisibility(View.VISIBLE);
                        editTextNumeroMetodosPagamentos.setHint("Número");
                        editTextCVMetodosPagamentos.setVisibility(View.VISIBLE);
                        editTextCVMetodosPagamentos.setHint("CVV2/CVC2");
                        editTextDataMetodosPagamentos.setVisibility(View.VISIBLE);
                        editTextDataMetodosPagamentos.setHint("Data Validade");
                        buttonGravarMetodosPagamentos.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        editTextNomeMetodosPagamentos.setVisibility(View.VISIBLE);
                        editTextNomeMetodosPagamentos.setHint("Nome");
                        editTextNumeroMetodosPagamentos.setVisibility(View.VISIBLE);
                        editTextNumeroMetodosPagamentos.setHint("IBAN");
                        editTextCVMetodosPagamentos.setVisibility(View.GONE);
                        editTextDataMetodosPagamentos.setVisibility(View.GONE);
                        buttonGravarMetodosPagamentos.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        editTextNomeMetodosPagamentos.setVisibility(View.GONE);
                        editTextNumeroMetodosPagamentos.setVisibility(View.VISIBLE);
                        editTextNumeroMetodosPagamentos.setHint("Telemovel");
                        editTextCVMetodosPagamentos.setVisibility(View.GONE);
                        editTextDataMetodosPagamentos.setVisibility(View.GONE);
                        buttonGravarMetodosPagamentos.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        buttonGravarMetodosPagamentos.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (spinnerMetodoMetodosPagamentos.getSelectedItemPosition()){
                    case 1:
                        new taskGravarPagamento(getActivity(), handler).execute(String.valueOf(user.getId()), String.valueOf(spinnerMetodoMetodosPagamentos.getSelectedItemPosition()), editTextNomeMetodosPagamentos.getText().toString(), editTextNumeroMetodosPagamentos.getText().toString(),
                                editTextDataMetodosPagamentos.getText().toString(), editTextCVMetodosPagamentos.getText().toString());
                }

            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //asynctask para ir buscar à base de dados o metodo de pagamento do utilizador logado
        new taskMetodoPagamento(getActivity(), pagamentosHandler, listener).execute(String.valueOf(user.getId()));
    }
}
