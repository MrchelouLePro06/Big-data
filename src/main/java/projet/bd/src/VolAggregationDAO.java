package projet.bd.src;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Projections;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class VolAggregationDAO {
    private final MongoCollection<Document> collection;

    public VolAggregationDAO(MongoDatabase database) {
        this.collection = database.getCollection("vols");
    }

    // 1. Vols entre deux dates
    public List<Document> getVolsEntreDates(String start, String end) {
        List<Bson> pipeline = Arrays.asList(
                Aggregates.match(Filters.and(
                        Filters.gte("date_depart", start),
                        Filters.lte("date_depart", end)
                ))
        );
        return collection.aggregate(pipeline).into(new ArrayList<>());
    }

    // 2. Nombre de vols par compagnie
    public List<Document> countVolsParCompagnie() {
        List<Bson> pipeline = Arrays.asList(
                Aggregates.group("$compagnie.nom", Accumulators.sum("total", 1)),
                Aggregates.sort(Sorts.descending("total"))
        );
        return collection.aggregate(pipeline).into(new ArrayList<>());
    }

    // 3. Nombre de vols par ville de départ
    public List<Document> countVolsParVilleDepart() {
        List<Bson> pipeline = Arrays.asList(
                Aggregates.group("$aeroport_depart.ville", Accumulators.sum("nbVols", 1)),
                Aggregates.sort(Sorts.descending("nbVols"))
        );
        return collection.aggregate(pipeline).into(new ArrayList<>());
    }

    // 4. Vols avec détails du pilote (via jointure)
    public List<Document> getVolsAvecPilote() {
        List<Bson> pipeline = Arrays.asList(
                Aggregates.lookup("pilotes", "equipage.commandant.id", "_id", "commandant"),
                Aggregates.unwind("$commandant")
        );
        return collection.aggregate(pipeline).into(new ArrayList<>());
    }

    // 5. Moyenne de durée des vols par avion
    public List<Document> moyenneDureeParAvion() {
        List<Bson> pipeline = Arrays.asList(
                Aggregates.project(Projections.fields(
                        Projections.include("avion.id", "date_depart", "date_arrivee"),
                        Projections.computed("duree", new Document("$subtract", Arrays.asList(
                                new Document("$toDate", "$date_arrivee"),
                                new Document("$toDate", "$date_depart")
                        )))
                )),
                Aggregates.group("$avion.id", Accumulators.avg("moyenne_duree", "$duree"))
        );
        return collection.aggregate(pipeline).into(new ArrayList<>());
    }

    // 6. Derniers vols pour une compagnie
    public List<Document> derniersVolsParCompagnie(String compagnieId, int limit) {
        List<Bson> pipeline = Arrays.asList(
                Aggregates.match(Filters.eq("compagnie.id", compagnieId)),
                Aggregates.sort(Sorts.descending("date_depart")),
                Aggregates.limit(limit)
        );
        return collection.aggregate(pipeline).into(new ArrayList<>());
    }
}
