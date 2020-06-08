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
    private int idMarca;
    private String modelo;

    public Modelos(int id, String modelo, int idMarca) {
        this.id = id;
        this.modelo = modelo;
        this.idMarca = idMarca;
    }

    public int getId() {
        return id;
    }

    public int getIdMarca(){
        return idMarca;
    }

    public String getModelo() {
        return modelo;
    }

    protected Modelos(Parcel in){
        this.id = in.readInt();
        this.modelo = in.readString();
        this.idMarca = in.readInt();
    }

    @Override
    public String toString(){
        return modelo;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeInt(this.id);
        dest.writeString(this.modelo);
        dest.writeInt(this.idMarca);
    }

    @Override
    public int describeContents(){
        return 0;
    }
}
