package com.example.gesparkmove;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;

public class fragmentConsultar extends Fragment{
    Utilizador user;
    ArrayList<Veiculo> veiculos = new ArrayList<>();
    ArrayAdapter adapterVeiculos;
    ListView listViewConsultar;
    TextView textViewSemVeiculos;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consultar, container, false);
        Bundle bundle = getActivity().getIntent().getExtras();
        user = bundle.getParcelable("USER");
        veiculos = bundle.getParcelableArrayList("VEICULO");
        listViewConsultar = view.findViewById(R.id.listViewConsultar);
        textViewSemVeiculos = view.findViewById(R.id.textViewSemVeiculos);
        if(veiculos.size() != 0){
            textViewSemVeiculos.setVisibility(view.INVISIBLE);
            listViewConsultar.setVisibility(view.VISIBLE);
            adapterVeiculos = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, veiculos){
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View itemView = super.getView(position, convertView, parent);
                    if(veiculos.get(position).getAtivo() == 0){
                        itemView.setBackgroundColor(Color.RED);
                    }else{
                        itemView.setBackgroundColor(Color.GREEN);
                    }
                    return itemView;
                }
            };
            listViewConsultar.setAdapter(adapterVeiculos);
        }else{
            textViewSemVeiculos.setVisibility(view.VISIBLE);
            listViewConsultar.setVisibility(view.INVISIBLE);
        }


        listViewConsultar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Veiculo data = (Veiculo) parent.getItemAtPosition(position);
                Intent intent = getActivity().getIntent();
                intent.putExtra("DATAVEICULO", data);

                fragmentVeiculo vFragment = new fragmentVeiculo();
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.containerFragment, vFragment, "veiculo")
                        .commit();
                //Log.println(Log.INFO, "TOAST:  ", data.toString());
                //Toast.makeText(getActivity(), data.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
