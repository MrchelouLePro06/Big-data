package projet.bd.src;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.mongodb.client.model.*;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class PiloteAggregationDAO {
    private final MongoCollection<Document> collection;

    public PiloteAggregationDAO(MongoDatabase database) {
        this.collection = database.getCollection("pilotes");
    }

    // 1. Statistiques des vols par pilote
    public List<Document> statsVolsParPilote() {
        List<Bson> pipeline = Arrays.asList(
                Aggregates.lookup("vols", "_id", "pilote._id", "vols"),
                Aggregates.project(Projections.fields(
                        Projections.include("nom", "prenom"),
                        Projections.computed("nombre_vols", new Document("$size", "$vols")),
                        Projections.computed("heures_vol", new Document("$sum", "$vols.duree"))
                )),
                Aggregates.sort(Sorts.descending("nombre_vols"))
        );
        return collection.aggregate(pipeline).into(new ArrayList<>());
    }

    // 2. Pilotes par compagnie et expérience
    public List<Document> pilotesParCompagnieExperience() {
        List<Bson> pipeline = Arrays.asList(
                Aggregates.project(Projections.fields(
                        Projections.include("nom", "prenom", "compagnie"),
                        Projections.computed("annees_experience",
                                new Document("$dateDiff", new Document()
                                        .append("startDate", "$date_embauche")
                                        .append("endDate", "$$NOW")
                                        .append("unit", "year"))
                        )
                )),
                Aggregates.group("$compagnie._id",
                        Accumulators.push("pilotes", "$$ROOT"),
                        Accumulators.avg("experience_moyenne", "$annees_experience")
                )
        );
        return collection.aggregate(pipeline).into(new ArrayList<>());
    }

    // 3. Routes les plus fréquentes par pilote
    public List<Document> routesFrequentesParPilote() {
        List<Bson> pipeline = Arrays.asList(
                Aggregates.lookup("vols", "_id", "pilote._id", "vols"),
                Aggregates.unwind("$vols"),
                Aggregates.group(new Document("pilote", "$_id")
                                .append("route", new Document()
                                        .append("depart", "$vols.aeroport_depart._id")
                                        .append("arrivee", "$vols.aeroport_arrivee._id")),
                        Accumulators.sum("frequence", 1)
                ),
                Aggregates.sort(Sorts.descending("frequence"))
        );
        return collection.aggregate(pipeline).into(new ArrayList<>());
    }

    // 4. Performance des pilotes (retards et incidents)
    public List<Document> performancePilotes() {
        List<Bson> pipeline = Arrays.asList(
                Aggregates.lookup("vols", "_id", "pilote._id", "vols"),
                Aggregates.project(Projections.fields(
                        Projections.include("nom", "prenom"),
                        Projections.computed("vols_retardes",
                                new Document("$size",
                                        new Document("$filter", new Document()
                                                .append("input", "$vols")
                                                .append("as", "vol")
                                                .append("cond", new Document("$eq", Arrays.asList("$$vol.statut", "Delayed")))
                                        )
                                )
                        ),
                        Projections.computed("incidents",
                                new Document("$size",
                                        new Document("$filter", new Document()
                                                .append("input", "$vols")
                                                .append("as", "vol")
                                                .append("cond", new Document("$eq", Arrays.asList("$$vol.statut", "Incident")))
                                        )
                                )
                        )
                ))
        );
        return collection.aggregate(pipeline).into(new ArrayList<>());
    }

    // 5. Disponibilité des pilotes
    public List<Document> disponibilitePilotes(String date) {
        List<Bson> pipeline = Arrays.asList(
                Aggregates.lookup("vols", "_id", "pilote._id", "vols"),
                Aggregates.match(Filters.eq("vols.date_depart", date)),
                Aggregates.project(Projections.fields(
                        Projections.include("nom", "prenom"),
                        Projections.computed("disponible", new Document("$cond", Arrays.asList(
                                new Document("$eq", Arrays.asList(new Document("$size", "$vols"), 0)),
                                true,
                                false
                        )))
                ))
        );
        return collection.aggregate(pipeline).into(new ArrayList<>());
    }
}