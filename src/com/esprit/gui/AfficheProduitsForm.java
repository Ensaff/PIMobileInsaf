/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.esprit.gui;


import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Container;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.plaf.Border;
import com.esprit.entities.Produit;
import com.esprit.services.ServiceProduit;
import com.codename1.ui.Button;
import com.codename1.ui.FontImage;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BoxLayout;
import java.io.IOException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.codename1.io.FileSystemStorage;
import com.codename1.ui.Command;
import com.codename1.ui.Component;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.geom.Dimension;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import java.util.List;
/**
 *
 * @author Ensaf^^
 */
public class AfficheProduitsForm extends Form {
Button supprimerButton;
    Button modifierButton;
    Button telechargerButton;

    ServiceProduit sp = new ServiceProduit();

    public AfficheProduitsForm(BaseForm baseForm) {
        super("Liste des produits");
        createGUI();
       

        telechargerButton = new Button("PDF");

        // Set the command position to RIGHT
        telechargerButton.setUIID("TitleCommand");
        this.getToolbar().addCommandToRightBar(new Command("PDF") {
            @Override
            public void actionPerformed(ActionEvent evt) {
                telechargerListeProduit();
            }
        });
    }

    private void createGUI() {
        List<Produit> produits = sp.afficher();
        for (Produit produit : produits) {
            addItem(produit);
        }

        revalidate();
    }

    public void addItem(Produit produit) {
        Container c1 = new Container(BoxLayout.x());
        c1.getAllStyles().setBorder(Border.createLineBorder(1, ColorUtil.GRAY));

        Container c2 = new Container(BoxLayout.y());
        c2.setPreferredSize(new Dimension(800, 300));

        Container c3 = new Container(BoxLayout.x());

        Container c4 = new Container(BoxLayout.x());

        Label nom = new Label("Nom : " + String.valueOf(produit.getNom()));
        Label prix = new Label("Prix: " + String.valueOf(produit.getPrix()));
        Label nomCategorie = new Label("Categorie: " + String.valueOf(produit.getNomCategorie()));
        c2.addAll(nom, prix, nomCategorie);
        FontImage suppIcon = FontImage.createMaterial(FontImage.MATERIAL_DELETE, "Label", 4);
        supprimerButton = new Button(suppIcon);
        c3.add(supprimerButton);

        FontImage modifIcon = FontImage.createMaterial(FontImage.MATERIAL_EDIT, "Label", 4);
        modifierButton = new Button(modifIcon);
        c4.add(modifierButton);

        nom.addPointerPressedListener(p -> {
            Dialog.show("Produit", "Nom: " + produit.getNom() + "\nPrix: " + produit.getPrix() + "\nCategorie:" + produit.getNomCategorie(), "OK", null);
        });
        nom.getAllStyles().setAlignment(Component.LEFT);
        c2.setLeadComponent(nom);

        c1.addAll(c2, c3, c4);

        add(c1);

        supprimerButton.addActionListener((evt) -> {
            Dialog dialog = new Dialog("Alerte");
            dialog.setLayout(new BoxLayout(BoxLayout.Y_AXIS));

            Label messageLabel = new Label("Êtes-vous sûr de vouloir supprimer ce produit ?");
            Button okButton = new Button("OK");
            Button annulerButton = new Button("Annuler");

            okButton.addActionListener((e) -> {
                ServiceProduit sp = new ServiceProduit();
                if (sp.supprimer(produit)) {
                    Dialog.show("SUCCESS", "Suppression réussie", "OK", null);
                    removeComponent(c1);
                } else {
                    Dialog.show("ERROR", "Erreur serveur", "OK", null);
                }
                dialog.dispose();
            });

            annulerButton.addActionListener((e) -> {
                dialog.dispose();
            });

            dialog.add(messageLabel);

            Container buttonsContainer = new Container(new BorderLayout());
            buttonsContainer.add(BorderLayout.WEST, okButton);
            buttonsContainer.add(BorderLayout.EAST, annulerButton);
            dialog.add(buttonsContainer);

            dialog.show();
        });

        modifierButton.addActionListener((evt) -> {
            Dialog dialog = new Dialog("Alerte");
            dialog.setLayout(new BoxLayout(BoxLayout.Y_AXIS));

            Label messageLabel = new Label("Êtes-vous sûr de vouloir modifier ce produit ?");
            Button okButton = new Button("OK");
            Button annulerButton = new Button("Annuler");

            okButton.addActionListener((e) -> {
                dialog.dispose();

                ModifierProduitForm modifierForm = new ModifierProduitForm(null, produit);
                modifierForm.show();
            });

            annulerButton.addActionListener((e) -> {
                dialog.dispose();
            });

            dialog.add(messageLabel);

            Container buttonsContainer = new Container(new BorderLayout());
            buttonsContainer.add(BorderLayout.WEST, okButton);
            buttonsContainer.add(BorderLayout.EAST, annulerButton);
            dialog.add(buttonsContainer);

            dialog.show();
        });

    }

    public void updateProduits() {
        removeAll();
        List<Produit> produits = sp.afficher();
        for (Produit produit : produits) {
            addItem(produit);
        }
        revalidate();
    }
 
public void telechargerListeProduit() {
    Dialog dialog = new Dialog("Alerte");
    dialog.setLayout(new BoxLayout(BoxLayout.Y_AXIS));

    Label messageLabel = new Label("Voulez-vous télécharger la liste des produits ?");
    Button okButton = new Button("OK");
    Button annulerButton = new Button("Annuler");

    okButton.addActionListener((e) -> {
        dialog.dispose();

        try {
            // Create a new Document
            Document document = new Document();
            // Set the file path where the PDF will be saved
            String filePath = FileSystemStorage.getInstance().getAppHomePath() + "liste_produits.pdf";
            // Create a new PdfWriter instance
            PdfWriter.getInstance(document, FileSystemStorage.getInstance().openOutputStream(filePath));
            // Open the document
            document.open();

            // Get the list of products
            ServiceProduit sp = new ServiceProduit();
            List<Produit> produits = sp.afficher();

            // Iterate over the products and add them to the document
            Paragraph paragraph = new Paragraph();
            Paragraph titre = new Paragraph("Liste des produits: \n\n");
            titre.setFont(FontFactory.getFont(FontFactory.HELVETICA_BOLD, 30));
            titre.setAlignment(Element.ALIGN_CENTER);
            document.add(titre);

            for (Produit produit : produits) {
                // Create a Paragraph for each product and add it to the document
                paragraph.add("Nom: " + produit.getNom() + "\n");
                paragraph.add("Prix: " + produit.getPrix() + "\n");
                paragraph.add("Nom Categorie: " + produit.getNomCategorie() + "\n\n");
            }

            document.add(paragraph);
            // Close the document
            document.close();

            // Show a success message
            Dialog.show("Success", "La liste des produits a été téléchargée avec succès.", "Show", null);

            // Open the PDF file for viewing
            Display.getInstance().execute(filePath);
        } catch (IOException | DocumentException ex) {
            ex.printStackTrace();
            // Show an error message
            Dialog.show("Error", "Une erreur s'est produite lors du téléchargement de la liste des produits.", "OK", null);
        }
    });

    annulerButton.addActionListener((e) -> {
        dialog.dispose();
    });

    dialog.add(messageLabel);
    Container buttonsContainer = new Container(new BorderLayout());
    buttonsContainer.add(BorderLayout.WEST, okButton);
    buttonsContainer.add(BorderLayout.EAST, annulerButton);
    dialog.add(buttonsContainer);

    dialog.show();
}

  
   
}
