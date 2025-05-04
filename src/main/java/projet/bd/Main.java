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

        //test aeroports
        AeroportDAO aeroportDAO = new AeroportDAO(database);
        Aeroport tahiadz = new Aeroport("ALG213", "Boumedienne", "Alger", "Algerie");
        aeroportDAO.insertAeroport(tahiadz);
        System.out.println(aeroportDAO.findById("ALG213").toDocument().toJson());
        aeroportDAO.deleteAeroport("ALG213");
        System.out.println(aeroportDAO.findById("ALG213"));

        //test passager
        PassagerDAO passagerDAO = new PassagerDAO(database);
        Passager steve=new Passager(213,"Mansour","Mehdi","Mehdi.Mansour@fac.com","0102030405","01/03/2002");
        passagerDAO.insertPassager(steve);
        System.out.println(passagerDAO.findById(steve.getId()).toDocument().toJson());
        // Test vols
        VolAggregationDAO vad = new VolAggregationDAO(database);
        // Vols entre deux dates
        System.out.println("=== Vols entre deux dates ===");
        List<Document> volsEntreDates = vad.getVolsEntreDates("2025-01-01", "2025-06-1");
        //volsEntreDates.forEach(doc -> System.out.println(doc.toJson()));



        // Fermeture de la connexion
        client.close();
    }
}