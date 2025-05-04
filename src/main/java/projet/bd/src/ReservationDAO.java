package projet.bd.src;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import java.util.List;

public class ReservationDAO {
    private final MongoCollection<Document> collection;

    public ReservationDAO(MongoDatabase database) {
        this.collection = database.getCollection("reservations");
    }

    public void insertReservation(Reservation reservation) {
        collection.insertOne(reservation.toDocument());
    }

    public void insertReservations(List<Reservation> reservations) {
        List<Document> documents = reservations.stream()
                .map(Reservation::toDocument)
                .toList();
        collection.insertMany(documents);
    }

    public void updateReservation(int pnr, String field, Object newValue) {
        collection.updateOne(Filters.eq("pnr", pnr), Updates.set(field, newValue));
    }

    public void deleteReservation(int pnr) {
        collection.deleteOne(Filters.eq("pnr", pnr));
    }
}