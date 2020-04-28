package com.example.gesparkmove;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class dashboardFragment extends Fragment {
    TextView carros;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        carros = view.findViewById(R.id.textViewDashboardVeiculos);
        Bundle bundle = getActivity().getIntent().getExtras();
        Utilizador user = bundle.getParcelable("USER");
        carros.setText(String.valueOf(user.getCarros()));

        return view;
    }
}
