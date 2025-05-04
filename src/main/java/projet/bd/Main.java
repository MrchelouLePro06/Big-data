package projet.bd;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import projet.bd.src.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        MongoClient client = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = client.getDatabase("bd_aviation");

        VolDAO vd = new VolDAO(database);
        Compagnie compagnie = new Compagnie(213, "Air Algerie", "TeamDZ");
        Aeroport depart = new Aeroport(213, "Boumedienne", "Alger", "Algerie");
        Aeroport arrivee = new Aeroport(212, "aeroportMaroc", "Rabat", "Maroc");
        Avion avion = new Avion(1000, "A320", 150, compagnie);

        vd.insertVol(new Vol(10000, "2024-01-01", "2024-01-02","Parti",compagnie,depart,arrivee,avion,));
        // Test vols
        VolAggregationDAO vad = new VolAggregationDAO(database);
        // Vols entre deux dates
        System.out.println("=== Vols entre deux dates ===");
        List<Document> volsEntreDates = vad.getVolsEntreDates("2024-01-01", "2024-12-31");
        volsEntreDates.forEach(doc -> System.out.println(doc.toJson()));



        // Fermeture de la connexion
        client.close();
    }
}