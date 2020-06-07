package com.example.gesparkmove;

import android.os.Parcel;
import android.os.Parcelable;

public class Modelos implements Parcelable {

    public static final Creator<Modelos> CREATOR = new Creator<Modelos>() {
        @Override
        public Modelos createFromParcel(Parcel in) {
            return new Modelos(in);
        }

        @Override
        public Modelos[] newArray(int size) {
            return new Modelos[size];
        }
    };

    private int id;
    private int idModelo;
    private String modelo;

    public Modelos(int id, int idModelo, String modelo) {
        this.id = id;
        this.idModelo = idModelo;
        this.modelo = modelo;
    }

    public int getId() {
        return id;
    }

    public int getIdModelo(){
        return idModelo;
    }

    public String getModelo() {
        return modelo;
    }

    protected Modelos(Parcel in){
        this.id = in.readInt();
        this.idModelo = in.readInt();
        this.modelo = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeInt(this.id);
        dest.writeInt(this.idModelo);
        dest.writeString(this.modelo);
    }

    @Override
    public int describeContents(){
        return 0;
    }
}
