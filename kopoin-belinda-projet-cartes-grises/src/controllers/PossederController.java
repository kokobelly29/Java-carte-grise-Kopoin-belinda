package controllers;

import database.DatabaseConnection;
import models.Posseder;

import javax.swing.JOptionPane; // Pour afficher des boîtes de dialogue standard
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Contrôleur pour gérer les propriétés dans la base de données.
 */
public class PossederController {
    // Récupère toutes les propriétés de la base de données
    public List<Posseder> getAllProprietaires() {
        List<Posseder> proprietes = new ArrayList<>();
        String query = "SELECT p.id_proprietaire, p.nom AS nom_proprietaire, p.prenom AS prenom_proprietaire, " +
                "v.id_vehicule, v.matricule, ps.date_debut_propriete, ps.date_fin_propriete " +
                "FROM POSSEDER ps " +
                "JOIN PROPRIETAIRE p ON ps.id_proprietaire = p.id_proprietaire " +
                "JOIN VEHICULE v ON ps.id_vehicule = v.id_vehicule";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            // Parcourt les résultats et ajoute chaque propriété à la liste
            while (rs.next()) {
                proprietes.add(new Posseder(
                        rs.getInt("id_proprietaire"),
                        rs.getString("nom_proprietaire"),
                        rs.getString("prenom_proprietaire"),
                        rs.getInt("id_vehicule"),
                        rs.getString("matricule"),
                        rs.getDate("date_debut_propriete").toLocalDate(),
                        rs.getDate("date_fin_propriete") != null ? rs.getDate("date_fin_propriete").toLocalDate() : null));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return proprietes;
    }

    // Ajouter une propriété
    public void addPropriete(int idProprietaire, int idVehicule, LocalDate dateDebutPropriete,
                             LocalDate dateFinPropriete) {
        // Vérifiez si l'identifiant du propriétaire ou du véhicule est -1
        if (idProprietaire == -1 || idVehicule == -1) {
            // Si le propriétaire ou le véhicule n'existe pas, afficher un message d'erreur
            JOptionPane.showMessageDialog(
                    null, "Erreur : Le propriétaire avec l'ID '" + idProprietaire
                            + "' ou le véhicule avec l'ID '" + idVehicule + "' n'existe pas.",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Vérifier si un propriétaire existe déjà pour ce véhicule
        if (existsPosseder(idProprietaire, idVehicule, dateDebutPropriete, dateFinPropriete)) {
            JOptionPane.showMessageDialog(null, "Erreur : Un propriétaire existe déjà pour ce véhicule.", "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Ajoutez ici le code pour ajouter la propriété si les identifiants sont valides
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO POSSEDER (id_proprietaire, id_vehicule, date_debut_propriete, date_fin_propriete) VALUES (?, ?, ?, ?)")) {

            ps.setInt(1, idProprietaire);
            ps.setInt(2, idVehicule);
            ps.setDate(3, Date.valueOf(dateDebutPropriete)); // Conversion de LocalDate à java.sql.Date
            ps.setDate(4, dateFinPropriete != null ? Date.valueOf(dateFinPropriete) : null); // Conversion de LocalDate à java.sql.Date
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Modifier une propriété
public void updatePropriete(int idProprietaire, int idVehicule, String newNom, String newPrenom, String newMatricule, LocalDate newDebutDate, LocalDate newFinDate) {
    // Requête pour mettre à jour la relation dans la table POSSEDER
    String updatePossessionQuery = "UPDATE POSSEDER SET date_debut_propriete = ?, date_fin_propriete = ? " +
                                   "WHERE id_proprietaire = ? AND id_vehicule = ?";

    try (Connection conn = DatabaseConnection.getConnection()) {
        try (PreparedStatement ps = conn.prepareStatement(updatePossessionQuery)) {
            ps.setDate(1, Date.valueOf(newDebutDate)); // Utiliser newDebutDate
            ps.setDate(2, newFinDate != null ? Date.valueOf(newFinDate) : null); // Utiliser newFinDate
            ps.setInt(3, idProprietaire);
            ps.setInt(4, idVehicule);
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                showAlert("Succès", "Relation mise à jour avec succès !");
            } else {
                showAlert("Erreur", "Erreur lors de la mise à jour.");
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        showAlert("Erreur", "Une erreur s'est produite lors de la mise à jour de la relation.");
    }
}

    // Supprimer une propriété
    public void deletePropriete(int idProprietaire, int idVehicule) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn
                     .prepareStatement("DELETE FROM POSSEDER WHERE id_proprietaire = ? AND id_vehicule = ?")) {
            ps.setInt(1, idProprietaire);
            ps.setInt(2, idVehicule);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Propriété supprimée avec succès !");
            } else {
                JOptionPane.showMessageDialog(null, "Aucune propriété trouvée à supprimer.", "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de la suppression de la propriété : " + e.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Vérifier si un propriétaire existe déjà pour un véhicule
    private boolean existsPosseder(int idProprietaire, int idVehicule, LocalDate dateDebutPropriete,
                                    LocalDate dateFinPropriete) {
        String query = "SELECT COUNT(*) FROM POSSEDER WHERE id_proprietaire = ? AND id_vehicule = ? AND date_debut_propriete = ? AND date_fin_propriete = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, idProprietaire);
            ps.setInt(2, idVehicule);
            ps.setDate(3, Date.valueOf(dateDebutPropriete)); // Conversion de LocalDate à java.sql.Date
            ps.setDate(4, dateFinPropriete != null ? Date.valueOf(dateFinPropriete) : null); // Conversion de LocalDate à java.sql.Date

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Vérifie si le compte est supérieur à 0
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to get the ID of a Proprietaire based on name and surname
    public int getIdProprietaire(String nom, String prenom) {
        String query = "SELECT id_proprietaire FROM PROPRIETAIRE WHERE nom = ? AND prenom = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, nom);
            ps.setString(2, prenom);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_proprietaire");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if not found
    }

    // Method to get the ID of a Vehicule based on matricule
    public int getIdVehicule(String matricule) {
        String query = "SELECT id_vehicule FROM VEHICULE WHERE matricule = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, matricule);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_vehicule");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if not found
    }

    // Méthode pour récupérer la date de début de propriété à partir de l'ID du propriétaire et de l'ID du véhicule
    public LocalDate getDateDebutPropriete(int idProprietaire, int idVehicule) {
        String query = "SELECT date_debut_propriete FROM POSSEDER WHERE id_proprietaire = ? AND id_vehicule = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, idProprietaire); // Utiliser l'ID du propriétaire comme paramètre
            ps.setInt(2, idVehicule); // Utiliser l'ID du véhicule comme paramètre

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDate("date_debut_propriete").toLocalDate(); // Retourne la date sous forme de LocalDate
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Si la date n'est pas trouvée
    }

    public LocalDate getDateFinPropriete(int idProprietaire, int idVehicule) {
        String query = "SELECT date_fin_propriete FROM POSSEDER WHERE id_proprietaire = ? AND id_vehicule = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, idProprietaire); // Utiliser l'ID du propriétaire comme paramètre
            ps.setInt(2, idVehicule); // Utiliser l'ID du véhicule comme paramètre

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDate("date_fin_propriete").toLocalDate(); // Retourne la date sous forme de LocalDate
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Si la date n'est pas trouvée
    }

    // Afficher une alerte
    private void showAlert(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

}