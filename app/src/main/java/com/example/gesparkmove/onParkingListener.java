package com.example.gesparkmove;

import java.util.ArrayList;
//interface para receber os dados da taskParked
public interface onParkingListener {
    void onParkingCompleted(ArrayList<Parked> data);
}
