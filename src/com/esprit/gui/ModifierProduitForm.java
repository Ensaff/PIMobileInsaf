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
import com.codename1.ui.ComboBox;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Form;
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
public class ModifierProduitForm extends Form {
    private TextField tfNom;
    private TextField tfPrix;
        private ComboBox <String> tfNomCategorie;
    private Button modifierButton;
    private Button btnRetour;
    private Produit produit;
    private Form previousForm;
    
    String url = Statics.BASE_URL + "/Categorie";
       ConnectionRequest request = new ConnectionRequest();

   public ModifierProduitForm(HomeForm homeForm, Produit produit) {
    super("Modifier", BoxLayout.y());
    this.previousForm = homeForm;
    this.produit = produit;
    initGui();
    addActions();
    
}

    private void initGui() {
        tfNom = new TextField(produit.getNom(), "Nom");
        tfPrix = new TextField(String.valueOf(produit.getPrix()), "Prix");
       tfNomCategorie = new ComboBox<>(String.valueOf(produit.getNomCategorie()));
         ServiceCategorie sc = new ServiceCategorie();
         
       modifierButton = new Button("Modifier");
        btnRetour = new Button("Annuler");
        
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

        Component[] components = {tfNom, tfPrix, tfNomCategorie,  modifierButton, btnRetour};
        Container container = new Container();
        container.setLayout(BoxLayout.y());
        container.addAll(components);

        this.add(container);
    }

    private void addActions() { 
    modifierButton.addActionListener((evt) -> {
    if (tfNom.getText().isEmpty() || tfPrix.getText().isEmpty() || tfNomCategorie.getSelectedItem() == null) {
        Dialog.show("Erreur", "Veuillez remplir tous les champs", "OK", null);
        return;
    }
 ServiceProduit sp = new ServiceProduit();
    String nomProduit = tfNom.getText().trim();
    String prixText = tfPrix.getText();

    if (!isValidPrix(prixText)) {
        Dialog.show("Erreur", "Veuillez entrer un prix valide (entier positif ou un float positif avec 2 décimales)", "OK", null);
        return;
    }

    if (!nomProduit.equals(produit.getNom())) {
        boolean produitExiste = false;

        // Parcourir les produits existants
        List<Produit> produits = sp.afficher();
        for (Produit autreProduit : produits) {
            String nomExistant = autreProduit.getNom().trim();

            // Vérifier si le nom du produit existe déjà
            if (nomExistant.equals(nomProduit)) {
                produitExiste = true;
                break;
            }
        }

        if (produitExiste) {
            Dialog.show("Alerte", "Le nom du produit existe déjà", "OK", null);
            return;
        }
    }

    // Mettre à jour les valeurs du produit en fonction des champs de texte modifiés
    produit.setNom(tfNom.getText());
     produit.setPrix(Float.parseFloat(tfPrix.getText()));

    // Récupérer l'ID de la catégorie sélectionnée
    String nomCategorie = (String) tfNomCategorie.getSelectedItem();
    int idCategorie = (int) tfNomCategorie.getClientProperty(nomCategorie);
    produit.setIdCategorie(idCategorie);

    if (sp.modifier(produit)) {
        Dialog.show("SUCCESS", "Modification réussie", "OK", null);
        HomeForm homeForm = (HomeForm) previousForm;
        homeForm.updateProduits();
        previousForm.showBack();
    } else {
        Dialog.show("ERROR", "Erreur serveur", "OK", null);
    }
});

    
    btnRetour.addActionListener((evt) -> {
        previousForm.showBack();
    });
    }
      private boolean isValidPrix(String value) {
    try {
        float floatValue = Float.parseFloat(value);
        int intValue = (int) floatValue;
        return floatValue >= 0 && (floatValue == intValue || floatValue == Math.round(floatValue * 100) / 100f);
    } catch (NumberFormatException e) {
        return false;
    }
}
}


