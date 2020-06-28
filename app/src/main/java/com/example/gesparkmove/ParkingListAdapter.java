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

public class ParkingListAdapter extends ArrayAdapter<Parked> {

    private Context ctx;
    int mResource;


    public ParkingListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Parked> objects) {
        super(context, resource, objects);
        ctx = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String licensePlate = getItem(position).getLicensePlate();
        String park = getItem(position).getPark();
        String entrance = getItem(position).getEntrance();
        String exit = getItem(position).getExit();
        double value = getItem(position).getValue();

        Parked parked = new Parked(licensePlate, park, entrance, exit, value);

        LayoutInflater inflater = LayoutInflater.from(ctx);
        convertView = inflater.inflate(mResource, parent, false);

        TextView textViewLicensePlate = convertView.findViewById(R.id.textViewParkedLicensePlate);
        TextView textViewPark = convertView.findViewById(R.id.textViewParkedPark);
        TextView textViewEntrance = convertView.findViewById(R.id.textViewParkedEntrance);
        TextView textViewExit = convertView.findViewById(R.id.textViewParkedExit);
        TextView textViewValue = convertView.findViewById(R.id.textViewParkedValue);

        textViewLicensePlate.setText(licensePlate);
        textViewPark.setText(park);
        textViewEntrance.setText(entrance);
        textViewExit.setText(exit);
        textViewValue.setText(value + "€");

        return convertView;
    }
}