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

public class dashboardFragment extends Fragment {
    TextView textViewDashboardFragmentVeiculos;
    CardView cardViewDashboardFragmentVeiculos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);


        textViewDashboardFragmentVeiculos = view.findViewById(R.id.textViewDashboardVeiculos);
        Bundle bundle = getActivity().getIntent().getExtras();
        Utilizador user = bundle.getParcelable("USER");
        textViewDashboardFragmentVeiculos.setText(String.valueOf(user.getCarros()));
        cardViewDashboardFragmentVeiculos = view.findViewById(R.id.fragmentDashboardCardViewVeiculos);
        cardViewDashboardFragmentVeiculos.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
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
