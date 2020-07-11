package com.example.gesparkmove;

import java.util.List;
//interface para receber os dados da taskPayment
public interface onPaymentListener {
    void onPagamentosCompleted(List<String> data);
}
