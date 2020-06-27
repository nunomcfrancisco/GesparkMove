package com.example.gesparkmove;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class fragmentPagamentos extends Fragment {
    //declaração das variaveis
    TextView textViewNomeMetodosPagamentos, textViewNumeroMetodosPagamentos, textViewDataMetodosPagamentos;
    EditText editTextNomeMetodosPagamentos, editTextNumeroMetodosPagamentos, editTextCVMetodosPagamentos;
    Spinner spinnerDataMesMetodosPagamentos, spinnerDataAnoMetodosPagamentos, spinnerMetodoMetodosPagamentos;
    LinearLayout linearLayoutDataMetodosPagamentos;
    ImageView imageViewLogoMetodosPagamentos;
    Button buttonGravarMetodosPagamentos;
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
        editTextNomeMetodosPagamentos.addTextChangedListener(pagamentosTextWatcher);
        editTextNumeroMetodosPagamentos = view.findViewById(R.id.editTextNumeroMetodosPagamentos);
        editTextNumeroMetodosPagamentos.addTextChangedListener(pagamentosTextWatcher);
        editTextCVMetodosPagamentos = view.findViewById(R.id.editTextCVMetodosPagamentos);
        editTextCVMetodosPagamentos.addTextChangedListener(pagamentosTextWatcher);
        spinnerDataMesMetodosPagamentos = view.findViewById(R.id.spinnerDataMesMetodosPagamentos);
        spinnerDataAnoMetodosPagamentos = view.findViewById(R.id.spinnerDataAnoMetodosPagamentos);
        buttonGravarMetodosPagamentos = view.findViewById(R.id.buttonGravarMetodosPagamentos);
        spinnerMetodoMetodosPagamentos = view.findViewById(R.id.spinnerMetodoMetodosPagamentos);
        linearLayoutDataMetodosPagamentos = view.findViewById(R.id.linearLayoutDataMetodosPagamentos);
        String[] pp = new String[]{"", "Cartão de Crédito", "MBWay", "Débito Direto"};
        String[] meses = new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        int ano = Calendar.getInstance().get(Calendar.YEAR);
        String[] anos = new String[]{String.valueOf(ano), String.valueOf(ano + 1), String.valueOf(ano + 2), String.valueOf(ano + 3), String.valueOf(ano + 4), String.valueOf(ano + 5), String.valueOf(ano + 6)};
        ArrayAdapter<String> adapterMetodo = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, pp);
        ArrayAdapter<String> adapterMes = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, meses);
        ArrayAdapter<String> adapterAno = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, anos);
        spinnerMetodoMetodosPagamentos.setAdapter(adapterMetodo);
        spinnerDataMesMetodosPagamentos.setAdapter(adapterMes);
        spinnerDataAnoMetodosPagamentos.setAdapter(adapterAno);
        Bundle bundle = Objects.requireNonNull(getActivity()).getIntent().getExtras();
        user = Objects.requireNonNull(bundle).getParcelable("USER");

        spinnerMetodoMetodosPagamentos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        editTextNomeMetodosPagamentos.setVisibility(View.GONE);
                        editTextNumeroMetodosPagamentos.setVisibility(View.GONE);
                        editTextCVMetodosPagamentos.setVisibility(View.GONE);
                        buttonGravarMetodosPagamentos.setVisibility(View.GONE);
                        linearLayoutDataMetodosPagamentos.setVisibility(View.GONE);
                        break;
                    case 1:
                        editTextNomeMetodosPagamentos.setVisibility(View.VISIBLE);
                        editTextNomeMetodosPagamentos.setText("");
                        editTextNomeMetodosPagamentos.setHint("Nome");
                        editTextNumeroMetodosPagamentos.setVisibility(View.VISIBLE);
                        editTextNumeroMetodosPagamentos.setText("");
                        editTextNumeroMetodosPagamentos.setHint("Número");
                        editTextCVMetodosPagamentos.setVisibility(View.VISIBLE);
                        editTextCVMetodosPagamentos.setText("");
                        editTextCVMetodosPagamentos.setHint("CVV2/CVC2");
                        linearLayoutDataMetodosPagamentos.setVisibility(View.VISIBLE);
                        buttonGravarMetodosPagamentos.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        editTextNomeMetodosPagamentos.setVisibility(View.GONE);
                        editTextNumeroMetodosPagamentos.setVisibility(View.VISIBLE);
                        editTextNumeroMetodosPagamentos.setText("");
                        editTextNumeroMetodosPagamentos.setHint("Telemovel");
                        editTextCVMetodosPagamentos.setVisibility(View.GONE);
                        linearLayoutDataMetodosPagamentos.setVisibility(View.GONE);
                        buttonGravarMetodosPagamentos.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        editTextNomeMetodosPagamentos.setVisibility(View.VISIBLE);
                        editTextNomeMetodosPagamentos.setText("");
                        editTextNomeMetodosPagamentos.setHint("Nome");
                        editTextNumeroMetodosPagamentos.setVisibility(View.VISIBLE);
                        editTextNumeroMetodosPagamentos.setText("");
                        editTextNumeroMetodosPagamentos.setHint("IBAN");
                        editTextCVMetodosPagamentos.setVisibility(View.GONE);
                        linearLayoutDataMetodosPagamentos.setVisibility(View.GONE);
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
                FragmentManager manager = getFragmentManager();
                switch (spinnerMetodoMetodosPagamentos.getSelectedItemPosition()){
                    case 1:
                        String data = spinnerDataMesMetodosPagamentos.getSelectedItem().toString() + "/" + spinnerDataAnoMetodosPagamentos.getSelectedItem().toString().substring(2);
                        new taskGravarPagamento(getActivity(), handler, manager).execute(String.valueOf(user.getId()), String.valueOf(spinnerMetodoMetodosPagamentos.getSelectedItemPosition()), editTextNomeMetodosPagamentos.getText().toString(), editTextNumeroMetodosPagamentos.getText().toString(),
                                data, editTextCVMetodosPagamentos.getText().toString());
                    break;
                    case 2:
                        new taskGravarPagamento(getActivity(), handler, manager).execute(String.valueOf(user.getId()), String.valueOf(spinnerMetodoMetodosPagamentos.getSelectedItemPosition()), editTextNumeroMetodosPagamentos.getText().toString());
                    break;
                    case 3:
                        new taskGravarPagamento(getActivity(), handler, manager).execute(String.valueOf(user.getId()), String.valueOf(spinnerMetodoMetodosPagamentos.getSelectedItemPosition()), editTextNomeMetodosPagamentos.getText().toString(), editTextNumeroMetodosPagamentos.getText().toString());
                    break;
                }

            }
        });

        return view;
    }

    private TextWatcher pagamentosTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            switch (spinnerMetodoMetodosPagamentos.getSelectedItemPosition()){
                case 1:
                    String nomeCC = editTextNomeMetodosPagamentos.getText().toString().trim();
                    String numeroCC = editTextNumeroMetodosPagamentos.getText().toString().trim();
                    String cv = editTextCVMetodosPagamentos.getText().toString().trim();
                    buttonGravarMetodosPagamentos.setEnabled(!nomeCC.isEmpty() && !numeroCC.isEmpty() && !cv.isEmpty());
                    break;
                case 2:
                    String numeroMB = editTextNumeroMetodosPagamentos.getText().toString().trim();
                    buttonGravarMetodosPagamentos.setEnabled(!numeroMB.isEmpty());
                    break;
                case 3:
                    String nomeDD = editTextNomeMetodosPagamentos.getText().toString().trim();
                    String numeroDD = editTextNumeroMetodosPagamentos.getText().toString().trim();
                    buttonGravarMetodosPagamentos.setEnabled(!nomeDD.isEmpty() && !numeroDD.isEmpty());
                    break;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //asynctask para ir buscar à base de dados o metodo de pagamento do utilizador logado
        new taskMetodoPagamento(getActivity(), pagamentosHandler, listener).execute(String.valueOf(user.getId()));
    }
}
