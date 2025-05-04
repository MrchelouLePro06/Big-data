package projet.bd.src;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import java.util.List;

public class PiloteDAO {
    private final MongoCollection<Document> collection;

    public PiloteDAO(MongoDatabase database) {
        this.collection = database.getCollection("pilotes");
    }

    public void insertPilote(Pilote pilote) {
        if (findById(pilote.getId()) == null) {
            collection.insertOne(pilote.toDocument());
            System.out.println("Pilote inséré avec succès.");
        } else {
            System.out.println("Le pilote avec l'ID " + pilote.getId() + " existe déjà.");
        }
    }

    public Pilote findById(String id) {
        Document doc = collection.find(Filters.eq("id", id)).first();
        if (doc != null) {
            return new Pilote(
                    doc.getInteger("id"),
                    doc.getString("nom")
            );
        }
        return null;
    }

    public void insertPilotes(List<Pilote> pilotes) {
        List<Document> documents = pilotes.stream()
                .map(Pilote::toDocument)
                .toList();
        collection.insertMany(documents);
    }

    public void updatePilote(int idPilote, String field, Object newValue) {
        collection.updateOne(Filters.eq("id_pilote", idPilote), Updates.set(field, newValue));
    }

    public void deletePilote(int idPilote) {
        collection.deleteOne(Filters.eq("id_pilote", idPilote));
    }
}