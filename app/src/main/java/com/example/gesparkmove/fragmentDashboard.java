package com.example.gesparkmove;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class fragmentDashboard extends Fragment {
    //declaração de variaveis
    TextView textViewFragmentDashboardVeiculos, textViewFragmentDashboardEstacionamentos;
    CardView cardViewFragmentDashboardVeiculos, cardViewFragmentDashboardEstacionamentos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        //carregar a informação do utilizador logado
        Bundle bundle = getActivity().getIntent().getExtras();
        Utilizador user = bundle.getParcelable("USER");
        //inicialização dos elementos visuais
        textViewFragmentDashboardVeiculos = view.findViewById(R.id.textViewFragmentDashboardVeiculos);
        textViewFragmentDashboardEstacionamentos = view.findViewById(R.id.textViewFragmentDashboardEstacionamentos);
        textViewFragmentDashboardVeiculos.setText(String.valueOf(user.getCarros()));
        textViewFragmentDashboardEstacionamentos.setText(String.valueOf(user.getValor()));
        cardViewFragmentDashboardVeiculos = view.findViewById(R.id.cardViewFragmemtDashboardVeiculos);
        cardViewFragmentDashboardEstacionamentos = view.findViewById(R.id.cardViewFragmentDashboardEstacionamentos);
        //ao clickar nos veiculos abres o janela de consultar veiculos
        cardViewFragmentDashboardVeiculos.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                fragmentConsultar cFragment = new fragmentConsultar();
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.containerFragment, cFragment, "consultar")
                        .commit();
            }
        });
        //ao clickar nos gastos abre o histórico dos estacionamentos
        cardViewFragmentDashboardEstacionamentos.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                fragmentEstacionamentos eFragment = new fragmentEstacionamentos();
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.containerFragment, eFragment, "estacionamentos")
                        .commit();
            }
        });

        return view;
    }
}
