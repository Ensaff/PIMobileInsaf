/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.esprit.gui;

import com.codename1.charts.util.ColorUtil;
import com.codename1.components.SpanLabel;
import com.codename1.ui.Container;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.Border;
import com.esprit.entities.Produit;
import com.esprit.services.ServiceProduit;
import java.util.List;

/**
 *
 * @author Ensaf^^
 */
public class RechercheProduitForm extends Form {
    private Form previousForm;
    private TextField txtSearch;
    private Label lblResults;
    private Label nom ; 
    private  Label prix ;
    private  Label nomCategorie ;
    ServiceProduit sp = new ServiceProduit();
     List<Produit> produits = sp.afficher();
      Container resultContainer;
     
     public RechercheProduitForm(Form f) {
        super("Recherche", BoxLayout.y());
        previousForm = f;
       resultContainer = new Container(BoxLayout.y());
        OnGui();
        addActions();
    }
     private void OnGui() {
    txtSearch = new TextField();
    lblResults = new Label();
    
    this.addAll(new Label("Entrez un terme de recherche :"), txtSearch, lblResults);
    
    Container container = new Container(BoxLayout.y());
    
    for (Produit produit : produits) {
        String nom = String.valueOf(produit.getNom());
        float prix = produit.getPrix();
        String nomCategorie = String.valueOf(produit.getNomCategorie());
        String labelText = "Nom: " + produit.getNom() + ", Prix: " + produit.getPrix() + ", Catégorie: " + produit.getNomCategorie();
        SpanLabel spanLabel = new SpanLabel(labelText);
        container.add(spanLabel);
    }
    
    this.add(resultContainer);;
    }

 
    private void addActions() {
        txtSearch.addDataChangedListener((int type, int index) -> {
            String searchTerm = txtSearch.getText().trim();
            filterProducts(searchTerm);
        });

        this.getToolbar().addCommandToLeftBar("Return", null, (evt) -> {
            previousForm.showBack();
        });
    }
private void filterProducts(String searchTerm) {
    resultContainer.removeAll();
    if (searchTerm.isEmpty()) {
        lblResults.setText("");

        for (Produit produit : produits) {
            String labelText = "Nom: " + produit.getNom() + ", Prix: " + produit.getPrix() + ", Catégorie: " + produit.getNomCategorie();
            SpanLabel spanLabel = new SpanLabel(labelText);
            resultContainer.add(spanLabel);
        }
    } else {
        List<Produit> filteredProducts = sp.filterProducts(produits, searchTerm);
        displayResults(filteredProducts);
    }
    // Clear the result container if the search term is empty
    if (searchTerm.isEmpty()) {
        resultContainer.removeAll();
    }
    revalidate();
}
   private void displayResults(List<Produit> produits) {
      resultContainer.removeAll();
    
    if (produits.isEmpty()) {
        lblResults.setText("Aucun résultat trouvé");
    } else {
        for (Produit produit : produits) {
            Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
            
            Label nameLabel = new Label("Nom: " + produit.getNom());
            Label priceLabel = new Label("Prix: " + produit.getPrix());
            Label categoryLabel = new Label("Catégorie: " + produit.getNomCategorie());
            
            container.add(nameLabel);
            container.add(priceLabel);
            container.add(categoryLabel);
            container.getAllStyles().setBorder(Border.createLineBorder(1, ColorUtil.GRAY));
            resultContainer.add(container);
        }
    }
    resultContainer.revalidate();
}
  
}
