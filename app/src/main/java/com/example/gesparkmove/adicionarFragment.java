package com.example.gesparkmove;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class adicionarFragment extends Fragment{
    private Handler adicionarHandler = new Handler();
    Globals g = new Globals();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ArrayList<String> marcasItems = new ArrayList<>();
        ArrayList<String> modelosItems = new ArrayList<>();
        View view = inflater.inflate(R.layout.fragment_adicionar, container, false);
        Bundle bundle = getActivity().getIntent().getExtras();
        Utilizador user = bundle.getParcelable("USER");
        ArrayList<Marcas> marcas = bundle.getParcelable("MARCAS");
        ArrayList<Modelos> modelos = bundle.getParcelable("MODELOS");
        //Log.println(Log.INFO, "NOME FRAGMENT", user.getNome());
        Spinner spinnerMarcas = view.findViewById(R.id.spinnerAdicionarMarca);

        Log.println(Log.INFO, "MARCAS - ", String.valueOf(marcas.size()));
        //for (Marcas marca : marcas) marcasItems.add(marca.getMarca());
        //for (Modelos modelo : modelos) modelosItems.add(modelo.getModelo());

        //String[] items = new String[]{String.valueOf(user.getId()), user.getNome(), user.getMail()};
        ArrayAdapter adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, marcasItems);
        spinnerMarcas.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }


}
