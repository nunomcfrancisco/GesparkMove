package com.example.gesparkmove;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class fragmentEstacionamentos extends Fragment {
    ListView listViewEstacionamentos;
    Handler handlerEstacionados = new Handler();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_estacionamentos, container, false);
        listViewEstacionamentos = view.findViewById(R.id.listViewEstacionamentos);
        ArrayList<Estacionamento> estacionamentos = new ArrayList<>();

        estacionamentos.add(new Estacionamento("XX-00-AA", "Marques de Pombal - Lisboa", "2020-05-20 00:05:30", "2020-05-20 00:06:30", 3.50));
        estacionamentos.add(new Estacionamento("FF-33-GG", "Marques de Pombal - Lisboa", "2020-06-20 00:05:30", "2020-06-20 00:06:30", 4.50));

        EstacionamentoListAdapter adapter = new EstacionamentoListAdapter(getActivity(), R.layout.adapter_estacionados_layout, estacionamentos);
        listViewEstacionamentos.setAdapter(adapter);

        new taskVeiculosEstacionados(getActivity(), handlerEstacionados).execute(String.valueOf(38));

        return view;
    }
}
