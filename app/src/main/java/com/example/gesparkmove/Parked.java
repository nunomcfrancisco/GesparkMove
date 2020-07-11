package com.example.gesparkmove;
//classe que representa um evento de estacionamento
public class Parked {
    private String licensePlate;
    private String park;
    private String entrance;
    private String exit;
    private double value;

    public Parked(String licensePlate, String park, String entrance, String exit, double value) {
        this.licensePlate = licensePlate;
        this.park = park;
        this.entrance = entrance;
        this.exit = exit;
        this.value = value;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getPark() {
        return park;
    }

    public String getEntrance() {
        return entrance;
    }

    public String getExit() {
        return exit;
    }

    public double getValue() {
        return value;
    }
}
