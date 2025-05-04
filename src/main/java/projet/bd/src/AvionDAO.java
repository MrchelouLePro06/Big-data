package projet.bd.src;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import java.util.List;

public class AvionDAO {
    private final MongoCollection<Document> collection;

    public AvionDAO(MongoDatabase database) {
        this.collection = database.getCollection("avions");
    }

    public void insertAvion(Avion avion) {
        collection.insertOne(avion.toDocument());
    }

    public void insertAvions(List<Avion> avions) {
        List<Document> documents = avions.stream()
                .map(Avion::toDocument)
                .toList();
        collection.insertMany(documents);
    }

    public void updateAvion(int idAvion, String field, Object newValue) {
        collection.updateOne(Filters.eq("id_avion", idAvion), Updates.set(field, newValue));
    }

    public void deleteAvion(int idAvion) {
        collection.deleteOne(Filters.eq("id_avion", idAvion));
    }
}