package models;

import java.time.LocalDate;

/**
 * Classe représentant la table "POSSEDER" de la base de données.
 * Les noms des attributs correspondent aux colonnes de la table.
 */
public class Posseder {
    private int idProprietaire; // Identifiant unique du propriétaire (clé étrangère)
    private String nomProprietaire;
    private String prenomProprietaire;
    private int idVehicule; // Identifiant du véhicule associé (clé étrangère)
    private String matricule;
    private LocalDate dateDebutPropriete; // Date de début de la propriété du véhicule
    private LocalDate dateFinPropriete; // Date de fin de la propriété du véhicule

    // Constructeur
    public Posseder(int idProprietaire, String nomProprietaire, String prenomProprietaire, int idVehicule, String matricule, LocalDate dateDebutPropriete, LocalDate dateFinPropriete) {
        this.idProprietaire = idProprietaire;
        this.nomProprietaire = nomProprietaire;
        this.prenomProprietaire = prenomProprietaire;
        this.idVehicule = idVehicule;
        this.matricule = matricule;
        this.dateDebutPropriete = dateDebutPropriete;
        this.dateFinPropriete = dateFinPropriete;
    }

    // Getters et Setters
    public int getIdProprietaire() {
        return idProprietaire;
    }

    public void setIdProprietaire(int idProprietaire) {
        this.idProprietaire = idProprietaire;
    }

    public int getIdVehicule() {
        return idVehicule;
    }

    public void setIdVehicule(int idVehicule) {
        this.idVehicule = idVehicule;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule( String matricule){
        this.matricule = matricule;
    }

    public LocalDate getDateDebutPropriete() {
        return dateDebutPropriete;
    }

    public void setDateDebutPropriete(LocalDate dateDebutPropriete) {
        this.dateDebutPropriete = dateDebutPropriete;
    }

    public LocalDate getDateFinPropriete() {
        return dateFinPropriete;
    }

    public void setDateFinPropriete(LocalDate dateFinPropriete) {
        this.dateFinPropriete = dateFinPropriete;
    }

    public String getNomProprietaire() {
        return nomProprietaire;
    }

    public void setNomProprietaire(String nomProprietaire) {
        this.nomProprietaire = nomProprietaire;
    }

    public String getPrenomProprietaire() {
        return prenomProprietaire;
    }

    public void setPrenomProprietaire(String prenomProprietaire) {
        this.prenomProprietaire = prenomProprietaire;
    }

    // Méthode toString() : Affiche un résumé des informations du propriétaire et du véhicule.
    @Override
    public String toString() {
        return "Propriétaire: " + nomProprietaire + " " + prenomProprietaire + ", Véhicule : " + matricule;
    }

    // Méthode pour vérifier si la propriété du véhicule est encore valide
    public boolean estProprietaireActuel() {
        LocalDate today = LocalDate.now(); // Récupère la date actuelle
        
        // Vérifie si la date actuelle est après la date de début de propriété et avant la date de fin de propriété
        return today.isAfter(dateDebutPropriete) && (dateFinPropriete == null || today.isBefore(dateFinPropriete));
    }
}