package melke.bogdo.kth.lab2.labb2mungodb.Model.DAO;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import melke.bogdo.kth.lab2.labb2mungodb.Model.*;
import melke.bogdo.kth.lab2.labb2mungodb.Model.DAO.Interface.BookDAO;
import melke.bogdo.kth.lab2.labb2mungodb.Model.DatabaseConnection;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

/**
 * Implementation of the {@link BookDAO} interface for managing book-related operations in MongoDB.
 * <p>
 * This class provides methods for CRUD operations on books and handles embedded entities such as authors, reviews, and ratings.
 * </p>
 */
public class BookDAOImpl implements BookDAO {

    /**
     * The collection of books in the MongoDB database.
     * Each document in this collection represents a book with its related data.
     */
    private final MongoCollection<Document> booksCollection;

    /**
     * Constructs a new {@code BookDAOImpl} instance and initializes the connection to the "books" collection.
     * The connection is established via the {@link DatabaseConnection} class.
     */
    public BookDAOImpl() {
        MongoDatabase database = DatabaseConnection.getDatabase();
        this.booksCollection = database.getCollection("books");
    }

    /**
     * Retrieves all books stored in the database.
     *
     * @return a {@link List} of {@link Book} objects representing all books in the database.
     */
    @Override
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        for (Document doc : booksCollection.find()) {
            books.add(mapDocumentToBook(doc));
        }
        return books;
    }

    /**
     * Searches for books in the database based on a specific type and keyword.
     *
     * @param type    the type of search (e.g., "title", "author", "genre", "isbn").
     *                Determines which field of the book to search in.
     * @param keyword the search keyword to match against the specified type.
     * @return a {@link List} of {@link Book} objects that match the search criteria.
     * @throws IllegalArgumentException if {@code type} is null or empty, or if {@code keyword} is null or empty.
     */
    @Override
    public List<Book> searchBooks(String type, String keyword) {
        if (type == null || keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Search type and keyword must be provided.");
        }

        List<Book> results = new ArrayList<>();
        Bson filter;

        switch (type.toLowerCase()) {
            case "title":
                filter = Filters.regex("title", ".*" + keyword + ".*", "i");
                break;
            case "author":
                filter = Filters.elemMatch("authors", Filters.regex("name", ".*" + keyword + ".*", "i"));
                break;
            case "genre":
                filter = Filters.regex("genre.name", ".*" + keyword + ".*", "i");
                break;
            case "isbn":
                filter = Filters.regex("isbn", ".*" + keyword + ".*", "i");
                break;
            default:
                throw new IllegalArgumentException("Invalid search type: " + type);
        }

        for (Document doc : booksCollection.find(filter)) {
            results.add(mapDocumentToBook(doc));
        }

        return results;
    }

    /**
     * Adds a new book to the database.
     * <p>
     * Constructs a MongoDB document from the {@link Book} object and inserts it into the "books" collection.
     * </p>
     *
     * @param book the {@link Book} object to be added.
     * @throws IllegalStateException if the generated ID for the inserted book cannot be retrieved.
     */
    @Override
    public void addBook(Book book) {
        Document bookDoc = new Document("title", book.getTitle())
                .append("isbn", book.getIsbn())
                .append("genre", new Document("id", book.getGenre().getId()).append("name", book.getGenre().getName()))
                .append("authors", book.getAuthors().stream()
                        .map(author -> new Document("name", author.getName())
                                .append("birthdate", author.getBirthdate())
                                .append("user_id", author.getUserId()))
                        .toList())
                .append("reviews", new ArrayList<>())
                .append("ratings", new ArrayList<>())
                .append("user_id", book.getUserId());

        booksCollection.insertOne(bookDoc);

        ObjectId generatedId = bookDoc.getObjectId("_id");
        if (generatedId != null) {
            book.setId(generatedId.toString());
        } else {
            throw new IllegalStateException("Failed to retrieve generated _id for the inserted book.");
        }
    }

    /**
     * Deletes a book from the database by its unique ID.
     *
     * @param bookId the unique ID of the book to delete.
     * @throws IllegalArgumentException if {@code bookId} is not a valid ObjectId.
     */
    @Override
    public void deleteBook(String bookId) {
        if (!ObjectId.isValid(bookId)) {
            throw new IllegalArgumentException("Invalid ObjectId: " + bookId);
        }
        booksCollection.deleteOne(eq("_id", new ObjectId(bookId)));
    }

    /**
     * Adds or updates a review for a specific book.
     * <p>
     * If a review by the same user exists, it updates the review. Otherwise, it adds a new review.
     * </p>
     *
     * @param bookId the unique ID of the book.
     * @param review the {@link Review} object containing the review details.
     */
    @Override
    public void addReview(String bookId, Review review) {
        ObjectId bookObjectId = new ObjectId(bookId);

        long modifiedCount = booksCollection.updateOne(
                Filters.and(eq("_id", bookObjectId), Filters.elemMatch("reviews", eq("user_id", review.getUserId()))),
                new Document("$set", new Document("reviews.$.review_text", review.getReviewText())
                        .append("reviews.$.review_date", review.getReviewDate().toString()))
        ).getModifiedCount();

        if (modifiedCount == 0) {
            booksCollection.updateOne(
                    eq("_id", bookObjectId),
                    new Document("$push", new Document("reviews", new Document("user_id", review.getUserId())
                            .append("review_text", review.getReviewText())
                            .append("review_date", review.getReviewDate().toString())
                            .append("username", review.getUsername())))
            );
        }
    }

    /**
     * Adds or updates a rating for a specific book.
     * <p>
     * If a rating by the user exists, it updates the rating. Otherwise, it adds a new rating.
     * </p>
     *
     * @param bookId the unique ID of the book.
     * @param rating the {@link Rating} object containing the rating details.
     */
    @Override
    public void addRating(String bookId, Rating rating) {
        ObjectId bookObjectId = new ObjectId(bookId);

        booksCollection.updateOne(
                Filters.and(eq("_id", bookObjectId), Filters.elemMatch("ratings", eq("user_id", rating.getUserId()))),
                new Document("$set", new Document("ratings.$.rating", rating.getRating()))
        );

        booksCollection.updateOne(
                eq("_id", bookObjectId),
                new Document("$addToSet", new Document("ratings", new Document("user_id", rating.getUserId()).append("rating", rating.getRating())))
        );
    }

    /**
     * Maps a MongoDB document to a {@link Book} object.
     *
     * @param doc the MongoDB document representing a book.
     * @return the {@link Book} object.
     */
    private Book mapDocumentToBook(Document doc) {
        String id = doc.getObjectId("_id").toString();
        String title = doc.getString("title");
        String isbn = doc.getString("isbn");

        Genre genre = null;
        Document genreDoc = doc.get("genre", Document.class);
        if (genreDoc != null) {
            genre = new Genre(genreDoc.getInteger("id"), genreDoc.getString("name"));
        }

        List<Author> authors = new ArrayList<>();
        List<Document> authorDocs = doc.getList("authors", Document.class);
        if (authorDocs != null) {
            for (Document authorDoc : authorDocs) {
                authors.add(new Author(
                        authorDoc.getString("name"),
                        authorDoc.getString("birthdate"),
                        authorDoc.getString("user_id")
                ));
            }
        }

        List<Review> reviews = new ArrayList<>();
        List<Document> reviewDocs = doc.getList("reviews", Document.class);
        if (reviewDocs != null) {
            for (Document reviewDoc : reviewDocs) {
                reviews.add(new Review(
                        reviewDoc.getString("user_id"),
                        reviewDoc.getString("review_text"),
                        LocalDate.parse(reviewDoc.getString("review_date")),
                        reviewDoc.getString("username")
                ));
            }
        }

        List<Rating> ratings = new ArrayList<>();
        List<Document> ratingDocs = doc.getList("ratings", Document.class);
        if (ratingDocs != null) {
            for (Document ratingDoc : ratingDocs) {
                ratings.add(new Rating(
                        ratingDoc.getString("user_id"),
                        ratingDoc.getInteger("rating")
                ));
            }
        }

        String userId = doc.getString("user_id");

        return new Book(id, title, isbn, genre, authors, reviews, ratings, userId);
    }
}
