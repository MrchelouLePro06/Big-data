package projet.bd.src;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.mongodb.client.model.*;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class ReservationAggregationDAO {
    private final MongoCollection<Document> collection;

    public ReservationAggregationDAO(MongoDatabase database) {
        this.collection = database.getCollection("reservations");
    }

    // 1. Statistiques des réservations par vol
    public List<Document> statsReservationsParVol() {
        List<Bson> pipeline = Arrays.asList(
                Aggregates.lookup("vols", "vol_id", "_id", "vol"),
                Aggregates.unwind("$vol"),
                Aggregates.group("$vol_id",
                        Accumulators.first("numero_vol", "$vol.numero_vol"),
                        Accumulators.sum("nombre_reservations", 1),
                        Accumulators.avg("prix_moyen", "$prix")
                ),
                Aggregates.sort(Sorts.descending("nombre_reservations"))
        );
        return collection.aggregate(pipeline).into(new ArrayList<>());
    }

    // 2. Répartition des classes de réservation
    public List<Document> repartitionClassesReservation() {
        List<Bson> pipeline = Arrays.asList(
                Aggregates.group("$classe",
                        Accumulators.sum("total", 1),
                        Accumulators.avg("prix_moyen", "$prix")
                ),
                Aggregates.project(Projections.fields(
                        Projections.include("total", "prix_moyen"),
                        Projections.computed("pourcentage", new Document("$multiply", Arrays.asList(
                                new Document("$divide", Arrays.asList("$total", new Document("$sum", "$total"))),
                                100
                        )))
                ))
        );
        return collection.aggregate(pipeline).into(new ArrayList<>());
    }

    // 3. Historique des réservations par passager
    public List<Document> historiqueReservationsPassager() {
        List<Bson> pipeline = Arrays.asList(
                Aggregates.lookup("passagers", "passager_id", "_id", "passager"),
                Aggregates.unwind("$passager"),
                Aggregates.group("$passager_id",
                        Accumulators.first("nom", "$passager.nom"),
                        Accumulators.first("prenom", "$passager.prenom"),
                        Accumulators.push("reservations", new Document()
                                .append("vol_id", "$vol_id")
                                .append("date", "$date_reservation")
                                .append("classe", "$classe")
                                .append("prix", "$prix")
                        ),
                        Accumulators.sum("total_depense", "$prix")
                ),
                Aggregates.sort(Sorts.descending("total_depense"))
        );
        return collection.aggregate(pipeline).into(new ArrayList<>());
    }

    // 4. Analyse des revenus par période
    public List<Document> analyseRevenusParPeriode(String dateDebut, String dateFin) {
        List<Bson> pipeline = Arrays.asList(
                Aggregates.match(Filters.and(
                        Filters.gte("date_reservation", dateDebut),
                        Filters.lte("date_reservation", dateFin)
                )),
                Aggregates.group(new Document("$dateToString", new Document()
                                .append("format", "%Y-%m")
                                .append("date", new Document("$toDate", "$date_reservation"))),
                        Accumulators.sum("revenu_total", "$prix"),
                        Accumulators.avg("prix_moyen", "$prix"),
                        Accumulators.sum("nombre_reservations", 1)
                ),
                Aggregates.sort(Sorts.ascending("_id"))
        );
        return collection.aggregate(pipeline).into(new ArrayList<>());
    }

    // 5. Top des destinations les plus réservées
    public List<Document> topDestinationsReservees() {
        List<Bson> pipeline = Arrays.asList(
                Aggregates.lookup("vols", "vol_id", "_id", "vol"),
                Aggregates.unwind("$vol"),
                Aggregates.group("$vol.aeroport_arrivee._id",
                        Accumulators.first("ville", "$vol.aeroport_arrivee.ville"),
                        Accumulators.sum("nombre_reservations", 1),
                        Accumulators.avg("prix_moyen", "$prix")
                ),
                Aggregates.sort(Sorts.descending("nombre_reservations")),
                Aggregates.limit(10)
        );
        return collection.aggregate(pipeline).into(new ArrayList<>());
    }
}