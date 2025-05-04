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
        if (findById(compagnie.getId_compagnie()) == null) {
            collection.insertOne(compagnie.toDocument());
            System.out.println("Compagnie insérée avec succès.");
        } else {
            System.out.println("La compagnie avec l'ID " + compagnie.getId_compagnie() + " existe déjà.");
        }
    }

    public Compagnie findById(String id) {
        Document doc = collection.find(Filters.eq("id_compagnie", id)).first();
        if (doc != null) {
            return new Compagnie(
                    doc.getString("id_compagnie"),
                    doc.getString("nom"),
                    doc.getString("alliance")
            );
        }
        return null;
    }

    public void insertCompagnies(List<Compagnie> compagnies) {
        List<Document> documents = compagnies.stream()
                .map(Compagnie::toDocument)
                .toList();
        collection.insertMany(documents);
    }

    public void updateCompagnie(String idCompagnie, String field, Object newValue) {
        collection.updateOne(Filters.eq("id_compagnie", idCompagnie), Updates.set(field, newValue));
    }

    public void deleteCompagnie(String idCompagnie) {
        collection.deleteOne(Filters.eq("id_compagnie", idCompagnie));
    }
}