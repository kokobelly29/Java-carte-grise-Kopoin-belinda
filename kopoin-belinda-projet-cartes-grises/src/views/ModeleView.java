package views;

import controllers.ModeleController;
import models.Modele;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ModeleView extends JFrame {
    private ModeleController controller;
    private Color pinkColor = new Color(255, 192, 203);
    private Color backgroundColor = new Color(89, 117, 156); // Couleur de fond RGB(89, 117, 156)

    public ModeleView() {
        controller = new ModeleController();

        setTitle("Gestion des Modèles");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Appliquer la couleur de fond à la fenêtre
        getContentPane().setBackground(backgroundColor); // Couleur de fond du JFrame
        getContentPane().setLayout(new BorderLayout()); // Utiliser BorderLayout pour le JFrame

        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Disposition verticale

        // Charger les modèles
        List<Modele> modeles = controller.getAllModeles();
        for (Modele modele : modeles) {
            String nomMarque = controller.getNomMarqueById(modele.getId_marque());

            // Créer un panel pour chaque modèle avec un FlowLayout
            JPanel modelePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            modelePanel.setBackground(backgroundColor);
            modelePanel.setOpaque(true);
            JLabel modeleLabel = new JLabel(modele.getNom_modele() + " (Marque : " + nomMarque + ")");

            // Bouton Modifier
            JButton modifyButton = new JButton("Modifier");
            modifyButton.setBackground(pinkColor);
            modifyButton.setForeground(Color.WHITE);
            modifyButton.addActionListener(e -> {
                JTextField nomField = new JTextField(modele.getNom_modele());
                JTextField marqueField = new JTextField(nomMarque);
                Object[] message = {
                        "Nom du modèle :", nomField,
                        "Nom de la marque associée :", marqueField
                };

                int option = JOptionPane.showConfirmDialog(this, message, "Modifier un Modèle",
                        JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    try {
                        String newNom = nomField.getText();
                        String newNomMarque = marqueField.getText(); // Nom de la marque
                        controller.updateModele(modele.getId_modele(), newNom, newNomMarque);
                        refreshView();
                    } catch (Exception ex) {
                        showErrorMessage("Une erreur est survenue lors de la modification du modèle.");
                    }
                }
            });

            // Bouton Supprimer
            JButton deleteButton = new JButton("Supprimer");
            deleteButton.setBackground(pinkColor);
            deleteButton.setForeground(Color.WHITE);
            deleteButton.addActionListener(e -> {
                int confirmation = JOptionPane.showConfirmDialog(this,
                        "Êtes-vous sûr de vouloir supprimer ce modèle ?",
                        "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    try {
                        controller.deleteModele(modele.getId_modele());
                        refreshView();
                    } catch (Exception ex) {
                        showErrorMessage("Une erreur est survenue lors de la suppression du modèle.");
                    }
                }
            });

            // Ajouter les composants au panel
            modelePanel.add(modeleLabel);
            modelePanel.add(modifyButton);
            modelePanel.add(deleteButton);
            panel.add(modelePanel);
        }

        // Panel pour les boutons "Ajouter" et "Retour"
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(backgroundColor); // Couleur de fond du panneau de boutons
        buttonPanel.setOpaque(true); // Assurez-vous que le panneau est opaque

        // Bouton "Ajouter un Modèle"
        JButton addButton = new JButton("Ajouter un Modèle");
        addButton.setBackground(pinkColor); // Appliquer la couleur rose au bouton
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(e -> {
            JTextField nomField = new JTextField();
            JTextField marqueField = new JTextField();
            Object[] message = {
                    "Nom du modèle :", nomField,
                    "Nom de la marque associée :", marqueField
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Ajouter un Modèle",
                    JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    String nom = nomField.getText();
                    String nomMarque = marqueField.getText(); // Utiliser le nom de la marque
                    controller.addModele(nom, nomMarque);
                    refreshView();
                } catch (Exception ex) {
                    showErrorMessage("Une erreur est survenue lors de l'ajout du modèle.");
                }
            }
        });

        // Bouton "Retour"
        JButton backButton = new JButton("Retour");
        backButton.setBackground(pinkColor); // Appliquer la couleur rose au bouton
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> dispose());

        // Ajouter les boutons au panel
        buttonPanel.add(addButton);
        buttonPanel.add(backButton);

        // Ajouter le panel des boutons au panneau principal
        panel.add(buttonPanel);

        // Ajouter un JScrollPane pour la barre de défilement
        JScrollPane scrollPane = new JScrollPane(panel);
        add(scrollPane);

        // Rendre visible
        setVisible(true);
    }

    // Méthode pour rafraîchir la vue
    private void refreshView() {
        dispose();
        new ModeleView(); // Recharger la vue avec les nouveaux modèles
    }

    // Afficher un message d'erreur
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}
