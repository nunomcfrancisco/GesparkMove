package com.example.gesparkmove;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class fragmentParked extends Fragment{
    //declaração de variáveis
    User user;
    ListView listViewParked;
    TextView textViewParkedNoParked;
    Handler parkedHandler = new Handler();
    //interface para trabalhar a informação recebida da taskParked
    onParkingListener listener = new onParkingListener() {
        @Override
        public void onParkingCompleted(ArrayList<Parked> data) {
            if(data.size() != 0){
            ParkingListAdapter adapter = new ParkingListAdapter(getActivity(), R.layout.adapter_parked_layout, data);
            listViewParked.setAdapter(adapter);
            }else{
                textViewParkedNoParked.setVisibility(getView().VISIBLE);
                listViewParked.setVisibility(getView().INVISIBLE);
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parked, container, false);
        Bundle bundle = getActivity().getIntent().getExtras();
        user = bundle.getParcelable("USER");
        //associação das varáveis aos elementos visuais
        listViewParked = view.findViewById(R.id.listViewParkedParked);
        textViewParkedNoParked = view.findViewById(R.id.textViewParkedNoParked);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //asynctask para ir buscar à base de dados os veiculos estacionados
        new taskParked(getActivity(), parkedHandler, listener).execute(String.valueOf(user.getId()));
    }
}
