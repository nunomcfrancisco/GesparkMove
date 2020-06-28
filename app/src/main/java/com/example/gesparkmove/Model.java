package com.example.gesparkmove;

import android.os.Parcel;
import android.os.Parcelable;

public class Model implements Parcelable {

    public static final Creator<Model> CREATOR = new Creator<Model>() {
        @Override
        public Model createFromParcel(Parcel in) {
            return new Model(in);
        }

        @Override
        public Model[] newArray(int size) {
            return new Model[size];
        }
    };

    private int id;
    private int idBrand;
    private String model;

    public Model(int id, String model, int idBrand) {
        this.id = id;
        this.model = model;
        this.idBrand = idBrand;
    }

    public int getId() {
        return id;
    }

    public int getIdBrand(){
        return idBrand;
    }

    public String getModel() {
        return model;
    }

    protected Model(Parcel in){
        this.id = in.readInt();
        this.model = in.readString();
        this.idBrand = in.readInt();
    }

    @Override
    public String toString(){
        return model;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeInt(this.id);
        dest.writeString(this.model);
        dest.writeInt(this.idBrand);
    }

    @Override
    public int describeContents(){
        return 0;
    }
}
