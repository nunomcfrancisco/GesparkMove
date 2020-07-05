package com.example.gesparkmove;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Car implements Parcelable{

    public static final Creator<Car> CREATOR = new Creator<Car>(){
        @Override
        public Car createFromParcel(Parcel in){
            return new Car(in);
        }

        @Override
        public Car[] newArray(int size){
            return new Car[size];
        }
    };

    int id;
    String licensePlate;
    String brand;
    String model;
    String color;
    int parked;
    int active;
    String avatar;

    public Car(int id, String licensePlate, String brand, String model, String color, int parked, int active, String avatar) {
        this.id = id;
        this.licensePlate = licensePlate;
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.parked = parked;
        this.active = active;
        this.avatar = avatar;
    }

    protected Car(Parcel in){
        this.id = in.readInt();
        this.licensePlate = in.readString();
        this.brand = in.readString();
        this.model = in.readString();
        this.color = in.readString();
        this.parked = in.readInt();
        this.active = in.readInt();
        this.avatar = in.readString();
    }

    public int getId() {
        return id;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getColor() {
        return color;
    }

    public int getParked() {
        return parked;
    }

    public int getActive() {
        return active;
    }

    public String getAvatar() {return avatar;}

    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeInt(this.id);
        dest.writeString(this.licensePlate);
        dest.writeString(this.brand);
        dest.writeString(this.model);
        dest.writeString(this.color);
        dest.writeInt(this.parked);
        dest.writeInt(this.active);
        dest.writeString(this.avatar);
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @NonNull
    @Override
    public String toString() {
        return licensePlate + " - " + brand + " " + model;
    }
}
