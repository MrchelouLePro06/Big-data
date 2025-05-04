package projet.bd.src;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.mongodb.client.model.*;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class AeroportAggregationDAO {
    private final MongoCollection<Document> collection;

    public AeroportAggregationDAO(MongoDatabase database) {
        this.collection = database.getCollection("aeroports");
    }

    // 1. Nombre de vols par aéroport
    public List<Document> countVolsParAeroport() {
        List<Bson> pipeline = Arrays.asList(
                Aggregates.lookup("vols", "_id", "aeroport_depart._id", "vols_depart"),
                Aggregates.lookup("vols", "_id", "aeroport_arrivee._id", "vols_arrivee"),
                Aggregates.project(Projections.fields(
                        Projections.include("nom"),
                        Projections.computed("total_vols",
                                new Document("$add", Arrays.asList(
                                        new Document("$size", "$vols_depart"),
                                        new Document("$size", "$vols_arrivee")
                                ))
                        )
                )),
                Aggregates.sort(Sorts.descending("total_vols"))
        );
        return collection.aggregate(pipeline).into(new ArrayList<>());
    }

    // 2. Aéroports les plus fréquentés par période
    public List<Document> aeroportsFrequentes(String dateDebut, String dateFin) {
        List<Bson> pipeline = Arrays.asList(
                Aggregates.lookup("vols", "_id", "aeroport_depart._id", "vols"),
                Aggregates.match(Filters.and(
                        Filters.gte("vols.date_depart", dateDebut),
                        Filters.lte("vols.date_depart", dateFin)
                )),
                Aggregates.group("$_id",
                        Accumulators.first("nom", "$nom"),
                        Accumulators.sum("nombre_vols", new Document("$size", "$vols"))
                ),
                Aggregates.sort(Sorts.descending("nombre_vols"))
        );
        return collection.aggregate(pipeline).into(new ArrayList<>());
    }

    // 3. Connexions entre aéroports
    public List<Document> connexionsAeroports() {
        List<Bson> pipeline = Arrays.asList(
                Aggregates.lookup("vols", "_id", "aeroport_depart._id", "vols"),
                Aggregates.unwind("$vols"),
                Aggregates.group(new Document("depart", "$_id")
                                .append("arrivee", "$vols.aeroport_arrivee._id"),
                        Accumulators.sum("nombre_vols", 1)
                ),
                Aggregates.sort(Sorts.descending("nombre_vols"))
        );
        return collection.aggregate(pipeline).into(new ArrayList<>());
    }

    // 4. Statistiques des retards par aéroport
    public List<Document> statsRetardsParAeroport() {
        List<Bson> pipeline = Arrays.asList(
                Aggregates.lookup("vols", "_id", "aeroport_depart._id", "vols"),
                Aggregates.match(Filters.eq("vols.statut", "Delayed")),
                Aggregates.group("$_id",
                        Accumulators.first("nom", "$nom"),
                        Accumulators.sum("total_retards", 1),
                        Accumulators.avg("retard_moyen", "$vols.duree_retard")
                ),
                Aggregates.sort(Sorts.descending("total_retards"))
        );
        return collection.aggregate(pipeline).into(new ArrayList<>());
    }

    // 5. Aéroports par compagnie
    public List<Document> aeroportsParCompagnie() {
        List<Bson> pipeline = Arrays.asList(
                Aggregates.lookup("vols", "_id", "aeroport_depart._id", "vols"),
                Aggregates.unwind("$vols"),
                Aggregates.group(new Document("aeroport", "$_id")
                                .append("compagnie", "$vols.compagnie._id"),
                        Accumulators.sum("nombre_vols", 1)
                ),
                Aggregates.sort(Sorts.descending("nombre_vols"))
        );
        return collection.aggregate(pipeline).into(new ArrayList<>());
    }
}