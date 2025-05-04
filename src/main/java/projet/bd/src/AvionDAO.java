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
        if (findById(avion.getId_avion()) == null) {
            collection.insertOne(avion.toDocument());
            System.out.println("Avion inséré avec succès.");
        } else {
            System.out.println("L'avion avec l'ID " + avion.getId_avion() + " existe déjà.");
        }
    }
    public Avion findById(String id) {
        Document doc = collection.find(Filters.eq("id_avion", id)).first();
        if (doc != null) {
            return new Avion(
                    doc.getString("id_avion"),
                    doc.getString("modele"),
                    doc.getInteger("capacite"),
                    doc.getInteger("id_compagnie")
            );
        }
        return null;
    }
    public void insertAvions(List<Avion> avions) {
        List<Document> documents = avions.stream()
                .map(Avion::toDocument)
                .toList();
        collection.insertMany(documents);
    }

    public void updateAvion(String idAvion, String field, Object newValue) {
        collection.updateOne(Filters.eq("id_avion", idAvion), Updates.set(field, newValue));
    }

    public void deleteAvion(String idAvion) {
        collection.deleteOne(Filters.eq("id_avion", idAvion));
    }
}