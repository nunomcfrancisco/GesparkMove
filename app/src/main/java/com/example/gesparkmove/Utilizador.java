package com.example.gesparkmove;

public class Utilizador {
    private int id;
    private int nif;
    private String nome;
    private String morada;
    private String cp;
    private String mail;

    public Utilizador(int id, int nif, String nome, String morada, String cp, String mail) {
        this.id = id;
        this.nif = nif;
        this.nome = nome;
        this.morada = morada;
        this.cp = cp;
        this.mail = mail;
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

    public String getMorada() {
        return morada;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
