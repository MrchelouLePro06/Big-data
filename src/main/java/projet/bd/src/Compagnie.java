package projet.bd.src;

import org.bson.Document;

public class Compagnie {
    private int id_compagnie;
    private String nom;
    private String alliance;

    public Compagnie(int id_compagnie, String nom, String alliance) {
        this.id_compagnie = id_compagnie;
        this.nom = nom;
        this.alliance = alliance;
    }

    public Document toDocument() {
        return new Document("id_compagnie", id_compagnie)
                .append("nom", nom)
                .append("alliance", alliance);
    }

    public int getId_compagnie() {
        return id_compagnie;
    }

    public void setId_compagnie(int id_compagnie) {
        this.id_compagnie = id_compagnie;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAlliance() {
        return alliance;
    }

    public void setAlliance(String alliance) {
        this.alliance = alliance;
    }
}
