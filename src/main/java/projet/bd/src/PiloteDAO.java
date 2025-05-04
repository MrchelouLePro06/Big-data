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
        collection.insertOne(pilote.toDocument());
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