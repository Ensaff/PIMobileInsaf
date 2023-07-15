/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.esprit.entities;

/**
 *
 * @author Ensaf^^
 */
public class Categorie {
   private  int idCategorie ;
   private String nom ;
   
   public Categorie(int id, String nom) {
        this.idCategorie = id;
        this.nom = nom;
    }

    public Categorie(String nom) {
        this.nom = nom;
    }

    public int getIdCategorie() {
        return idCategorie;
    }

    public String getNom() {
        return nom;
    }

    public void setIdCategorie(int id) {
        this.idCategorie = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return "Categorie{" + "id=" + idCategorie + ", nom=" + nom + '}';
    }
}
