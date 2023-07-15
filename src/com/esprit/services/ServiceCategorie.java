/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.esprit.services;

import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkManager;
import com.esprit.entities.Categorie;
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
public class ServiceCategorie implements IService<Categorie>{
     private boolean responseResult;
    private List<Categorie> categories;
    
    private final String URI = Statics.BASE_URL + "/categorie/";

    public ServiceCategorie() {
        categories = new ArrayList();
    }
    
    public boolean ajouter(Categorie c) {
        ConnectionRequest request = new ConnectionRequest();
        
        request.setUrl(URI);
        request.setHttpMethod("POST");

        request.addArgument("nom", c.getNom());
       

        request.addResponseListener((evt) -> {
            responseResult = request.getResponseCode() == 201; // Code HTTP 201 OK
        });
        NetworkManager.getInstance().addToQueueAndWait(request);

        return responseResult;
    }
    
       public boolean modifier(Categorie c) {
        ConnectionRequest request = new ConnectionRequest();
        
        request.setUrl(URI + c.getIdCategorie());
        request.setHttpMethod("PUT");

        request.addArgument("nom", c.getNom());
        
        request.addResponseListener((evt) -> {
            responseResult = request.getResponseCode() == 200; // Code HTTP 200 OK
        });
        NetworkManager.getInstance().addToQueueAndWait(request);

        return responseResult;
    }
       
          public boolean supprimer(Categorie c) {
        ConnectionRequest request = new ConnectionRequest();
        
        request.setUrl(URI + c.getIdCategorie());
        request.setHttpMethod("DELETE");

        request.addResponseListener((evt) -> {
            responseResult = request.getResponseCode() == 200; // Code HTTP 200 OK
        });
        NetworkManager.getInstance().addToQueueAndWait(request);

        return responseResult;
    }
  
 public List<Categorie> afficher() {
        ConnectionRequest request = new ConnectionRequest();
        
        request.setUrl(URI);
        request.setHttpMethod("GET");

        request.addResponseListener((evt) -> {
            try {
                InputStreamReader jsonText = new InputStreamReader(new ByteArrayInputStream(request.getResponseData()), "UTF-8");
                Map<String, Object> result = new JSONParser().parseJSON(jsonText);
                List<Map<String, Object>> list = (List<Map<String, Object>>) result.get("root");

                for (Map<String, Object> obj : list) {
                    int idCategorie = (int) Float.parseFloat(obj.get("idCategorie").toString());
                    String nom = obj.get("nom").toString();
                    categories.add(new Categorie(idCategorie, nom));
                }

            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(request);

        return categories;
    }
}
