package projet.bd.src;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.mongodb.client.model.*;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class AvionAggregationDAO {
    private final MongoCollection<Document> collection;

    public AvionAggregationDAO(MongoDatabase database) {
        this.collection = database.getCollection("avions");
    }

    // 1. Statistiques d'utilisation par avion
    public List<Document> statsUtilisationAvion() {
        List<Bson> pipeline = Arrays.asList(
                Aggregates.lookup("vols", "_id", "avion._id", "vols"),
                Aggregates.project(Projections.fields(
                        Projections.include("modele"),
                        Projections.computed("nombre_vols", new Document("$size", "$vols")),
                        Projections.computed("heures_vol", new Document("$sum", "$vols.duree"))
                )),
                Aggregates.sort(Sorts.descending("nombre_vols"))
        );
        return collection.aggregate(pipeline).into(new ArrayList<>());
    }

    // 2. Maintenance par modèle d'avion
    public List<Document> maintenanceParModele() {
        List<Bson> pipeline = Arrays.asList(
                Aggregates.lookup("maintenances", "_id", "avion_id", "maintenances"),
                Aggregates.group("$modele",
                        Accumulators.sum("total_maintenances", new Document("$size", "$maintenances")),
                        Accumulators.avg("cout_moyen", "$maintenances.cout")
                ),
                Aggregates.sort(Sorts.descending("total_maintenances"))
        );
        return collection.aggregate(pipeline).into(new ArrayList<>());
    }

    // 3. Performance des avions par route
    public List<Document> performanceParRoute() {
        List<Bson> pipeline = Arrays.asList(
                Aggregates.lookup("vols", "_id", "avion._id", "vols"),
                Aggregates.unwind("$vols"),
                Aggregates.group(new Document("avion", "$_id")
                                .append("route", new Document()
                                        .append("depart", "$vols.aeroport_depart._id")
                                        .append("arrivee", "$vols.aeroport_arrivee._id")),
                        Accumulators.avg("temps_moyen", "$vols.duree"),
                        Accumulators.sum("nombre_vols", 1)
                )
        );
        return collection.aggregate(pipeline).into(new ArrayList<>());
    }

    // 4. Disponibilité des avions
    public List<Document> disponibiliteAvions(String date) {
        List<Bson> pipeline = Arrays.asList(
                Aggregates.lookup("vols", "_id", "avion._id", "vols"),
                Aggregates.match(Filters.eq("vols.date_depart", date)),
                Aggregates.project(Projections.fields(
                        Projections.include("modele"),
                        Projections.computed("disponible", new Document("$cond", Arrays.asList(
                                new Document("$eq", Arrays.asList(new Document("$size", "$vols"), 0)),
                                true,
                                false
                        )))
                ))
        );
        return collection.aggregate(pipeline).into(new ArrayList<>());
    }

    // 5. Capacité totale par compagnie
    public List<Document> capaciteTotaleParCompagnie() {
        List<Bson> pipeline = Arrays.asList(
                Aggregates.group("$compagnie._id",
                        Accumulators.sum("capacite_totale", "$nombre_places"),
                        Accumulators.sum("nombre_avions", 1)
                ),
                Aggregates.sort(Sorts.descending("capacite_totale"))
        );
        return collection.aggregate(pipeline).into(new ArrayList<>());
    }
}