package projet.bd.src;

import org.bson.Document;

public class Reservation {
    private int pnr;
    private String date_reservation;
    private String classe;
    private int siege;
    private Passager passager;
    private Vol vol;
    private Compagnie compagnie;

    public Reservation(int pnr, String date_reservation, String classe, int siege, Passager passager, Vol vol, Compagnie compagnie) {
        this.pnr = pnr;
        this.date_reservation = date_reservation;
        this.classe = classe;
        this.siege = siege;
        this.passager = passager;
        this.vol = vol;
        this.compagnie = compagnie;
    }

    public Document toDocument() {
        return new Document("pnr", pnr)
                .append("date_reservation", date_reservation)
                .append("classe", classe)
                .append("siege", siege)
                .append("passager",passager.toDocument())
                .append("vol",vol.toDocument())
                .append("compagnie", compagnie.toDocument());
    }

    public int getPnr() {
        return pnr;
    }

    public void setPnr(int pnr) {
        this.pnr = pnr;
    }

    public String getDate_reservation() {
        return date_reservation;
    }

    public void setDate_reservation(String date_reservation) {
        this.date_reservation = date_reservation;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public int getSiege() {
        return siege;
    }

    public void setSiege(int siege) {
        this.siege = siege;
    }

    public Passager getPassager() {
        return passager;
    }

    public void setPassager(Passager passager) {
        this.passager = passager;
    }

    public Vol getVol() {
        return vol;
    }

    public void setVol(Vol vol) {
        this.vol = vol;
    }

    public Compagnie getCompagnie() {
        return compagnie;
    }

    public void setCompagnie(Compagnie compagnie) {
        this.compagnie = compagnie;
    }
}
