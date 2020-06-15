package com.example.gesparkmove;

import android.os.Parcel;
import android.os.Parcelable;

public class Utilizador implements Parcelable {

    public static final Creator<Utilizador> CREATOR = new Creator<Utilizador>() {
        @Override
        public Utilizador createFromParcel(Parcel in) {
            return new Utilizador(in);
        }

        @Override
        public Utilizador[] newArray(int size) {
            return new Utilizador[size];
        }
    };

    private int id;
    private int nif;
    private String nome;
    private String mail;
    private int carros;
    private String avatar;
    private int ativo;
    private double valor;

    public Utilizador(int id, int nif, String nome, String mail, int carros, String avatar, int ativo, double valor){
        this.id = id;
        this.nif = nif;
        this.nome = nome;
        this.mail = mail;
        this.carros = carros;
        this.avatar = avatar;
        this.ativo = ativo;
        this.valor = valor;
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMail(){
        return mail;
    }

    public void setMail(String mail){
        this.mail = mail;
    }

    public int getCarros(){
        return carros;
    }

    public void setCarros(int carros){
        this.carros = carros;
    }

    public String getAvatar(){
        return avatar;
    }

    public void setAvatar(String avatar){
        this.avatar = avatar;
    }

    public int getAtivo(){
        return ativo;
    }

    public void setAtivo(int ativo){
        this.ativo = ativo;
    }

    public double getValor(){return valor;}

    public void setValor(double valor){this.valor = valor;}

    protected Utilizador(Parcel in) {
        this.id = in.readInt();
        this.nif = in.readInt();
        this.nome = in.readString();
        this.mail = in.readString();
        this.carros = in.readInt();
        this.avatar = in.readString();
        this.ativo = in.readInt();
        this.valor = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.nif);
        dest.writeString(this.nome);
        dest.writeString(this.mail);
        dest.writeInt(this.carros);
        dest.writeString(this.avatar);
        dest.writeInt(this.ativo);
        dest.writeDouble(this.valor);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
