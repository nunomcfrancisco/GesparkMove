package com.example.gesparkmove;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class fragmentEstacionamentos extends Fragment{
    //declaração de variaveis
    Utilizador user;
    ListView listViewEstacionamentos;
    TextView textViewFragmentEstacionamentosSemHistorico;
    Handler handlerEstacionados = new Handler();
    //interface para trabalhar a informação recebida da taskVeiculosEstacionados
    onEstacionamentosListener listener = new onEstacionamentosListener() {
        @Override
        public void onEstacionamentosCompleted(ArrayList<Estacionamento> data) {
            if(data.size() != 0){
            EstacionamentoListAdapter adapter = new EstacionamentoListAdapter(getActivity(), R.layout.adapter_estacionados_layout, data);
            listViewEstacionamentos.setAdapter(adapter);
            }else{
                textViewFragmentEstacionamentosSemHistorico.setVisibility(getView().VISIBLE);
                listViewEstacionamentos.setVisibility(getView().INVISIBLE);
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_estacionamentos, container, false);
        Bundle bundle = getActivity().getIntent().getExtras();
        user = bundle.getParcelable("USER");
        //inicialização dos elementos visuais
        listViewEstacionamentos = view.findViewById(R.id.listViewEstacionamentos);
        textViewFragmentEstacionamentosSemHistorico = view.findViewById(R.id.textViewFragmentEstacionamentosSemHistorico);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //asynctask para ir buscar à base de dados os veiculos estacionados
        new taskVeiculosEstacionados(getActivity(), handlerEstacionados, listener).execute(String.valueOf(user.getId()));
    }
}
