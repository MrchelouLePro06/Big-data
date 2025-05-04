package projet.bd;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import projet.bd.src.VolAggregationDAO;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        MongoClient client = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = client.getDatabase("bd_aviation");
        VolAggregationDAO volDAO = new VolAggregationDAO(database);

        // Test 1 : Vols entre deux dates
        System.out.println("=== Vols entre deux dates ===");
        List<Document> volsEntreDates = volDAO.getVolsEntreDates("2024-01-01", "2024-12-31");
        volsEntreDates.forEach(doc -> System.out.println(doc.toJson()));

        // Test 2 : Nombre de vols par compagnie
        System.out.println("\n=== Nombre de vols par compagnie ===");
        List<Document> volsParCompagnie = volDAO.countVolsParCompagnie();
        volsParCompagnie.forEach(doc -> System.out.println(doc.toJson()));

        // Test 3 : Nombre de vols par ville de départ
        System.out.println("\n=== Nombre de vols par ville de départ ===");
        List<Document> volsParVille = volDAO.countVolsParVilleDepart();
        volsParVille.forEach(doc -> System.out.println(doc.toJson()));

        // Test 4 : Vols avec détails du pilote
        System.out.println("\n=== Vols avec détails du pilote ===");
        List<Document> volsAvecPilote = volDAO.getVolsAvecPilote();
        volsAvecPilote.forEach(doc -> System.out.println(doc.toJson()));

        // Test 5 : Moyenne de durée des vols par avion
        System.out.println("\n=== Moyenne de durée des vols par avion ===");
        List<Document> moyenneDuree = volDAO.moyenneDureeParAvion();
        moyenneDuree.forEach(doc -> System.out.println(doc.toJson()));

        // Test 6 : Derniers vols pour une compagnie
        System.out.println("\n=== Derniers vols pour une compagnie ===");
        List<Document> derniersVols = volDAO.derniersVolsParCompagnie("comp1", 5);
        derniersVols.forEach(doc -> System.out.println(doc.toJson()));

        // Fermeture de la connexion
        client.close();
    }
}