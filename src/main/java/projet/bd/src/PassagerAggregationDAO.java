package projet.bd.src;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.mongodb.client.model.*;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class PassagerAggregationDAO {
    private final MongoCollection<Document> collection;

    public PassagerAggregationDAO(MongoDatabase database) {
        this.collection = database.getCollection("passagers");
    }

    // 1. Nombre de réservations par passager
    public List<Document> countReservationsParPassager() {
        List<Bson> pipeline = Arrays.asList(
                Aggregates.lookup("reservations", "_id", "passager_id", "reservations"),
                Aggregates.project(Projections.fields(
                        Projections.include("nom", "prenom"),
                        Projections.computed("nombre_reservations",
                                new Document("$size", "$reservations"))
                )),
                Aggregates.sort(Sorts.descending("nombre_reservations"))
        );
        return collection.aggregate(pipeline).into(new ArrayList<>());
    }

    // 2. Passagers par tranche d'âge
    public List<Document> passagersParTrancheAge() {
        List<Bson> pipeline = Arrays.asList(
                Aggregates.project(Projections.fields(
                        Projections.include("nom", "prenom"),
                        Projections.computed("age",
                                new Document("$dateDiff",
                                        new Document("startDate", "$date_naissance")
                                                .append("endDate", "$$NOW")
                                                .append("unit", "year")))
                )),
                Aggregates.bucket("$age",
                        Arrays.asList(0, 18, 30, 50, 70, 100),
                        new BucketOptions()
                                .defaultBucket("70+")
                                .output(Accumulators.sum("count", 1),
                                        Accumulators.push("passagers", "$$ROOT"))
                )
        );
        return collection.aggregate(pipeline).into(new ArrayList<>());
    }

    // 3. Top des passagers fréquents
    public List<Document> topPassagersFrequents(int limit) {
        List<Bson> pipeline = Arrays.asList(
                Aggregates.lookup("reservations", "_id", "passager_id", "reservations"),
                Aggregates.project(Projections.fields(
                        Projections.include("nom", "prenom"),
                        Projections.computed("total_vols", new Document("$size", "$reservations"))
                )),
                Aggregates.sort(Sorts.descending("total_vols")),
                Aggregates.limit(limit)
        );
        return collection.aggregate(pipeline).into(new ArrayList<>());
    }

    // 4. Passagers avec leurs vols récents
    public List<Document> passagersAvecVolsRecents() {
        List<Bson> pipeline = Arrays.asList(
                Aggregates.lookup("reservations", "_id", "passager_id", "reservations"),
                Aggregates.unwind("$reservations"),
                Aggregates.lookup("vols", "reservations.vol_id", "_id", "vol"),
                Aggregates.sort(Sorts.descending("vol.date_depart")),
                Aggregates.group("$_id",
                        Accumulators.first("nom", "$nom"),
                        Accumulators.first("prenom", "$prenom"),
                        Accumulators.push("derniers_vols", "$vol")
                )
        );
        return collection.aggregate(pipeline).into(new ArrayList<>());
    }

    // 5. Statistiques des réservations par pays
    public List<Document> statsReservationsParPays() {
        List<Bson> pipeline = Arrays.asList(
                Aggregates.group("$pays",
                        Accumulators.sum("total_passagers", 1),
                        Accumulators.avg("age_moyen", "$age")
                ),
                Aggregates.sort(Sorts.descending("total_passagers"))
        );
        return collection.aggregate(pipeline).into(new ArrayList<>());
    }
}