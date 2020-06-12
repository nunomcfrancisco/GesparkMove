package com.example.gesparkmove;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
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
import java.util.Objects;

public class adicionarFragment extends Fragment {
    EditText editTextAdicionarMatricula, editTextAdicionarCor;
    Button buttonAdicionarAdicionar;
    Handler adicionarHandler = new Handler();
    Utilizador user;
    ArrayList<Marca> marcasItems = new ArrayList<>();
    ArrayList<Modelo> modelosItems = new ArrayList<>();
    private List<String> marcaModelo = new ArrayList<>();
    int idMarca, sLengthNow, sLengthBefore;
    Spinner spinnerMarcas, spinnerModelos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_adicionar, container, false);
        spinnerMarcas = view.findViewById(R.id.spinnerAdicionarMarca);
        spinnerModelos = view.findViewById(R.id.spinnerAdicionarModelo);
        Bundle bundle = Objects.requireNonNull(getActivity()).getIntent().getExtras();
        user = Objects.requireNonNull(bundle).getParcelable("USER");
        marcasItems = bundle.getParcelableArrayList("MARCA");
        modelosItems = bundle.getParcelableArrayList("MODELO");
        ArrayAdapter<Marca> adapterMarcas = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, marcasItems);
        spinnerMarcas.setAdapter(adapterMarcas);
        spinnerMarcas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String spinnerValue = spinnerMarcas.getSelectedItem().toString();
                marcaModelo.clear();
                for (Marca marca : marcasItems)
                    if(spinnerValue.equals(marca.getMarca()))
                        idMarca = marca.getId();
                for (Modelo modelo : modelosItems)
                    if(idMarca == modelo.getIdMarca())
                        marcaModelo.add(modelo.getModelo());
                spinnerMarcas.setSelection(position);
                ArrayAdapter adapterModelos = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, marcaModelo);
                spinnerModelos.setAdapter(adapterModelos);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        ArrayAdapter<Modelo> adapterModelo = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, modelosItems);

        spinnerModelos.setAdapter(adapterModelo);
        editTextAdicionarMatricula = view.findViewById(R.id.editTextAdicionarMatricula);
        editTextAdicionarCor = view.findViewById(R.id.editTextAdicionarCor);
        editTextAdicionarMatricula.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        editTextAdicionarMatricula.addTextChangedListener(adicionarMatriculaTextWatcher);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buttonAdicionarAdicionar = view.findViewById(R.id.buttonAdicionarAdicionar);
        buttonAdicionarAdicionar.setOnClickListener(new View.OnClickListener(){
            int idMa, idMo;
            @Override
            public void onClick(View view){
                for(Marca marca : marcasItems)
                    if(marca.getMarca().equals(spinnerMarcas.getSelectedItem().toString()))
                        idMa = marca.getId();
                for(Modelo modelo : modelosItems)
                    if(modelo.getModelo().equals(spinnerModelos.getSelectedItem().toString()))
                        idMo = modelo.getId();
                taskAdicionarMatricula tam = new taskAdicionarMatricula(getActivity(), adicionarHandler, getActivity());
                tam.execute(editTextAdicionarMatricula.getText().toString(),
                        String.valueOf(user.getId()),
                        String.valueOf(idMa),
                        String.valueOf(idMo),
                        editTextAdicionarCor.getText().toString());
                editTextAdicionarMatricula.setText("");
                editTextAdicionarCor.setText("");
                spinnerMarcas.setSelection(0);
                spinnerModelos.setSelection(0);
                sLengthNow = 0;
                sLengthBefore = 0;
            }
        });
    }

    private TextWatcher adicionarMatriculaTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            sLengthNow = editTextAdicionarMatricula.getText().length();
            if(sLengthNow > sLengthBefore){
                if(s.length() == 2 || s.length() == 5){
                    editTextAdicionarMatricula.setText(s + "-");
                    editTextAdicionarMatricula.setSelection(editTextAdicionarMatricula.getText().length());
                    sLengthBefore = sLengthNow - 1;
                }
            }else{
                sLengthBefore = sLengthBefore - 1;
            }
            buttonAdicionarAdicionar.setEnabled(s.length() == 8);
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };
}
