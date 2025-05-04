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
                Aggregates.lookup("vols", "id", "equipage.commandant.id", "vols_commandant"),
                Aggregates.lookup("vols", "id", "equipage.opl.id", "vols_opl"),
                Aggregates.project(Projections.fields(
                        Projections.include("nom"),
                        Projections.computed("nombre_vols_total",
                                new Document("$add", Arrays.asList(
                                        new Document("$size", "$vols_commandant"),
                                        new Document("$size", "$vols_opl")
                                ))
                        )
                )),
                Aggregates.sort(Sorts.descending("nombre_vols_total"))
        );
        return collection.aggregate(pipeline).into(new ArrayList<>());
    }

    // 2. Routes fréquentes par pilote
    public List<Document> routesFrequentesParPilote() {
        List<Bson> pipeline = Arrays.asList(
                Aggregates.lookup("vols", "id", "equipage.commandant.id", "vols"),
                Aggregates.unwind("$vols"),
                Aggregates.group(new Document("pilote", "$id")
                                .append("route", new Document()
                                        .append("depart", "$vols.aeroport_depart.id")
                                        .append("arrivee", "$vols.aeroport_arrivee.id")),
                        Accumulators.sum("frequence", 1)),
                Aggregates.sort(Sorts.descending("frequence"))
        );
        return collection.aggregate(pipeline).into(new ArrayList<>());
    }

    // 3. Performances des pilotes (statuts des vols)
    public List<Document> performancePilotes() {
        List<Bson> pipeline = Arrays.asList(
                Aggregates.lookup("vols", "id", "equipage.commandant.id", "vols"),
                Aggregates.project(Projections.fields(
                        Projections.include("nom"),
                        Projections.computed("vols_total", new Document("$size", "$vols")),
                        Projections.computed("vols_partis",
                                new Document("$size", new Document("$filter", new Document()
                                        .append("input", "$vols")
                                        .append("as", "vol")
                                        .append("cond", new Document("$eq", Arrays.asList("$$vol.statut", "Parti")))
                                ))
                        )
                ))
        );
        return collection.aggregate(pipeline).into(new ArrayList<>());
    }
}