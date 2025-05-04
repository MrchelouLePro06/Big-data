package projet.bd.src;

import org.bson.Document;

public class Avion {
    private int id_avion;
    private String modele;
    private int capacite;
    private Compagnie compagnie;

    public Avion(int id_avion, String modele, int capacite, Compagnie compagnie) {
        this.id_avion = id_avion;
        this.modele = modele;
        this.capacite = capacite;
        this.compagnie = compagnie;
    }

    public Document toDocument() {
        return new Document("id_avion", id_avion)
                .append("modele", modele)
                .append("capacite", capacite)
                .append("compagnie", compagnie.toDocument());
    }

    public int getId_avion() {
        return id_avion;
    }

    public void setId_avion(int id_avion) {
        this.id_avion = id_avion;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public Compagnie getCompagnie() {
        return compagnie;
    }

    public void setCompagnie(Compagnie compagnie) {
        this.compagnie = compagnie;
    }
}
