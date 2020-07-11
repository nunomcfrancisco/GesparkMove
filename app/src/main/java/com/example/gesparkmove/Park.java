package com.example.gesparkmove;
//Classe que representa um parque de estacionamento
public class Park {
    private String name;
    private String location;
    private String phone;
    private String email;
    private String latitude;
    private String longitude;

    public Park(String name, String location, String phone, String email, String latitude, String longitude) {
        this.name = name;
        this.location = location;
        this.phone = phone;
        this.email = email;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
