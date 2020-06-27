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
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class fragmentAdicionar extends Fragment {
    //declaração de variaveis
    EditText editTextAdicionarMatricula, editTextAdicionarCor;
    Button buttonAdicionarAdicionar;
    Handler adicionarHandler = new Handler();
    Utilizador user;
    ArrayList<Marca> marcasItems = new ArrayList<>();
    ArrayList<Modelo> modelosItems = new ArrayList<>();
    private List<String> marcaModelo = new ArrayList<>();
    int idMarca, sLengthNow, sLengthBefore;
    Spinner spinnerMarcas, spinnerModelos;

    //interface para trabalhar a informação devolvida da taskData
    onMarcasModelosListener listener = new onMarcasModelosListener() {
        @Override
        public void onMarcasModelosCompleted(ArrayList<ArrayList> data) {
            marcasItems = data.get(0);
            modelosItems = data.get(1);
            ArrayAdapter<Marca> adapterMarcas = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, marcasItems);
            spinnerMarcas.setAdapter(adapterMarcas);
            //listener para colocar no spinner dos modelos apenas os modelos referentes à marca selecionada
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
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_adicionar, container, false);
        //inicialização dos elementos visuais
        spinnerMarcas = view.findViewById(R.id.spinnerAdicionarMarca);
        spinnerModelos = view.findViewById(R.id.spinnerAdicionarModelo);
        Bundle bundle = Objects.requireNonNull(getActivity()).getIntent().getExtras();
        user = Objects.requireNonNull(bundle).getParcelable("USER");
        editTextAdicionarMatricula = view.findViewById(R.id.editTextAdicionarMatricula);
        editTextAdicionarCor = view.findViewById(R.id.editTextAdicionarCor);
        editTextAdicionarMatricula.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(8)});
        editTextAdicionarMatricula.addTextChangedListener(adicionarMatriculaTextWatcher);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        new taskData(getActivity(), listener, adicionarHandler).execute();
        buttonAdicionarAdicionar = view.findViewById(R.id.buttonAdicionarAdicionar);
        //ação do botão Adicionar
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
                FragmentManager manager = getFragmentManager();
                new taskAdicionarMatricula(getActivity(), adicionarHandler, getActivity(), manager).execute(editTextAdicionarMatricula.getText().toString(),
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

    //Textwatcher para acrescentar os traços da matricula
    private TextWatcher adicionarMatriculaTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            buttonAdicionarAdicionar.setEnabled(s.length() == 8);
        }

        @Override
        public void afterTextChanged(Editable text) {
            sLengthNow = text.length();
            if(sLengthNow > sLengthBefore){
                if(text.length() == 2 || text.length() == 5){
                    text.append('-');
                    sLengthBefore = sLengthNow - 1;
                }
            }else{
                sLengthBefore = sLengthBefore -1;
            }
        }
    };
}
