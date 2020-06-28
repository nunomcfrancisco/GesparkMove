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
    TextView textViewDashboardCar, textViewDashboardValue;
    CardView cardViewDashboardCar, cardViewDashboardValue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        //carregar a informação do utilizador logado
        Bundle bundle = getActivity().getIntent().getExtras();
        User user = bundle.getParcelable("USER");
        //inicialização dos elementos visuais
        textViewDashboardCar = view.findViewById(R.id.textViewDashboardCar);
        textViewDashboardValue = view.findViewById(R.id.textViewDashboardValue);
        textViewDashboardCar.setText(String.valueOf(user.getCars()));
        textViewDashboardValue.setText(String.valueOf(user.getValue()));
        cardViewDashboardCar = view.findViewById(R.id.cardViewDashboardCar);
        cardViewDashboardValue = view.findViewById(R.id.cardViewDashboardValue);
        //ao clickar nos veiculos abres o janela de consultar veiculos
        cardViewDashboardCar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                fragmentListCar cFragment = new fragmentListCar();
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.containerFragment, cFragment, "consultar")
                        .commit();
            }
        });
        //ao clickar nos gastos abre o histórico dos estacionamentos
        cardViewDashboardValue.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                fragmentParked eFragment = new fragmentParked();
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.containerFragment, eFragment, "estacionamentos")
                        .commit();
            }
        });

        return view;
    }
}
