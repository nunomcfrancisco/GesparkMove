package com.example.gesparkmove;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class fragmentUser extends Fragment {
    TextView textViewUserName, textViewUserAddress, textViewUserPostalCode, textViewUserContacts, textViewUserFiscalCode;
    User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        Bundle bundle = getActivity().getIntent().getExtras();
        user = bundle.getParcelable("USER");
        textViewUserName = view.findViewById(R.id.textViewUserName);
        textViewUserName.setText(user.getName());
        textViewUserAddress = view.findViewById(R.id.textViewUserAddress);
        textViewUserPostalCode = view.findViewById(R.id.textViewUserPostalCode);
        textViewUserContacts = view.findViewById(R.id.textViewUserContact);
        textViewUserFiscalCode = view.findViewById(R.id.textViewUserFiscalCode);
        textViewUserFiscalCode.setText(String.valueOf(user.getNif()));


        return view;
    }
}