package com.example.gesparkmove;

import java.util.ArrayList;

//interface para receber informação da taskVeiculosEstacionados
public interface onEstacionamentosListener {
    void onEstacionamentosCompleted(ArrayList<Estacionamento> data);
}
