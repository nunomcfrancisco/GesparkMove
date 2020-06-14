package com.example.gesparkmove;

public class Estacionamento {
    private String matricula;
    private String parque;
    private String entrada;
    private String saida;
    private double valor;

    public Estacionamento(String matricula, String parque, String entrada, String saida, double valor) {
        this.matricula = matricula;
        this.parque = parque;
        this.entrada = entrada;
        this.saida = saida;
        this.valor = valor;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getParque() {
        return parque;
    }

    public void setParque(String parque) {
        this.parque = parque;
    }

    public String getEntrada() {
        return entrada;
    }

    public void setEntrada(String entrada) {
        this.entrada = entrada;
    }

    public String getSaida() {
        return saida;
    }

    public void setSaida(String saida) {
        this.saida = saida;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
