package projet.bd.src;

import org.bson.Document;

public class Equipage {
    private Pilote commandant;
    private Pilote opl;

    public Equipage(Pilote commandant, Pilote opl) {
        this.commandant = commandant;
        this.opl = opl;
    }

    public Document toDocument() {
        return new Document()
                .append("commandant", commandant.toDocument())
                .append("opl", opl.toDocument());
    }

    public Pilote getCommandant() {
        return commandant;
    }

    public void setCommandant(Pilote commandant) {
        this.commandant = commandant;
    }

    public Pilote getOpl() {
        return opl;
    }

    public void setOpl(Pilote opl) {
        this.opl = opl;
    }
}
