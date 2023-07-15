/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.esprit.gui;

import com.codename1.db.Database;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.Layout;
import com.esprit.services.ServiceProduit;

/**
 *
 * @author Ensaf^^
 */
public class BaseForm extends Form {
    protected Database db;

    public BaseForm(String title, Layout l) {
        super(title, l);
        addActions();
      ServiceProduit sp = new ServiceProduit();

    }

    private void addActions() {
        
        getToolbar().addMaterialCommandToSideMenu("Affichage", FontImage.MATERIAL_HOME, e -> {
            new HomeForm().show();
        });
        getToolbar().addMaterialCommandToSideMenu("Ajouter Produit", FontImage.MATERIAL_ADD, e -> {
            new AjoutProduitForm(this).show();
        });
        
      getToolbar().addMaterialCommandToSideMenu("Chercher Produit",FontImage.MATERIAL_SEARCH, e -> {
            new RechercheProduitForm(this).show();
        });
    
      
      
    }

}
    
