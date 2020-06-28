package com.example.gesparkmove;

import java.util.List;

//interface para receber informação da taskMetodoPagameto
public interface onPaymentListener {
    void onPagamentosCompleted(List<String> data);
}
