package com.example.gesparkmove;

import android.os.Parcel;
import android.os.Parcelable;

public class Marcas implements Parcelable{

    public static final Creator<Marcas> CREATOR = new Creator<Marcas>(){
        @Override
        public Marcas createFromParcel(Parcel in){return new Marcas(in);}

        @Override
        public Marcas[] newArray(int size) {return new Marcas[size];}
    };

    private int id;
    private String marca;

    public Marcas(int id, String marca) {
        this.id = id;
        this.marca = marca;
    }
    public int getId() {
        return id;
    }
    public String getMarca() {
        return marca;
    }

    protected Marcas(Parcel in){
        this.id = in.readInt();
        this.marca = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeInt(this.id);
        dest.writeString(this.marca);
    }

    @Override
    public int describeContents(){return 0;}
}
