package projet.bd.src;

import org.bson.Document;

public class Avion {
    private String id_avion;
    private String modele;
    private int capacite;
    private int compagnie;

    public Avion(String id_avion, String modele, int capacite, int compagnie) {
        this.id_avion = id_avion;
        this.modele = modele;
        this.capacite = capacite;
        this.compagnie = compagnie;
    }

    public Document toDocument() {
        return new Document("id_avion", id_avion)
                .append("modele", modele)
                .append("capacite", capacite)
                .append("id_compagnie", compagnie);
    }

    public String getId_avion() {
        return id_avion;
    }

    public void setId_avion(String id_avion) {
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

    public int getCompagnie() {
        return compagnie;
    }

    public void setCompagnie(int compagnie) {
        this.compagnie = compagnie;
    }
}
