package com.example.gesparkmove;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class fragmentContacts extends Fragment{
    //declaração de variáveis
    Handler contactsHandler = new Handler();
    ListView listViewContacts;

    //interface para trabalhar a informação devolvida da taskContacts
    onContactsListener listener = new onContactsListener() {
        @Override
        public void onContactsCompleted(ArrayList<Park> data) {
            if(data.size() != 0){
            ContactsListAdapter adapter = new ContactsListAdapter(getActivity(), R.layout.adapter_contacts_layout, data);
            listViewContacts.setAdapter(adapter);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        //associação das variáveis aos elementos visuais
        listViewContacts = view.findViewById(R.id.listViewContacts);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //task para obter a informação dos parques de estacionamento
        new taskContacts(getActivity(), contactsHandler, listener).execute();
    }
}