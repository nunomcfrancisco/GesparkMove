package com.example.gesparkmove;

import java.util.ArrayList;

//interface para receber informação da taskVeiculosEstacionados
public interface onParkingListener {
    void onParkingCompleted(ArrayList<Parked> data);
}
