package melke.bogdo.kth.lab2.labb2mungodb.Model.DAO;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import melke.bogdo.kth.lab2.labb2mungodb.Model.Review;
import melke.bogdo.kth.lab2.labb2mungodb.Model.DAO.Interface.ReviewDAO;
import melke.bogdo.kth.lab2.labb2mungodb.Model.DatabaseConnection;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

/**
 * Implementation of the {@link ReviewDAO} interface for managing reviews in MongoDB.
 * This class allows adding and retrieving reviews associated with specific books.
 */
public class ReviewDAOImpl implements ReviewDAO {

    private final MongoCollection<Document> booksCollection;

    /**
     * Constructs a new {@code ReviewDAOImpl} and initializes the connection to the "books" collection.
     */
    public ReviewDAOImpl() {
        MongoDatabase database = DatabaseConnection.getDatabase();
        this.booksCollection = database.getCollection("books");
    }

    /**
     * Adds a review to a specific book.
     * If a review by the same user already exists, it updates the existing review.
     * Otherwise, a new review is added to the book.
     *
     * @param bookId the unique ID of the book to add the review to.
     * @param review the {@link Review} object containing the review details.
     * @return {@code true} if the review was successfully added or updated, {@code false} otherwise.
     */
    @Override
    public boolean addReviewToBook(String bookId, Review review) {
        try {
            ObjectId bookObjectId = new ObjectId(bookId);

            // Attempt to update an existing review by matching user_id
            long updatedCount = booksCollection.updateOne(
                    eq("_id", bookObjectId),
                    new Document("$set", new Document("reviews.$[userReview].review_text", review.getReviewText())
                            .append("reviews.$[userReview].review_date", review.getReviewDate().toString())),
                    new com.mongodb.client.model.UpdateOptions().arrayFilters(
                            List.of(new Document("userReview.user_id", review.getUserId()))
                    )
            ).getModifiedCount();

            // If no existing review was updated, add a new review
            if (updatedCount == 0) {
                booksCollection.updateOne(
                        eq("_id", bookObjectId),
                        new Document("$push", new Document("reviews", new Document("user_id", review.getUserId())
                                .append("review_text", review.getReviewText())
                                .append("review_date", review.getReviewDate().toString())
                                .append("username", review.getUsername())))
                );
            }

            return true; //  success
        } catch (Exception e) {
            System.err.println("Error in addReviewToBook: " + e.getMessage());
            e.printStackTrace();
            return false; //  failure
        }
    }

    /**
     * Retrieves all reviews associated with a specific book.
     *
     * @param bookId the unique ID of the book.
     * @return a {@link List} of {@link Review} objects representing the reviews of the book,
     *         or an empty list if no reviews are found.
     * @throws IllegalArgumentException if {@code bookId} is null or invalid.
     */
    @Override
    public List<Review> getReviewsByBookId(String bookId) {
        if (bookId == null || !ObjectId.isValid(bookId)) {
            throw new IllegalArgumentException("Invalid book ID.");
        }

        // Ensure bookId is converted to ObjectId
        ObjectId bookObjectId = new ObjectId(bookId);

        // Fetch the book document
        Document book = booksCollection.find(eq("_id", bookObjectId)).first();
        if (book != null) {
            // Retrieve and parse the reviews array
            List<Document> reviewDocs = book.getList("reviews", Document.class);
            List<Review> reviews = new ArrayList<>();
            if (reviewDocs != null) {
                for (Document doc : reviewDocs) {
                    reviews.add(new Review(
                            doc.getString("user_id"),
                            doc.getString("review_text"),
                            LocalDate.parse(doc.getString("review_date")),
                            doc.getString("username")
                    ));
                }
            }
            return reviews;
        }
        return new ArrayList<>();
    }
}
