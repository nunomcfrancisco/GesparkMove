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

public class ContactsListAdapter extends ArrayAdapter<Park>{
    private Context ctx;
    int mResource;

    public ContactsListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Park> objects){
        super(context, resource, objects);
        ctx = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = getItem(position).getName();
        String location = getItem(position).getLocation();
        String phone = getItem(position).getPhone();
        String email = getItem(position).getEmail();
        String latitude = getItem(position).getLatitude();
        String longitude = getItem(position).getLongitude();

        LayoutInflater inflater = LayoutInflater.from(ctx);
        convertView = inflater.inflate(mResource, parent, false);

        TextView textViewContactsName = convertView.findViewById(R.id.textViewContactsName);
        TextView textViewContactsLocation = convertView.findViewById(R.id.textViewContactsLocation);
        TextView textViewContactsPhone = convertView.findViewById(R.id.textViewContactsPhone);
        TextView textViewContactsEmail = convertView.findViewById(R.id.textViewContactsEmail);
        TextView textViewContactsLatitude = convertView.findViewById(R.id.textViewContactsLatitude);
        TextView textViewContactsLongitude = convertView.findViewById(R.id.textViewContactsLongitude);

        textViewContactsName.setText(name);
        textViewContactsLocation.setText(location);
        textViewContactsPhone.setText(phone);
        textViewContactsEmail.setText(email);
        textViewContactsLatitude.setText(latitude);
        textViewContactsLongitude.setText(longitude);

        return convertView;
    }
}
