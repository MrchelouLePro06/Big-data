package projet.bd.src;

import org.bson.Document;

import java.util.List;

public class Passager {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String date_naissance;
    private String telephone;

    public Passager(int id, String nom, String prenom, String email, String telephone,String date_naissance) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.date_naissance=date_naissance;
    }

    public Document toDocument() {
        return new Document("id", id)
                .append("nom", nom)
                .append("prenom", prenom)
                .append("email", email)
                .append("date_naissance", date_naissance)
                .append("telephone", telephone);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getDate_naissance() {
        return date_naissance;
    }

    public void setDate_naissance(String date_naissance) {
        this.date_naissance = date_naissance;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
