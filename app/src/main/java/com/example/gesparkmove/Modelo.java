package com.example.gesparkmove;

import android.os.Parcel;
import android.os.Parcelable;

public class Modelo implements Parcelable {

    public static final Creator<Modelo> CREATOR = new Creator<Modelo>() {
        @Override
        public Modelo createFromParcel(Parcel in) {
            return new Modelo(in);
        }

        @Override
        public Modelo[] newArray(int size) {
            return new Modelo[size];
        }
    };

    private int id;
    private int idMarca;
    private String modelo;

    public Modelo(int id, String modelo, int idMarca) {
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

    protected Modelo(Parcel in){
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
