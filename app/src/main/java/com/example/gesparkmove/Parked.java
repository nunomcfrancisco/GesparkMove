package com.example.gesparkmove;

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

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getPark() {
        return park;
    }

    public void setPark(String park) {
        this.park = park;
    }

    public String getEntrance() {
        return entrance;
    }

    public void setEntrance(String entrance) {
        this.entrance = entrance;
    }

    public String getExit() {
        return exit;
    }

    public void setExit(String exit) {
        this.exit = exit;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
