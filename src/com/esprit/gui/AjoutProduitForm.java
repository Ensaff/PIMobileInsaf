/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.esprit.gui;

import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.ui.Button;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Form;
import com.codename1.ui.ComboBox;
import com.codename1.ui.TextField;
import com.codename1.ui.layouts.BoxLayout;
import com.esprit.entities.Produit;
import com.esprit.services.ServiceCategorie;
import com.esprit.services.ServiceProduit;
import com.esprit.utils.Statics;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Ensaf^^
 */
public class AjoutProduitForm extends Form {
      private TextField tfNom;
    private TextField tfPrix;
    private ComboBox <String> tfNomCategorie;
    
    private Button btnAjout;
    private Button btnAfficher;
    
private Form previousForm;

 String url = Statics.BASE_URL + "/Categorie";
       ConnectionRequest request = new ConnectionRequest();

public AjoutProduitForm(Form f) {
        super("Ajout", BoxLayout.y());
        previousForm = f;
        OnGui();
        addActions();
    }

    private void OnGui() {
        tfNom = new TextField(null, "Nom");
        tfPrix = new TextField(null, "Prix");
        tfNomCategorie = new ComboBox<>();
         ServiceCategorie sc = new ServiceCategorie();
         
        btnAjout = new Button("Ajouter");
        btnAfficher = new Button("Annuler");
        
     request.setUrl(url);
    request.setHttpMethod("GET");
    request.addResponseListener((NetworkEvent evt) -> {
        byte[] responseData = request.getResponseData();
        if (responseData != null) {
            String response = new String(responseData);
            try {
                  InputStreamReader jsonText = new InputStreamReader(new ByteArrayInputStream(request.getResponseData()), "UTF-8");
                Map<String, Object> result = new JSONParser().parseJSON(jsonText);
                List<Map<String, Object>> list = (List<Map<String, Object>>) 
               result.get("root");

 if (list != null) {

      for (Map<String, Object> Categorie : list) {
    String nomCategorie = (String) Categorie.get("nom");
   
    int id = ((Double) Categorie.get("idCategorie")).intValue();
    tfNomCategorie.addItem(nomCategorie);
    tfNomCategorie.putClientProperty(nomCategorie, id); 
} 
 }else {
    System.out.println("");
}
     
                System.out.println(list);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    });

    NetworkManager.getInstance().addToQueueAndWait(request);

        Component[] components = {tfNom, tfPrix, tfNomCategorie, btnAjout, btnAfficher};
        Container container = new Container();
        container.setLayout(BoxLayout.y());
        container.addAll(components);

        this.add(container);
    }
  
   

    private void addActions() {
        btnAjout.addActionListener((evt) -> {
            if (tfNom.getText().isEmpty() || tfPrix.getText().isEmpty() || tfNomCategorie.getSelectedItem() == null) {
                Dialog.show("Alerte", "Veillez remplir tous les champs", "OK", null);
            } else {
                
             ServiceProduit sp = new ServiceProduit();
             
             String nomCategorie = (String) tfNomCategorie.getSelectedItem();
             int idCategorie = (int) tfNomCategorie.getClientProperty(nomCategorie);
              String prixText = tfPrix.getText();
                 if (isValidPrix(prixText)) {
            String nomProduit = tfNom.getText().trim();
            boolean produitExiste = false;
            
            // Parcourir les produits existants
            List<Produit> produits = sp.afficher();
            for (Produit produit : produits) {
                String nomExistant = produit.getNom().trim();
                
                // Vérifier si le nom du produit existe déjà
                if (nomExistant.equals(nomProduit)) {
                    produitExiste = true;
                    break;
                }
            }
            
            if (produitExiste) {
                Dialog.show("Alerte", "Le nom du produit existe déjà", "OK", null);
            } else {
                if (sp.ajouter(new Produit(tfNom.getText(), Float.parseFloat(tfPrix.getText()),idCategorie))) {
                    Dialog.show("SUCCESS", "Produit ajoutée !", "OK", null);
                     HomeForm homeForm = (HomeForm) previousForm;
                     homeForm.updateProduits();
                     previousForm.showBack();
                   
                } else {
                    Dialog.show("ERROR", "Erreur serveur", "OK", null);
                }
            }
} else {
                Dialog.show("Alerte", "Veuillez saisir un prix valide : le prix doit être un nombre positif avec au maximum deux chiffres après la virgule", "OK", null);
            }
            }
        });
        
       
this.getToolbar().addCommandToLeftBar("Return", null, (evt) -> {
            previousForm.showBack();
        });
 btnAfficher.addActionListener((evt) -> {
            previousForm.showBack();
        });
       
    }
    private boolean isValidPrix(String value) {
    try {
        float floatValue = Float.parseFloat(value);
        int intValue = (int) floatValue;
        return floatValue > 0 && (floatValue == intValue || floatValue == Math.round(floatValue * 100) / 100f);
    } catch (NumberFormatException e) {
        return false;
    }
}
    
}
