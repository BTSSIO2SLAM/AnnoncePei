package com.example.annoncepei.Models;

public class Categorie {

    private int Id;
    private String Libelle;

    public Categorie() {
    }

    public Categorie(int id, String libelle) {
        this.Id = id;
        this.Libelle = libelle;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public String getLibelle() {
        return Libelle;
    }

    public void setLibelle(String libelle) {
        this.Libelle = libelle;
    }

    @Override
    public String toString() {
        return Libelle;
    }
}
