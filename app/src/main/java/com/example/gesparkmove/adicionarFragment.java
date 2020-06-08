package com.example.gesparkmove;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class adicionarFragment extends Fragment{
    private Handler adicionarHandler = new Handler();
    Globals g = new Globals();
    int idMarca;
    List<String> marcaModelo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ArrayList<Marcas> marcasItems = new ArrayList<>();
        final ArrayList<Modelos> modelosItems = new ArrayList<>();
        ArrayList<String> marcas = new ArrayList<>();
        View view = inflater.inflate(R.layout.fragment_adicionar, container, false);
        Bundle bundle = getActivity().getIntent().getExtras();
        Utilizador user = bundle.getParcelable("USER");
        taskData td = new taskData(getActivity(), marcasItems, modelosItems, adicionarHandler);
        td.execute();

        final Spinner spinnerMarcas = view.findViewById(R.id.spinnerAdicionarMarca);
        //final Spinner spinnerModelos = view.findViewById(R.id.spinnerAdicionarModelo);
        Log.println(Log.INFO, "MARCAS SIZE1::::", String.valueOf(marcasItems.size()));
        ArrayAdapter<Marcas> adapterMarcas = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, marcasItems);
        spinnerMarcas.setAdapter(adapterMarcas);
        Log.println(Log.INFO, "MARCAS SIZE2::::", String.valueOf(marcasItems.size()));
        /*spinnerMarcas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String spinnerValue = spinnerMarcas.getSelectedItem().toString();

                for (int i = 0; marcasItems.size() > i; i++){
                    if (spinnerValue == marcasItems.get(i).getMarca()) {
                        idMarca = marcasItems.get(i).getId();
                    }
                }
                for(int i = 0; modelosItems.size() > i; i++){
                    if(idMarca == modelosItems.get(i).getIdMarca()){
                        marcaModelo.add(modelosItems.get(i).getModelo());
                    }
                }
                spinnerMarcas.setSelection(position);
                ArrayAdapter adapterModelos = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, marcaModelo);
                //spinnerModelos.setAdapter(adapterModelos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

    }




}
