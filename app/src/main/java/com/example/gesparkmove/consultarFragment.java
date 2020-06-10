package com.example.gesparkmove;

import android.content.Intent;
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

public class consultarFragment extends Fragment implements onConsultarListener{
    Handler consultarHandler = new Handler();
    Utilizador user;
    ArrayList<Veiculo> veiculos = new ArrayList<>();
    ListView listViewConsultar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consultar, container, false);
        Bundle bundle = getActivity().getIntent().getExtras();

        user = bundle.getParcelable("USER");
        veiculos = bundle.getParcelableArrayList("VEICULO");
        listViewConsultar = view.findViewById(R.id.listViewConsultar);
        ArrayAdapter adapterVeiculos = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, veiculos);
        listViewConsultar.setAdapter(adapterVeiculos);
        /*taskConsultar tc = new taskConsultar(getActivity(), this, consultarHandler);
        tc.execute(String.valueOf(user.getId()));*/
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onConsultarCompleted(ArrayList<Veiculo> data) {
        veiculos = data;
        ArrayAdapter adapterVeiculos = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, veiculos);
        listViewConsultar.setAdapter(adapterVeiculos);
    }
}
