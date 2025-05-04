package projet.bd.src;

import org.bson.Document;

public class Pilote {
    private int id_pilote;
    private String nom;

    public Pilote(int id_pilote, String nom) {
        this.id_pilote = id_pilote;
        this.nom = nom;

    }

    public Document toDocument() {
        return new Document("id_pilote", id_pilote)
                .append("nom", nom);
    }

    public int getId_pilote() {
        return id_pilote;
    }

    public void setId_pilote(int id_pilote) {
        this.id_pilote = id_pilote;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
