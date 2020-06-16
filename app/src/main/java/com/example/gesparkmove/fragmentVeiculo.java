package com.example.gesparkmove;

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

public class fragmentVeiculo extends Fragment {
    Handler veiculoHandler = new Handler();
    Veiculo veiculo;
    TextView textViewMatricula, textViewMarca, textViewModelo, textViewCor, textViewAviso;
    Button buttonApagarVeiculo, buttonGravarVeiculo;
    Switch switchAtivo;
    Spinner spinnerPlanoPagamento;
    int ativo, plano;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_veiculo, container, false);
        //inicialização dos elementos
        textViewMatricula = view.findViewById(R.id.textViewMatriculaVeiculoFragment);
        textViewMarca = view.findViewById(R.id.textViewMarcaVeiculoFragment);
        textViewModelo = view.findViewById(R.id.textViewModeloVeiculoFragment);
        textViewCor = view.findViewById(R.id.textViewCorVeiculoFragment);
        textViewAviso = view.findViewById(R.id.textViewAvisoVeiculoFragment);
        switchAtivo = view.findViewById(R.id.switchAtivoVeiculoFragment);
        buttonApagarVeiculo = view.findViewById(R.id.buttonApagarVeiculoVeiculoFragment);
        buttonGravarVeiculo = view.findViewById(R.id.buttonGravarVeiculoFragment);
        spinnerPlanoPagamento = view.findViewById(R.id.spinnerPlanoPagamentoVeiculoFragment);
        //Vai buscar a informação do veiculo selecionado
        Bundle bundle = getActivity().getIntent().getExtras();
        veiculo = bundle.getParcelable("DATAVEICULO");
        //elementos do spinner
        String[] pp = new String[]{"Avença", "Fracção"};
        ArrayAdapter<String> adapterPlano = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, pp);
        spinnerPlanoPagamento.setAdapter(adapterPlano);
        //Se o veiculo estiver estacionado bloqueia os botões, spinner e switch
        if(veiculo.getEstacionado() == 1){
            spinnerPlanoPagamento.setEnabled(false);
            switchAtivo.setEnabled(false);
            buttonApagarVeiculo.setEnabled(false);
            buttonGravarVeiculo.setEnabled(false);
            textViewAviso.setVisibility(view.VISIBLE);
        }
        //mostra a informação do veiculo
        textViewMatricula.setText(veiculo.getMatricula());
        textViewMarca.setText("Marca: " + veiculo.getMarca());
        textViewModelo.setText("Modelo: " + veiculo.getModelo());
        textViewCor.setText("Cor: " + veiculo.getCor());
        //coloca o switch ativo ou não conforme a informação do veiculo.
        if(veiculo.getAtivo() == 1) {
            ativo = 1;
            switchAtivo.setChecked(true);
            spinnerPlanoPagamento.setEnabled(true);
        }
        else{
            ativo = 0;
            switchAtivo.setChecked(false);
            spinnerPlanoPagamento.setEnabled(false);
        }
        //listener para ativar/desativar o spinner conforme a posição do switch
        switchAtivo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ativo = 1;
                    spinnerPlanoPagamento.setEnabled(true);
                }else{
                    ativo = 0;
                    spinnerPlanoPagamento.setEnabled(false);
                }
            }
        });

        buttonGravarVeiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spinnerPlanoPagamento.getSelectedItem().toString().equals("Avença")) plano = 1;
                else plano = 2;
                new taskGravarVeiculo(getActivity(), veiculoHandler).execute(String.valueOf(veiculo.getId()), String.valueOf(ativo), String.valueOf(plano));
            }
        });


        /*switchAtivo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
        });*/
        //listener para detetar a ação do botão "apagar"
        /*buttonApagarVeiculo.setOnClickListener(new View.OnClickListener() {
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
                fragmentConsultar cFragment = new fragmentConsultar();
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.containerFragment, cFragment, "consultar")
                        .commit();
            }
        });*/

        return view;
    }
}