package com.example.gesparkmove;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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

public class fragmentListCar extends Fragment{
    //declaração de variaveis
    Handler listCarHandler = new Handler();
    User user;
    ArrayList<Car> cars = new ArrayList<>();
    ArrayAdapter adapterCar;
    ListView listViewListCar;
    TextView textViewListCarNoCar;
    //interface para trabalhar a informação recebida da taskListCar
    onListCarListener listener = new onListCarListener() {
        @Override
        public void onListCarCompleted(ArrayList<Car> data) {
            cars = data;
            if(cars.size() != 0){
                textViewListCarNoCar.setVisibility(getView().INVISIBLE);
                listViewListCar.setVisibility(getView().VISIBLE);
                adapterCar = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, cars){
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        View itemView = super.getView(position, convertView, parent);
                        if(cars.get(position).getActive() == 0){
                            itemView.setBackgroundColor(Color.parseColor("#cc0000"));
                        }else{
                            itemView.setBackgroundColor(Color.parseColor("#009933"));
                        }
                        return itemView;
                    }
                };
                listViewListCar.setAdapter(adapterCar);
            }else{
                textViewListCarNoCar.setVisibility(getView().VISIBLE);
                listViewListCar.setVisibility(getView().INVISIBLE);
            }
            listViewListCar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Car data = (Car) parent.getItemAtPosition(position);
                    Intent intent = getActivity().getIntent();
                    intent.putExtra("DATAVEICULO", data);
                    //carrega o fragment veiculo
                    fragmentCar vFragment = new fragmentCar();
                    FragmentManager manager = getFragmentManager();
                    manager.beginTransaction()
                            .replace(R.id.containerFragment, vFragment, "veiculo")
                            .commit();
                }
            });
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listcar, container, false);
        //inicialização dos elementos visuais

        listViewListCar = view.findViewById(R.id.listViewListCarListCar);
        textViewListCarNoCar = view.findViewById(R.id.textViewListCarNoCar);
        Bundle bundle = getActivity().getIntent().getExtras();
        user = bundle.getParcelable("USER");

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //asynctask para ir buscar à base de dados os veiculos do utilizador
        new taskListCar(getActivity(), listener, listCarHandler).execute(String.valueOf(user.getId()));
    }
}
