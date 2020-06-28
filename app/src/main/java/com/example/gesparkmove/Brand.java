package com.example.gesparkmove;

import android.os.Parcel;
import android.os.Parcelable;

public class Brand implements Parcelable{

    public static final Creator<Brand> CREATOR = new Creator<Brand>(){
        @Override
        public Brand createFromParcel(Parcel in){
            return new Brand(in);
        }

        @Override
        public Brand[] newArray(int size) {
            return new Brand[size];
        }
    };

    private int id;
    private String brand;

    public Brand(int id, String brand) {
        this.id = id;
        this.brand = brand;
    }
    public int getId(){
        return id;
    }
    public String getBrand(){
        return brand;
    }

    protected Brand(Parcel in){
        this.id = in.readInt();
        this.brand = in.readString();
    }

    @Override
    public String toString(){
        return brand;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeInt(this.id);
        dest.writeString(this.brand);
    }

    @Override
    public int describeContents(){
        return 0;
    }
}
