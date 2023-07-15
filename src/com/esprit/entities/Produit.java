/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.esprit.entities;

/**
 *
 * @author Ensaf^^
 */
public class Produit {
private int idProduit ;
private String nom ;
private float prix;
private int idCategorie;
private String nomCategorie;

public Produit() {
    }

    public String getNomCategorie() {
        return nomCategorie;
    }

    public void setNomCategorie(String nomCategorie) {
        this.nomCategorie = nomCategorie;
    }

    public Produit(int idProduit, String nom, float prix, int idCategorie, String nomCategorie) {
        this.idProduit = idProduit;
        this.nom = nom;
        this.prix = prix;
        this.idCategorie = idCategorie;
        this.nomCategorie = nomCategorie;
    }


    public Produit(int id, String nom, float prix, int idCategorie) {
        this.idProduit = id;
        this.nom = nom;
        this.prix = prix;
        this.idCategorie = idCategorie;
    }

    public Produit(String nom, float prix, int idCategorie) {
        this.nom = nom;
        this.prix = prix;
        this.idCategorie = idCategorie;
    }

       public int getIdProduit() {
        return idProduit;
    }

    public String getNom() {
        return nom;
    }

    public float getPrix() {
        return prix;
    }

    public int getIdCategorie() {
        return idCategorie;
    }

    public void setIdProduit(int id) {
        this.idProduit = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public void setIdCategorie(int idCategorie) {
        this.idCategorie = idCategorie;
    }

    @Override
    public String toString() {
        return "Produit{" + "idProduit=" + idProduit + ", nom=" + nom + ", prix=" + prix + ", idCategorie=" + idCategorie + ", nomCategorie=" + nomCategorie + '}';
    }

   
}
