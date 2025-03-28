package views;

import controllers.PossederController;
import models.Posseder;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PossederView extends JFrame {
    private PossederController controller;
    private Color pinkColor = new Color(255, 192, 203);
    private Color backgroundColor = new Color(89, 117, 156); // Couleur de fond RGB(89, 117, 156)

    public PossederView() {
        controller = new PossederController();
        setTitle("Liste des Propriétés");
        setSize(1000, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Appliquer la couleur de fond à la fenêtre
        getContentPane().setBackground(backgroundColor); // Couleur de fond du JFrame
        getContentPane().setLayout(new BorderLayout()); // Utiliser BorderLayout pour le JFrame

        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Disposition verticale
        // Format de date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("DD/MM/YYYY");

        // Charger les propriétés
        List<Posseder> proprietes = controller.getAllProprietaires();
        for (Posseder propriete : proprietes) {
            String matricule = propriete.getMatricule();

            // Créer un panel pour chaque propriété avec un FlowLayout
            JPanel proprietePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            proprietePanel.setBackground(backgroundColor);
            JLabel proprieteLabel = new JLabel("Propriétaire: " + propriete.getNomProprietaire() + " " + propriete.getPrenomProprietaire() +
                "   Matricule: " + matricule + // Use the matricule variable
                "   Date Début: " + propriete.getDateDebutPropriete().format(formatter) +
                "   Date Fin: " + (propriete.getDateFinPropriete() != null ? propriete.getDateFinPropriete().format(formatter) : "N/A"));

            // Bouton Modifier
            JButton modifyButton = new JButton("Modifier");
            modifyButton.setBackground(pinkColor);
            modifyButton.setForeground(Color.WHITE);
            modifyButton.addActionListener(e -> {
                JTextField nomField = new JTextField(propriete.getNomProprietaire());
                JTextField prenomField = new JTextField(propriete.getPrenomProprietaire());
                JTextField matriculeField = new JTextField(matricule);
                JTextField dateDebutField = new JTextField(propriete.getDateDebutPropriete().format(formatter));
                JTextField dateFinField = new JTextField(propriete.getDateFinPropriete() != null ? propriete.getDateFinPropriete().format(formatter) : "N/A");
                Object[] message = {
                        "Nom du propriétaire :", nomField,
                        "Prénom du propriétaire :", prenomField,
                        "Matricule associée :", matriculeField,
                        "Date de début (DD/MM/YYYY):", dateDebutField,
                        "Date de fin (DD/MM/YYYY):", dateFinField
                };

                int option = JOptionPane.showConfirmDialog(this, message, "Modifier une Propriété", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    try {
                        String newNom = nomField.getText();
                        String newPrenom = prenomField.getText();
                        String newMatricule = matriculeField.getText();
                        LocalDate newDebutDate = LocalDate.parse(dateDebutField.getText(), formatter);
                        LocalDate newFinDate = dateFinField.getText().isEmpty() ? null : LocalDate.parse(dateFinField.getText(), formatter);

                        // Retrieve the IDs based on the new values
                        int idProprietaire = controller.getIdProprietaire(newNom, newPrenom); // Method to get ID
                        int idVehicule = controller.getIdVehicule(newMatricule); // Method to get ID

                        if (idProprietaire != -1 && idVehicule != -1) {
                            controller.updatePropriete(propriete.getIdProprietaire(), propriete.getIdVehicule(), newNom, newPrenom, newMatricule, newDebutDate, newFinDate);
                            refreshView();
                        } else {
                            showErrorMessage("Erreur : Propriétaire ou véhicule non trouvé.");
                        }
                    } catch (Exception ex) {
                        showErrorMessage("Une erreur est survenue lors de la modification de la propriété.");
                    }
                }
            });

            // Bouton Supprimer
            JButton deleteButton = new JButton("Supprimer");
            deleteButton.setBackground(pinkColor);
            deleteButton.setForeground(Color.WHITE);
            deleteButton.addActionListener(e -> {
                int confirmation = JOptionPane.showConfirmDialog(this,
                        "Êtes-vous sûr de vouloir supprimer cette propriété ?",
                        "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    try {
                        controller.deletePropriete(propriete.getIdProprietaire(), propriete.getIdVehicule());
                        refreshView();
                    } catch (Exception ex) {
                        showErrorMessage("Une erreur est survenue lors de la suppression de la propriété.");
                    }
                }
            });

            // Ajouter les composants au panel
            proprietePanel.add(proprieteLabel);
            proprietePanel.add(modifyButton);
            proprietePanel.add(deleteButton);
            panel.add(proprietePanel);
        }

        // Panel pour les boutons "Ajouter" et "Retour"
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Bouton "Ajouter une Propriété"
        JButton addButton = new JButton("Ajouter une Propriété");
        addButton.setBackground(pinkColor); // Appliquer la couleur rose
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(e -> {
            JTextField nomField = new JTextField();
            JTextField prenomField = new JTextField();
            JTextField matriculeField = new JTextField();
            JTextField dateDebutField = new JTextField();
            JTextField dateFinField = new JTextField();
            Object[] message = {
                "Nom du propriétaire :", nomField,
                "Prénom du propriétaire :", prenomField,
                "Matricule associée :", matriculeField,
                "Date de début (DD/MM/YYYY):", dateDebutField,
                "Date de fin (DD/MM/YYYY):", dateFinField
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Ajouter une Propriété", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    String nom = nomField.getText();
                    String prenom = prenomField.getText();
                    String matricule = matriculeField.getText();
                    LocalDate dateDebut = LocalDate.parse(dateDebutField.getText(), formatter);
                    LocalDate dateFin = dateFinField.getText().isEmpty() ? null : LocalDate.parse(dateFinField.getText(), formatter);

                    // Retrieve the IDs based on the input
                    int idProprietaire = controller.getIdProprietaire(nom, prenom); // Method to get ID
                    int idVehicule = controller.getIdVehicule(matricule); // Method to get ID

                    if (idProprietaire != -1 && idVehicule != -1) {
                        controller.addPropriete(idProprietaire, idVehicule, dateDebut, dateFin);
                        refreshView();
                    } else {
                        showErrorMessage("Erreur : Propriétaire ou véhicule non trouvé.");
                    }
                } catch (Exception ex) {
                    showErrorMessage("Une erreur est survenue lors de l'ajout de la propriété.");
                }
            }
        });

        // Bouton "Retour"
        JButton backButton = new JButton("Retour");
        backButton.setBackground(pinkColor); // Appliquer la couleur rose
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

        // Rendre la fenêtre visible
        setVisible(true);
    }

    // Méthode pour rafraîchir la vue
    private void refreshView() {
        dispose();
        new PossederView(); // Recharger la vue avec les nouveaux modèles
    }

    // Afficher un message d'erreur
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}