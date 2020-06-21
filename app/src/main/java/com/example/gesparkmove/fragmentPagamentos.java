package com.example.gesparkmove;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;
import java.util.Objects;

public class fragmentPagamentos extends Fragment {
    //declaração das variaveis
    TextView editTextNomeMetodosPagamentos, editTextNumeroMetodosPagamentos, editTextDataMetodosPagamentos;
    ImageView imageViewLogoMetodosPagamentos;
    Handler pagamentosHandler = new Handler();
    Utilizador user;
    //interface para trabalhar a informação recebida da taskMetodoPagamento
    onPagamentosListener listener = new onPagamentosListener() {
        @Override
        public void onPagamentosCompleted(List<String> data) {
            switch (data.size()){
                case(1):
                    imageViewLogoMetodosPagamentos.setImageDrawable(getResources().getDrawable(R.drawable.mbway));
                    editTextNomeMetodosPagamentos.setText(data.get(0));
                break;
                case(2):
                    imageViewLogoMetodosPagamentos.setImageDrawable(getResources().getDrawable(R.drawable.db));
                    editTextNomeMetodosPagamentos.setText(data.get(0));
                    editTextNumeroMetodosPagamentos.setText(data.get(1));
                break;
                case(3):
                    imageViewLogoMetodosPagamentos.setImageDrawable(getResources().getDrawable(R.drawable.visa));
                    editTextNomeMetodosPagamentos.setText(data.get(0));
                    editTextNumeroMetodosPagamentos.setText(data.get(1));
                    editTextDataMetodosPagamentos.setText(data.get(2));
                break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pagamentos, container, false);
        //inicialização dos elementos visuais
        editTextNomeMetodosPagamentos = view.findViewById(R.id.textViewNomeMetodosPagamentos);
        editTextNumeroMetodosPagamentos = view.findViewById(R.id.textViewNumeroMetodosPagamentos);
        editTextDataMetodosPagamentos = view.findViewById(R.id.textViewDataMetodosPagamentos);
        imageViewLogoMetodosPagamentos = view.findViewById(R.id.imageViewLogoMetodosPagamentos);
        Bundle bundle = Objects.requireNonNull(getActivity()).getIntent().getExtras();
        user = Objects.requireNonNull(bundle).getParcelable("USER");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //asynctask para ir buscar à base de dados o metodo de pagamento do utilizador logado
        new taskMetodoPagamento(getActivity(), pagamentosHandler, listener).execute(String.valueOf(user.getId()));
    }
}
