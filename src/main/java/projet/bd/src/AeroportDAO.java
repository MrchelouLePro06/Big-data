package projet.bd.src;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import java.util.List;

public class AeroportDAO {
    private final MongoCollection<Document> collection;

    public AeroportDAO(MongoDatabase database) {
        this.collection = database.getCollection("aeroports");
    }

    public void insertAeroport(Aeroport aeroport) {
        collection.insertOne(aeroport.toDocument());
    }

    public void insertAeroports(List<Aeroport> aeroports) {
        List<Document> documents = aeroports.stream()
                .map(Aeroport::toDocument)
                .toList();
        collection.insertMany(documents);
    }

    public void updateAeroport(int idAeroport, String field, Object newValue) {
        collection.updateOne(Filters.eq("_id", idAeroport), Updates.set(field, newValue));
    }

    public void deleteAeroport(int idAeroport) {
        collection.deleteOne(Filters.eq("_id", idAeroport));
    }
}