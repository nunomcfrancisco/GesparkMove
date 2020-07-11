package com.example.gesparkmove;

import android.os.Parcel;
import android.os.Parcelable;

//classe que representa um utilizador
public class User implements Parcelable {

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    private int id;
    private int nif;
    private String name;
    private String mail;
    private int cars;
    private String avatar;
    private int active;
    private double value;

    public User(int id, int nif, String name, String mail, int cars, String avatar, int active, double value){
        this.id = id;
        this.nif = nif;
        this.name = name;
        this.mail = mail;
        this.cars = cars;
        this.avatar = avatar;
        this.active = active;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNif() {
        return nif;
    }

    public void setNif(int nif) {
        this.nif = nif;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail(){
        return mail;
    }

    public void setMail(String mail){
        this.mail = mail;
    }

    public int getCars(){
        return cars;
    }

    public void setCars(int cars){
        this.cars = cars;
    }

    public String getAvatar(){
        return avatar;
    }

    public void setAvatar(String avatar){
        this.avatar = avatar;
    }

    public int getActive(){
        return active;
    }

    public void setActive(int active){
        this.active = active;
    }

    public double getValue(){return value;}

    public void setValue(double value){this.value = value;}

    protected User(Parcel in) {
        this.id = in.readInt();
        this.nif = in.readInt();
        this.name = in.readString();
        this.mail = in.readString();
        this.cars = in.readInt();
        this.avatar = in.readString();
        this.active = in.readInt();
        this.value = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.nif);
        dest.writeString(this.name);
        dest.writeString(this.mail);
        dest.writeInt(this.cars);
        dest.writeString(this.avatar);
        dest.writeInt(this.active);
        dest.writeDouble(this.value);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
