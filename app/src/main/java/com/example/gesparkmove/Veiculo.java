package com.example.gesparkmove;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Veiculo implements Parcelable{

    public static final Creator<Veiculo> CREATOR = new Creator<Veiculo>(){
        @Override
        public Veiculo createFromParcel(Parcel in){
            return new Veiculo(in);
        }

        @Override
        public Veiculo[] newArray(int size){
            return new Veiculo[size];
        }
    };

    int id;
    String matricula;
    String marca;
    String modelo;
    String cor;
    int estacionado;
    int ativo;
    String avatar;

    public Veiculo(int id, String matricula, String marca, String modelo, String cor, int estacionado, int ativo, String avatar) {
        this.id = id;
        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
        this.cor = cor;
        this.estacionado = estacionado;
        this.ativo = ativo;
        this.avatar = avatar;
    }

    protected Veiculo(Parcel in){
        this.id = in.readInt();
        this.matricula = in.readString();
        this.marca = in.readString();
        this.modelo = in.readString();
        this.cor = in.readString();
        this.estacionado = in.readInt();
        this.ativo = in.readInt();
        this.avatar = in.readString();
    }

    public int getId() {
        return id;
    }

    public String getMatricula() {
        return matricula;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public String getCor() {
        return cor;
    }

    public int getEstacionado() {
        return estacionado;
    }

    public int getAtivo() {
        return ativo;
    }

    public void setAtivo(int valor){
        this.ativo = valor;
    }

    public String getAvatar() {return avatar;}

    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeInt(this.id);
        dest.writeString(this.matricula);
        dest.writeString(this.marca);
        dest.writeString(this.modelo);
        dest.writeString(this.cor);
        dest.writeInt(this.estacionado);
        dest.writeInt(this.ativo);
        dest.writeString(this.avatar);
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @NonNull
    @Override
    public String toString() {
        return matricula + " - " + marca + " " + modelo;
    }
}
