package com.example.gesparkmove;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.lang.reflect.Array;

public class adicionarFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_adicionar, container, false);

        Utilizador user = this.getArguments().getParcelable("USER");

        Log.println(Log.INFO, "NOME FRAGMENT", user.getNome());

        /*Spinner dropdown =view.findViewById(R.id.spinner);
        String[] items = new String[]{String.valueOf(user.getId()), user.getNome(), user.getMail()};
        ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);*/
        return view;
    }
}
