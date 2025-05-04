package projet.bd.src;
import org.bson.Document;

public class Vol {
    private int id_vol;
    private String numero_vol_base;
    private String date_depart;
    private String date_arrivee;
    private String statut;
    private Compagnie compagnie;
    private Aeroport aeroport_depart;
    private Aeroport aeroport_arrivee;
    private Avion avion;
    private Pilote equipage;
    private int id_opl;

    public Vol(int id_vol, String numero_vol_base, String date_depart, String date_arrivee, String statut,
               Compagnie compagnie, Aeroport aeroport_depart, Aeroport aeroport_arrivee,
               Avion avion, Pilote equipage, int id_opl) {
        this.id_vol = id_vol;
        this.numero_vol_base = numero_vol_base;
        this.date_depart = date_depart;
        this.date_arrivee = date_arrivee;
        this.statut = statut;
        this.compagnie = compagnie;
        this.aeroport_depart = aeroport_depart;
        this.aeroport_arrivee = aeroport_arrivee;
        this.avion = avion;
        this.equipage = equipage;
        this.id_opl = id_opl;
    }

    public Document toDocument() {
        return new Document("_id", id_vol)
                .append("numero_vol_base", numero_vol_base)
                .append("date_depart", date_depart)
                .append("date_arrivee", date_arrivee)
                .append("statut", statut)
                .append("compagnie", compagnie.toDocument())
                .append("aeroport_depart", aeroport_depart.toDocument())
                .append("aeroport_arrivee", aeroport_arrivee.toDocument())
                .append("avion", avion.toDocument())
                .append("equipage", equipage.toDocument());
    }

    public int getId_vol() {
        return id_vol;
    }

    public void setId_vol(int id_vol) {
        this.id_vol = id_vol;
    }

    public String getDate_depart() {
        return date_depart;
    }

    public void setDate_depart(String date_depart) {
        this.date_depart = date_depart;
    }

    public String getDate_arrivee() {
        return date_arrivee;
    }

    public void setDate_arrivee(String date_arrivee) {
        this.date_arrivee = date_arrivee;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getNumero_vol_base() {
        return numero_vol_base;
    }

    public void setNumero_vol_base(String numero_vol_base) {
        this.numero_vol_base = numero_vol_base;
    }

    public Compagnie getCompagnie() {
        return compagnie;
    }

    public void setCompagnie(Compagnie compagnie) {
        this.compagnie = compagnie;
    }

    public Aeroport getAeroport_depart() {
        return aeroport_depart;
    }

    public void setAeroport_depart(Aeroport aeroport_depart) {
        this.aeroport_depart = aeroport_depart;
    }

    public Aeroport getAeroport_arrivee() {
        return aeroport_arrivee;
    }

    public void setAeroport_arrivee(Aeroport aeroport_arrivee) {
        this.aeroport_arrivee = aeroport_arrivee;
    }

    public Avion getAvion() {
        return avion;
    }

    public void setAvion(Avion avion) {
        this.avion = avion;
    }

    public Pilote getEquipage() {
        return equipage;
    }

    public void setEquipage(Pilote equipage) {
        this.equipage = equipage;
    }

    public int getId_opl() {
        return id_opl;
    }

    public void setId_opl(int id_opl) {
        this.id_opl = id_opl;
    }
}
