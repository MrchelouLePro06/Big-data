package projet.bd.src;

import org.bson.Document;

public class Aeroport {
    private String id_aeroport;
    private String nom;
    private String ville;
    private String pays;

    public Aeroport(String id_aeroport, String nom, String ville, String pays) {
        this.id_aeroport = id_aeroport;
        this.nom = nom;
        this.ville = ville;
        this.pays = pays;
    }

    public Document toDocument() {
        return new Document("_id", id_aeroport)
                .append("nom", nom)
                .append("ville", ville)
                .append("pays", pays);
    }

    public static Aeroport fromDocument(Document doc) {
        String id = doc.getString("_id");
        String nom = doc.getString("nom");
        String ville = doc.getString("ville");
        String pays = doc.getString("pays");
        return new Aeroport(id, nom, ville, pays);
    }


    public String getId_aeroport() {
        return id_aeroport;
    }

    public void setId_aeroport(String id_aeroport) {
        this.id_aeroport = id_aeroport;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }
}
