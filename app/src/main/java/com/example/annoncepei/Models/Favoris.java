package com.example.annoncepei.Models;

import com.google.gson.annotations.SerializedName;

public class Favoris {

    @SerializedName("Id")
    private int Id;
    @SerializedName("UtilisateurID")
    private int UtilisateurID;
    @SerializedName("AnnonceID")
    private int AnnonceID;

    public Favoris() {
    }

    public Favoris(int id, int utilisateurID, int annonceID) {
        Id = id;
        UtilisateurID = utilisateurID;
        AnnonceID = annonceID;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getUtilisateurID() {
        return UtilisateurID;
    }

    public void setUtilisateurID(int utilisateurID) {
        UtilisateurID = utilisateurID;
    }

    public int getAnnonceID() {
        return AnnonceID;
    }

    public void setAnnonceID(int annonceID) {
        AnnonceID = annonceID;
    }
}
