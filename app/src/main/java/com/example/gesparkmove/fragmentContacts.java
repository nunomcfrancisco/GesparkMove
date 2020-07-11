package com.example.gesparkmove;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class fragmentContacts extends Fragment{
    Handler contactsHandler = new Handler();
    ListView listViewContacts;


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
        listViewContacts = view.findViewById(R.id.listViewContacts);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        new taskContacts(getActivity(), contactsHandler, listener).execute();
    }
}