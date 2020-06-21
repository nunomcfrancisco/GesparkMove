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
import androidx.fragment.app.FragmentManager;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class fragmentVeiculo extends Fragment {
    //declaração de variaveis
    Handler veiculoHandler = new Handler();
    Utilizador user;
    Veiculo veiculo;
    TextView textViewMatricula, textViewMarca, textViewModelo, textViewCor, textViewAviso;
    Button buttonApagarVeiculo, buttonGravarVeiculo;
    Switch switchAtivo;
    Spinner spinnerPlanoPagamento;
    CircleImageView imageViewVeiculo;
    int ativo, plano;
    onVeiculoListener listener = new onVeiculoListener() {
        @Override
        public void onVeiculoCompleted(Integer plano, Integer historico) {
            switch (plano){
                case 0:
                    spinnerPlanoPagamento.setSelection(1);
                break;
                case 1:
                    spinnerPlanoPagamento.setSelection(2);
                break;
                case 2:
                    spinnerPlanoPagamento.setSelection(3);
                break;
            }
            if(historico == 1)
                buttonApagarVeiculo.setEnabled(false);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_veiculo, container, false);
        //inicialização dos elementos visuais
        textViewMatricula = view.findViewById(R.id.textViewMatriculaVeiculoFragment);
        textViewMarca = view.findViewById(R.id.textViewMarcaVeiculoFragment);
        textViewModelo = view.findViewById(R.id.textViewModeloVeiculoFragment);
        textViewCor = view.findViewById(R.id.textViewCorVeiculoFragment);
        textViewAviso = view.findViewById(R.id.textViewAvisoVeiculoFragment);
        switchAtivo = view.findViewById(R.id.switchAtivoVeiculoFragment);
        buttonApagarVeiculo = view.findViewById(R.id.buttonApagarVeiculoVeiculoFragment);
        buttonGravarVeiculo = view.findViewById(R.id.buttonGravarVeiculoFragment);
        spinnerPlanoPagamento = view.findViewById(R.id.spinnerPlanoPagamentoVeiculoFragment);
        imageViewVeiculo = view.findViewById(R.id.imageViewVeiculoImagem);
        //Vai buscar a informação do veiculo selecionado
        Bundle bundle = getActivity().getIntent().getExtras();
        veiculo = bundle.getParcelable("DATAVEICULO");
        user = bundle.getParcelable("USER");
        //elementos do spinner
        String[] pp = new String[]{"", "Avença", "Fracção"};
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
        if(veiculo.getAvatar() != null){
            Picasso.get().load("https://gespark.pt/" + veiculo.getAvatar()).into(imageViewVeiculo);
        }
        textViewMatricula.setText(veiculo.getMatricula());
        textViewMarca.setText("Marca: " + veiculo.getMarca());
        textViewModelo.setText("Modelo: " + veiculo.getModelo());
        textViewCor.setText("Cor: " + veiculo.getCor());
        //coloca o switch ativo ou não conforme a informação do veiculo.
        if(veiculo.getAtivo() == 1) {
            ativo = 1;
            switchAtivo.setChecked(true);
        }
        else{
            ativo = 0;
            switchAtivo.setChecked(false);
        }
        //listener para ativar/desativar o spinner conforme a posição do switch
        switchAtivo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    ativo = 1;
                else
                    ativo = 0;
            }
        });
        //clickar no botão gravar
        buttonGravarVeiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spinnerPlanoPagamento.getSelectedItem().toString().equals("Avença")) plano = 1;
                else plano = 2;
                FragmentManager manager = getFragmentManager();
                //asynctask para adicionar o veiculo à base de dados
                new taskGravarVeiculo(getActivity(), veiculoHandler, manager).execute(String.valueOf(veiculo.getId()), String.valueOf(ativo), String.valueOf(plano), String.valueOf(user.getId()));
            }
        });
        //clickar no boão apagar
        buttonApagarVeiculo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FragmentManager manager = getFragmentManager();
                //asynctask para apagar o veiculo da base de dados
                new taskApagarVeiculo(getActivity(), veiculoHandler, manager, getActivity()).execute(String.valueOf(veiculo.getId()));
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        new taskVeiculo(getActivity(), veiculoHandler, listener).execute(String.valueOf(veiculo.getId()));
    }
}