package com.example.gesparkmove;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;

public class veiculoFragment extends Fragment {
    Handler veiculoHandler = new Handler();
    Veiculo veiculo;
    TextView textViewMatricula, textViewMarca, textViewModelo, textViewCor;
    Button buttonApagarVeiculo;
    Switch switchAtivo;
    Spinner spinnerPlanoPagamento;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_veiculo, container, false);
        Bundle bundle = getActivity().getIntent().getExtras();
        veiculo = bundle.getParcelable("DATAVEICULO");
        textViewMatricula = view.findViewById(R.id.textViewMatriculaVeiculoFragment);
        textViewMarca = view.findViewById(R.id.textViewMarcaVeiculoFragment);
        textViewModelo = view.findViewById(R.id.textViewModeloVeiculoFragment);
        textViewCor = view.findViewById(R.id.textViewCorVeiculoFragment);
        switchAtivo = view.findViewById(R.id.switchAtivoVeiculoFragment);
        buttonApagarVeiculo = view.findViewById(R.id.buttonApagarVeiculoVeiculoFragment);
        spinnerPlanoPagamento = view.findViewById(R.id.spinnerPlanoPagamentoVeiculoFragment);
        String[] pp = new String[]{"Avença", "Fracção"};
        ArrayAdapter<String> adapterPlano = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, pp);
        spinnerPlanoPagamento.setAdapter(adapterPlano);
        if(veiculo.getEstacionado() == 1){
            spinnerPlanoPagamento.setEnabled(false);
            switchAtivo.setEnabled(false);
            buttonApagarVeiculo.setEnabled(false);
        }

        textViewMatricula.setText(veiculo.getMatricula());
        textViewMarca.setText("Marca: " + veiculo.getMarca());
        textViewModelo.setText("Modelo: " + veiculo.getModelo());
        textViewCor.setText("Cor: " + veiculo.getCor());

        if(veiculo.getAtivo() == 1)
            switchAtivo.setChecked(true);
        else
            switchAtivo.setChecked(false);

        switchAtivo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent intent = getActivity().getIntent();
                Bundle bundle = intent.getExtras();
                ArrayList<Veiculo> veiculos = bundle.getParcelableArrayList("VEICULO");
                if(isChecked) {
                    veiculo.setAtivo(1);
                    new taskVeiculoAtivo(getActivity(), veiculoHandler).execute(String.valueOf(1), String.valueOf(veiculo.getId()));
                    for(Veiculo v : veiculos){
                        if(v.getId() == veiculo.getId())
                            v.setAtivo(1);
                    }
                }
                else {
                    veiculo.setAtivo(0);
                    new taskVeiculoAtivo(getActivity(), veiculoHandler).execute(String.valueOf(0), String.valueOf(veiculo.getId()));
                    for(Veiculo v : veiculos){
                        if(v.getId() == veiculo.getId())
                            v.setAtivo(0);
                    }
                }
                intent.putExtra("VEICULO", veiculos);
            }
        });

        buttonApagarVeiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getActivity().getIntent();
                Bundle bundle = getActivity().getIntent().getExtras();
                Veiculo veiculo = bundle.getParcelable("DATAVEICULO");
                ArrayList<Veiculo> vArray = bundle.getParcelableArrayList("VEICULO");
                for(Veiculo vs : vArray){
                    if(vs.getId() == veiculo.getId()){
                        vArray.remove(vs);
                    }
                }
                intent.putExtra("VEICULO", vArray);
                new taskApagarVeiculo(getActivity(), veiculoHandler).execute(String.valueOf(veiculo.getId()));
                consultarFragment cFragment = new consultarFragment();
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.containerFragment, cFragment, "consultar")
                        .commit();
            }
        });

        return view;
    }
}