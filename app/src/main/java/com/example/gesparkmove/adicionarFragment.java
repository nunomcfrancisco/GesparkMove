package com.example.gesparkmove;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class adicionarFragment extends Fragment implements onDataListener {
    EditText editTextAdicionarMatricula, editTextAdicionarCor;
    Handler adicionarHandler = new Handler();
    Utilizador user;
    ArrayList<Marcas> marcasItems = new ArrayList<>();
    ArrayList<Modelos> modelosItems = new ArrayList<>();
    private List<String> marcaModelo = new ArrayList<>();
    int idMarca;
    Spinner spinnerMarcas, spinnerModelos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_adicionar, container, false);
        Bundle bundle = getActivity().getIntent().getExtras();
        user = bundle.getParcelable("USER");
        new taskData(getActivity(), this, adicionarHandler).execute();
        editTextAdicionarMatricula = view.findViewById(R.id.editTextAdicionarMatricula);
        editTextAdicionarCor = view.findViewById(R.id.editTextAdicionarCor);
        editTextAdicionarMatricula.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        editTextAdicionarMatricula.addTextChangedListener(adicionarMatriculaTextWatcher);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button buttonAdicionarAdicionar = view.findViewById(R.id.buttonAdicionarAdicionar);
        buttonAdicionarAdicionar.setOnClickListener(new View.OnClickListener(){
            int idMa, idMo;
            @Override
            public void onClick(View view){

                for(Marcas marca : marcasItems)
                    if(marca.getMarca().equals(spinnerMarcas.getSelectedItem().toString()))
                        idMa = marca.getId();
                for(Modelos modelo : modelosItems)
                    if(modelo.getModelo().equals(spinnerModelos.getSelectedItem().toString()))
                        idMo = modelo.getId();

                Log.println(Log.INFO, "SELECTED MARCA", spinnerMarcas.getSelectedItem().toString());
                Log.println(Log.INFO, "SELECTED MODELO", spinnerModelos.getSelectedItem().toString());
                Log.println(Log.INFO, "MATRICULA: ", editTextAdicionarMatricula.getText().toString());
                Log.println(Log.INFO, "USER ID: ", String.valueOf(user.getId()));
                Log.println(Log.INFO, "MARCA ID: ",String.valueOf(idMa));
                Log.println(Log.INFO, "MODELO ID: ",String.valueOf(idMo));
                Log.println(Log.INFO, "COR: ",editTextAdicionarCor.getText().toString());

                taskAdicionarMatricula tam = new taskAdicionarMatricula(getActivity(), adicionarHandler);
                tam.execute(editTextAdicionarMatricula.getText().toString(),
                        String.valueOf(user.getId()),
                        String.valueOf(idMa),
                        String.valueOf(idMo),
                        editTextAdicionarCor.getText().toString());
            }
        });
    }

    @Override
    public void onMarcasCompleted(ArrayList<ArrayList> data) {
        marcasItems = data.get(0);
        modelosItems = data.get(1);
        spinnerMarcas = this.getView().findViewById(R.id.spinnerAdicionarMarca);
        spinnerModelos = this.getView().findViewById(R.id.spinnerAdicionarModelo);
        ArrayAdapter<Marcas> adapterMarcas = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, marcasItems);
        ArrayAdapter<Modelos> adapterModelos = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, modelosItems);
        spinnerMarcas.setAdapter(adapterMarcas);
        spinnerModelos.setAdapter(adapterModelos);
        spinnerMarcas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String spinnerValue = spinnerMarcas.getSelectedItem().toString();
                marcaModelo.clear();

                for (Marcas marca : marcasItems)
                    if(spinnerValue.equals(marca.getMarca()))
                        idMarca = marca.getId();

                for (Modelos modelo : modelosItems)
                    if(idMarca == modelo.getIdMarca())
                        marcaModelo.add(modelo.getModelo());

                spinnerMarcas.setSelection(position);
                ArrayAdapter adapterModelos = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, marcaModelo);
                spinnerModelos.setAdapter(adapterModelos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private TextWatcher adicionarMatriculaTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.length() == 2) {
                editTextAdicionarMatricula.setText(editTextAdicionarMatricula.getText() + "-");
                editTextAdicionarMatricula.setSelection(editTextAdicionarMatricula.getText().length());
            }
            if(s.length() == 5){
                editTextAdicionarMatricula.setText(editTextAdicionarMatricula.getText() + "-");
                editTextAdicionarMatricula.setSelection(editTextAdicionarMatricula.getText().length());
            }

        }

        @Override
        public void afterTextChanged(Editable s) {}
    };
}
