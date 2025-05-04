package projet.bd.src;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.mongodb.client.model.*;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class CompagnieAggregationDAO {
    private final MongoCollection<Document> collection;

    public CompagnieAggregationDAO(MongoDatabase database) {
        this.collection = database.getCollection("compagnies");
    }

    // 1. Statistiques des vols par compagnie
    public List<Document> statsVolsParCompagnie() {
        List<Bson> pipeline = Arrays.asList(
                Aggregates.lookup("vols", "_id", "compagnie._id", "vols"),
                Aggregates.project(Projections.fields(
                        Projections.include("nom"),
                        Projections.computed("total_vols", new Document("$size", "$vols")),
                        Projections.computed("vols_retard",
                                new Document("$size", new Document("$filter",
                                        new Document("input", "$vols")
                                                .append("as", "vol")
                                                .append("cond", new Document("$eq", Arrays.asList("$$vol.statut", "Delayed")))
                                ))
                        )
                ))
        );
        return collection.aggregate(pipeline).into(new ArrayList<>());
    }

    // 2. Flotte d'avions par compagnie
    public List<Document> flotteParCompagnie() {
        List<Bson> pipeline = Arrays.asList(
                Aggregates.lookup("avions", "_id", "compagnie_id", "avions"),
                Aggregates.project(Projections.fields(
                        Projections.include("nom"),
                        Projections.computed("nombre_avions", new Document("$size", "$avions")),
                        Projections.computed("modeles", "$avions.modele")
                ))
        );
        return collection.aggregate(pipeline).into(new ArrayList<>());
    }

    // 3. Destinations populaires par compagnie
    public List<Document> destinationsPopulaires() {
        List<Bson> pipeline = Arrays.asList(
                Aggregates.lookup("vols", "_id", "compagnie._id", "vols"),
                Aggregates.unwind("$vols"),
                Aggregates.group(new Document("compagnie", "$_id")
                                .append("destination", "$vols.aeroport_arrivee._id"),
                        Accumulators.sum("nombre_vols", 1)
                ),
                Aggregates.sort(Sorts.descending("nombre_vols"))
        );
        return collection.aggregate(pipeline).into(new ArrayList<>());
    }

    // 4. Performance des pilotes par compagnie
    public List<Document> performancePilotes() {
        List<Bson> pipeline = Arrays.asList(
                Aggregates.lookup("pilotes", "_id", "compagnie_id", "pilotes"),
                Aggregates.unwind("$pilotes"),
                Aggregates.lookup("vols", "pilotes._id", "pilote._id", "vols_pilote"),
                Aggregates.project(Projections.fields(
                        Projections.include("nom"),
                        Projections.include("pilotes"),
                        Projections.computed("nombre_vols", new Document("$size", "$vols_pilote"))
                ))
        );
        return collection.aggregate(pipeline).into(new ArrayList<>());
    }

    // 5. Revenus par route
    public List<Document> revenusParRoute() {
        List<Bson> pipeline = Arrays.asList(
                Aggregates.lookup("vols", "_id", "compagnie._id", "vols"),
                Aggregates.unwind("$vols"),
                Aggregates.group(new Document("compagnie", "$_id")
                                .append("route", new Document("depart", "$vols.aeroport_depart._id")
                                        .append("arrivee", "$vols.aeroport_arrivee._id")),
                        Accumulators.sum("revenu_total", "$vols.prix_moyen")
                ),
                Aggregates.sort(Sorts.descending("revenu_total"))
        );
        return collection.aggregate(pipeline).into(new ArrayList<>());
    }
}