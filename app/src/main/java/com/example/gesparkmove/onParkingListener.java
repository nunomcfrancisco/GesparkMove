package com.example.gesparkmove;

import java.util.ArrayList;
//interface para receber os dados de uma asynctask
public interface onParkingListener {
    void onParkingCompleted(ArrayList<Parked> data);
}
