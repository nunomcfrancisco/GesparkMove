package com.example.gesparkmove;

import java.util.ArrayList;
//interface para receber os dados da taskContacts
public interface onContactsListener {
    void onContactsCompleted(ArrayList<Park> data);
}
