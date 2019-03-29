package com.example.annoncepei.Models;

import com.google.gson.annotations.SerializedName;

public class Annonce {

    @SerializedName("Id")
    private int Id;
    @SerializedName("Titre")
    private String Titre;
    @SerializedName("Details")
    private String Details;
    @SerializedName("Prix")
    private int Prix;
    @SerializedName("UrlPhoto")
    private String UrlPhoto;
    @SerializedName("CategorieID")
    private int CategorieID;
    @SerializedName("UtilisateurID")
    private int UtilisateurID;
    private Utilisateur utilisateur;
    private Categorie categorie;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public String getTitre() {
        return Titre;
    }

    public void setTitre(String titre) {
        this.Titre = titre;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        this.Details = details;
    }

    public float getPrix() {
        return Prix;
    }

    public void setPrix(int prix) {
        this.Prix = prix;
    }

    public String getImage() {
        return UrlPhoto;
    }

    public void setImage(String image) {
        this.UrlPhoto = image;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public int getCategorieID() {
        return CategorieID;
    }

    public void setCategorieID(int categorieID) {
        CategorieID = categorieID;
    }

    public int getUtilisateurID() {
        return UtilisateurID;
    }

    public void setUtilisateurID(int utilisateurID) {
        UtilisateurID = utilisateurID;
    }
}
