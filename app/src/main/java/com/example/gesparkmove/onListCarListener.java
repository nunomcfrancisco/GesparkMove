package com.example.gesparkmove;

import java.util.ArrayList;
//interface para receber os dados de uma asynctask
public interface onListCarListener {
    void onListCarCompleted(ArrayList<Car> data);
}
