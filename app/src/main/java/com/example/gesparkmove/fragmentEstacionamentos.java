package com.example.gesparkmove;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class fragmentEstacionamentos extends Fragment{
    ListView listViewEstacionamentos;
    Handler handlerEstacionados = new Handler();
    onEstacionamentosListener listener = new onEstacionamentosListener() {
        @Override
        public void onEstacionamentosCompleted(ArrayList<Estacionamento> data) {
            EstacionamentoListAdapter adapter = new EstacionamentoListAdapter(getActivity(), R.layout.adapter_estacionados_layout, data);
            listViewEstacionamentos.setAdapter(adapter);
        }
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_estacionamentos, container, false);
        listViewEstacionamentos = view.findViewById(R.id.listViewEstacionamentos);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        new taskVeiculosEstacionados(getActivity(), handlerEstacionados, listener).execute(String.valueOf(38));
    }
}
