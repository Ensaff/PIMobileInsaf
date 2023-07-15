/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.esprit.services;

import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkManager;
import com.esprit.entities.Produit;
import com.esprit.utils.Statics;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Ensaf^^
 */
public class ServiceProduit implements IService<Produit> {
  private boolean responseResult;
    private List<Produit> produits;
    
    private final String URI = Statics.BASE_URL + "/Produit/";

    public ServiceProduit() {
        produits = new ArrayList();
    }   
    
    public boolean ajouter(Produit p) {
        ConnectionRequest request = new ConnectionRequest();
        
        request.setUrl(URI);
        request.setHttpMethod("POST");

        request.addArgument("nom", p.getNom());
        request.addArgument("prix", Float.toString(p.getPrix()));
        request.addArgument("idCategorie", Integer.toString(p.getIdCategorie()));

        request.addResponseListener((evt) -> {
            responseResult = request.getResponseCode() == 201; // Code HTTP 201 OK
        });
        NetworkManager.getInstance().addToQueueAndWait(request);

        return responseResult;
    }
    
        public boolean modifier(Produit p) {
        ConnectionRequest request = new ConnectionRequest();
        
        request.setUrl(URI + p.getIdProduit());
        request.setHttpMethod("PUT");

        request.addArgument("nom",  p.getNom());
        request.addArgument("prix",Float.toString(p.getPrix()));
        request.addArgument("idCategorie", Integer.toString(p.getIdCategorie()));
        
        request.addResponseListener((evt) -> {
            responseResult = request.getResponseCode() == 200; // Code HTTP 200 OK
        });
        NetworkManager.getInstance().addToQueueAndWait(request);

        return responseResult;
    }
        
    public boolean supprimer(Produit p) {
        ConnectionRequest request = new ConnectionRequest();
        
        request.setUrl(URI + p.getIdProduit());
        request.setHttpMethod("DELETE");

        request.addResponseListener((evt) -> {
            responseResult = request.getResponseCode() == 200; // Code HTTP 200 OK
        });
        NetworkManager.getInstance().addToQueueAndWait(request);

        return responseResult;
    }
    
    public List<Produit> afficher() {
      
        ConnectionRequest request = new ConnectionRequest();
        
        request.setUrl(URI);
        request.setHttpMethod("GET");

        request.addResponseListener((evt) -> {
            try {
                InputStreamReader jsonText = new InputStreamReader(new ByteArrayInputStream(request.getResponseData()), "UTF-8");
                Map<String, Object> result = new JSONParser().parseJSON(jsonText);
                List<Map<String, Object>> list = (List<Map<String, Object>>) result.get("root");

                for (Map<String, Object> obj : list) {
                   int idProduit =(int) Float.parseFloat(obj.get("idProduit").toString());
                    String nom = obj.get("np").toString();
                    float prix =Float.parseFloat(obj.get("prix").toString());
                    int idCategorie =(int) Float.parseFloat((obj.get("idC").toString()));
                    String nomCategorie=obj.get("categorie").toString();
                    produits.add(new Produit(idProduit, nom, prix,idCategorie,nomCategorie));
                }

            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(request);

        return produits;
    }
    
     public List<Produit> filterProducts(List<Produit> produits, String searchTerm) {
    List<Produit> filteredList = new ArrayList<>();

   for (Produit produit : produits) {
        String nomProduit = produit.getNom().toLowerCase();
        String nomCategorie = produit.getNomCategorie().toLowerCase();
        searchTerm = searchTerm.toLowerCase();

        if (nomProduit.contains(searchTerm) || nomCategorie.contains(searchTerm)) {
            filteredList.add(produit);
        }
    }

    return filteredList;
}

  }

