package com.example.gesparkmove;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

//visualização costumizada de um elemento numa list view

public class EstacionamentoListAdapter extends ArrayAdapter<Estacionamento> {

    private Context ctx;
    int mResource;


    public EstacionamentoListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Estacionamento> objects) {
        super(context, resource, objects);
        ctx = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String matricula = getItem(position).getMatricula();
        String parque = getItem(position).getParque();
        String entrada = getItem(position).getEntrada();
        String saida = getItem(position).getSaida();
        double valor = getItem(position).getValor();

        Estacionamento estacionamento = new Estacionamento(matricula, parque, entrada, saida, valor);

        LayoutInflater inflater = LayoutInflater.from(ctx);
        convertView = inflater.inflate(mResource, parent, false);

        TextView textViewMatricula = convertView.findViewById(R.id.textViewMatriculaEstacionamento);
        TextView textViewParque = convertView.findViewById(R.id.textViewParqueEstacionamento);
        TextView textViewEntrada = convertView.findViewById(R.id.textViewEntradaEstacionamento);
        TextView textViewSaida = convertView.findViewById(R.id.textViewSaidaEstacionamento);
        TextView textViewValor = convertView.findViewById(R.id.textViewValorEstacionamento);

        textViewMatricula.setText(matricula);
        textViewParque.setText(parque);
        textViewEntrada.setText(entrada);
        textViewSaida.setText(saida);
        textViewValor.setText(valor + "€");

        return convertView;
    }
}