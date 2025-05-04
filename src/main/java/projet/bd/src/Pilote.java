package projet.bd.src;

import org.bson.Document;

public class Pilote {
    private int id_pilote;
    private String nom;
    private String prenom;
    private String date_embauche;
    private Compagnie compagnie;

    public Pilote(int id_pilote, String nom, String prenom, String date_embauche, Compagnie compagnie) {
        this.id_pilote = id_pilote;
        this.nom = nom;
        this.prenom = prenom;
        this.date_embauche = date_embauche;
        this.compagnie = compagnie;
    }

    public Document toDocument() {
        return new Document("id_pilote", id_pilote)
                .append("nom", nom)
                .append("prenom", prenom)
                .append("date_embauche", date_embauche)
                .append("compagnie", compagnie.toDocument());
    }

    public int getId_pilote() {
        return id_pilote;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getDate_embauche() {
        return date_embauche;
    }

    public void setId_pilote(int id_pilote) {
        this.id_pilote = id_pilote;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setDate_embauche(String date_embauche) {
        this.date_embauche = date_embauche;
    }

    public Compagnie getCompagnie() {
        return compagnie;
    }

    public void setCompagnie(Compagnie compagnie) {
        this.compagnie = compagnie;
    }
}
