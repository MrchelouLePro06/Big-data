package projet.bd.src;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import java.util.stream.Collectors;
import org.bson.Document;

import java.util.List;

public class VolDAO {
    private final MongoCollection<Document> collection;

    public VolDAO(MongoDatabase database) {
        this.collection = database.getCollection("vols");
    }

    // Insérer un enregistrement
    public void insertVol(Vol vol) {
        collection.insertOne(vol.toDocument());
    }

    // Insérer plusieurs enregistrements
    public void insertVols(List<Vol> vols) {
        List<Document> documents = vols.stream().map(Vol::toDocument).toList();
        collection.insertMany(documents);
    }

    // Modifier un enregistrement
    public void updateVol(int idVol, String field, Object newValue) {
        collection.updateOne(Filters.eq("_id", idVol), Updates.set(field, newValue));
    }

    // Supprimer un enregistrement
    public void deleteVol(int idVol) {
        collection.deleteOne(Filters.eq("_id", idVol));
    }

    // Supprimer plusieurs enregistrements
    public void deleteVols(List<Integer> ids) {
        collection.deleteMany(Filters.in("_id", ids));
    }
}