package com.example.gesparkmove;

import android.os.Parcel;
import android.os.Parcelable;

public class Marca implements Parcelable{

    public static final Creator<Marca> CREATOR = new Creator<Marca>(){
        @Override
        public Marca createFromParcel(Parcel in){
            return new Marca(in);
        }

        @Override
        public Marca[] newArray(int size) {
            return new Marca[size];
        }
    };

    private int id;
    private String marca;

    public Marca(int id, String marca) {
        this.id = id;
        this.marca = marca;
    }
    public int getId(){
        return id;
    }
    public String getMarca(){
        return marca;
    }

    protected Marca(Parcel in){
        this.id = in.readInt();
        this.marca = in.readString();
    }

    @Override
    public String toString(){
        return marca;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeInt(this.id);
        dest.writeString(this.marca);
    }

    @Override
    public int describeContents(){
        return 0;
    }
}
