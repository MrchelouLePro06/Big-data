package projet.bd.src;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import java.util.List;

public class PassagerDAO {
    private final MongoCollection<Document> collection;

    public PassagerDAO(MongoDatabase database) {
        this.collection = database.getCollection("passagers");
    }

    public void insertPassager(Passager passager) {
        if (findById(passager.getId()) == null) {
            collection.insertOne(passager.toDocument());
            System.out.println("passager inséré avec succès.");
        } else {
            System.out.println("Le passager avec l'ID " + passager.getId() + " existe déjà.");
        }
    }
    public Passager findById(int id) {
        Document doc = collection.find(Filters.eq("id", id)).first();
        if (doc != null) {
            return new Passager(
                    doc.getInteger("id"),
                    doc.getString("nom"),
                    doc.getString("prenom"),
                    doc.getString("email"),
                    doc.getString("telephone"),
                    doc.getString("date_naissance")
            );
        }
        return null;
    }

    public void insertPassagers(List<Passager> passagers) {
        List<Document> documents = passagers.stream()
                .map(Passager::toDocument)
                .toList();
        collection.insertMany(documents);
    }

    public void updatePassager(int id, String field, Object newValue) {
        collection.updateOne(Filters.eq("id", id), Updates.set(field, newValue));
    }

    public void deletePassager(int id) {
        collection.deleteOne(Filters.eq("id", id));
    }
}