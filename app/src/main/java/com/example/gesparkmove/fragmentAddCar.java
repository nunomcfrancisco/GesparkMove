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

public class fragmentAddCar extends Fragment {
    //declaração de variaveis
    EditText editTextAddLicensePlate, editTextAddColor;
    Button buttonAddAdd;
    Handler addHandler = new Handler();
    User user;
    ArrayList<Brand> brandItems = new ArrayList<>();
    ArrayList<Model> modelItems = new ArrayList<>();
    private List<String> brandModel = new ArrayList<>();
    int idBrand, sLengthNow, sLengthBefore;
    Spinner spinnerBrand, spinnerModel;

    //interface para trabalhar a informação devolvida da taskData
    onBrandModelListener listener = new onBrandModelListener() {
        @Override
        public void onBrandModelCompleted(ArrayList<ArrayList> data) {
            brandItems = data.get(0);
            modelItems = data.get(1);
            ArrayAdapter<Brand> adapterMarcas = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, brandItems);
            spinnerBrand.setAdapter(adapterMarcas);
            //listener para colocar no spinner dos modelos apenas os modelos referentes à marca selecionada
            spinnerBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String spinnerValue = spinnerBrand.getSelectedItem().toString();
                    brandModel.clear();
                    for (Brand brand : brandItems)
                        if(spinnerValue.equals(brand.getBrand()))
                            idBrand = brand.getId();
                    for (Model model : modelItems)
                        if(idBrand == model.getIdBrand())
                            brandModel.add(model.getModel());
                    spinnerBrand.setSelection(position);
                    ArrayAdapter adapterModelos = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, brandModel);
                    spinnerModel.setAdapter(adapterModelos);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
            ArrayAdapter<Model> adapterModel = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, modelItems);
            spinnerModel.setAdapter(adapterModel);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        //inicialização dos elementos visuais
        spinnerBrand = view.findViewById(R.id.spinnerAddBrand);
        spinnerModel = view.findViewById(R.id.spinnerAddModel);
        Bundle bundle = Objects.requireNonNull(getActivity()).getIntent().getExtras();
        user = Objects.requireNonNull(bundle).getParcelable("USER");
        editTextAddLicensePlate = view.findViewById(R.id.editTextAddLicensePlate);
        editTextAddColor = view.findViewById(R.id.editTextAddColor);
        editTextAddLicensePlate.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(8)});
        editTextAddLicensePlate.addTextChangedListener(addLicensePlateTextWatcher);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        new taskData(getActivity(), listener, addHandler).execute();
        buttonAddAdd = view.findViewById(R.id.buttonAddAdd);
        //ação do botão Adicionar
        buttonAddAdd.setOnClickListener(new View.OnClickListener(){
            int idBr, idMo;
            @Override
            public void onClick(View view){
                for(Brand brand : brandItems)
                    if(brand.getBrand().equals(spinnerBrand.getSelectedItem().toString()))
                        idBr = brand.getId();
                for(Model model : modelItems)
                    if(model.getModel().equals(spinnerModel.getSelectedItem().toString()))
                        idMo = model.getId();
                FragmentManager manager = getFragmentManager();
                new taskAddLicensePlate(getActivity(), addHandler, getActivity(), manager).execute(editTextAddLicensePlate.getText().toString(),
                        String.valueOf(user.getId()),
                        String.valueOf(idBr),
                        String.valueOf(idMo),
                        editTextAddColor.getText().toString());
                editTextAddLicensePlate.setText("");
                editTextAddColor.setText("");
                spinnerBrand.setSelection(0);
                spinnerModel.setSelection(0);
                sLengthNow = 0;
                sLengthBefore = 0;
            }
        });
    }

    //Textwatcher para acrescentar os traços da matricula
    private TextWatcher addLicensePlateTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            buttonAddAdd.setEnabled(s.length() == 8);
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
