package projet.bd.src;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import java.util.List;
import static com.mongodb.client.model.Filters.eq;


public class AeroportDAO {
    private final MongoCollection<Document> collection;

    public AeroportDAO(MongoDatabase database) {
        this.collection = database.getCollection("aeroports");
    }

    public void insertAeroport(Aeroport aeroport) {
        if (findById(aeroport.getId_aeroport()) == null) {
            collection.insertOne(aeroport.toDocument());
            System.out.println("Aéroport inséré avec succès.");
        } else {
            System.out.println("L'aéroport avec l'ID " + aeroport.getId_aeroport() + " existe déjà.");
        }
    }

    public void insertAeroports(List<Aeroport> aeroports) {
        List<Document> documents = aeroports.stream()
                .map(Aeroport::toDocument)
                .toList();
        collection.insertMany(documents);
    }

    public void updateAeroport(String idAeroport, String field, Object newValue) {
        collection.updateOne(Filters.eq("_id", idAeroport), Updates.set(field, newValue));
    }

    public void deleteAeroport(String idAeroport) {
        collection.deleteOne(Filters.eq("_id", idAeroport));
    }

    public Aeroport findById(String id) {
        Document doc = collection.find(eq("_id", id)).first();
        if (doc != null) {
            return Aeroport.fromDocument(doc);
        }
        return null;
    }

}