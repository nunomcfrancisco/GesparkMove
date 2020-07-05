package com.example.gesparkmove;

import java.util.List;
//interface para receber os dados de uma asynctask
public interface onPaymentListener {
    void onPagamentosCompleted(List<String> data);
}
