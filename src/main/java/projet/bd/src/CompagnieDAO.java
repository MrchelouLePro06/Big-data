package projet.bd.src;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import java.util.List;

public class CompagnieDAO {
    private final MongoCollection<Document> collection;

    public CompagnieDAO(MongoDatabase database) {
        this.collection = database.getCollection("compagnies");
    }

    public void insertCompagnie(Compagnie compagnie) {
        collection.insertOne(compagnie.toDocument());
    }

    public void insertCompagnies(List<Compagnie> compagnies) {
        List<Document> documents = compagnies.stream()
                .map(Compagnie::toDocument)
                .toList();
        collection.insertMany(documents);
    }

    public void updateCompagnie(int idCompagnie, String field, Object newValue) {
        collection.updateOne(Filters.eq("id_compagnie", idCompagnie), Updates.set(field, newValue));
    }

    public void deleteCompagnie(int idCompagnie) {
        collection.deleteOne(Filters.eq("id_compagnie", idCompagnie));
    }
}