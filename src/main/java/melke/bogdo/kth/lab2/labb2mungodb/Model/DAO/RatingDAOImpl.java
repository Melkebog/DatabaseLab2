package melke.bogdo.kth.lab2.labb2mungodb.Model.DAO;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import melke.bogdo.kth.lab2.labb2mungodb.Model.Rating;
import melke.bogdo.kth.lab2.labb2mungodb.Model.DAO.Interface.RatingDAO;
import melke.bogdo.kth.lab2.labb2mungodb.Model.DatabaseConnection;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

/**
 * Implementation of the {@link RatingDAO} interface for managing ratings in MongoDB.
 * This class allows adding ratings to books and retrieving ratings associated with a specific book.
 */
public class RatingDAOImpl implements RatingDAO {

    private final MongoCollection<Document> booksCollection;

    /**
     * Constructs a new {@code RatingDAOImpl} and initializes the connection to the "books" collection.
     */
    public RatingDAOImpl() {
        MongoDatabase database = DatabaseConnection.getDatabase();
        this.booksCollection = database.getCollection("books");
    }

    /**
     * Adds a new rating to a specific book.
     *
     * @param bookId the unique ID of the book to which the rating will be added.
     * @param rating the {@link Rating} object containing the rating details.
     * @throws IllegalArgumentException if {@code bookId} or {@code rating} is null.
     */
    @Override
    public void addRatingToBook(String bookId, Rating rating) {
        if (bookId == null || rating == null) {
            throw new IllegalArgumentException("Book ID and rating cannot be null.");
        }

        Document ratingDoc = new Document("user_id", rating.getUserId())
                .append("rating", rating.getRating());

        booksCollection.updateOne(
                eq("_id", bookId),
                new Document("$push", new Document("ratings", ratingDoc))
        );
    }

    /**
     * Retrieves all ratings associated with a specific book.
     *
     * @param bookId the unique ID of the book.
     * @return a {@link List} of {@link Rating} objects representing the ratings of the book,
     *         or an empty list if no ratings are found.
     * @throws IllegalArgumentException if {@code bookId} is null.
     */
    @Override
    public List<Rating> getRatingsByBookId(String bookId) {
        if (bookId == null) {
            throw new IllegalArgumentException("Book ID cannot be null.");
        }

        Document book = booksCollection.find(eq("_id", bookId)).first();
        if (book != null) {
            List<Document> ratingDocs = book.getList("ratings", Document.class);
            List<Rating> ratings = new ArrayList<>();
            if (ratingDocs != null) {
                for (Document doc : ratingDocs) {
                    ratings.add(new Rating(
                            doc.getString("user_id"),
                            doc.getInteger("rating")
                    ));
                }
            }
            return ratings;
        }
        return new ArrayList<>();
    }
}
