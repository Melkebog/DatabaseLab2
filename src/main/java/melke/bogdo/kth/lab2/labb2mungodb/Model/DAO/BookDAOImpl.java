package melke.bogdo.kth.lab2.labb2mungodb.Model.DAO;

import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;
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
 * This class provides methods for CRUD operations on books and handles embedded entities such as authors, reviews, and ratings.
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

        // Determine the filter based on the search type
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

        // Execute the query and map results to Book objects
        try {
            for (Document doc : booksCollection.find(filter)) {
                results.add(mapDocumentToBook(doc));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Number of results found: " + results.size());
        return results;
    }



    /**
     * Retrieves a book by its unique ID.
     *
     * @param bookId the unique ID of the book.
     * @return the {@link Book} object if found, or {@code null} if no book is found with the given ID.
     * @throws IllegalArgumentException if {@code bookId} is not a valid ObjectId.
     */
    @Override
    public Book getBookById(String bookId) {
        if (!ObjectId.isValid(bookId)) {
            throw new IllegalArgumentException("Invalid ObjectId: " + bookId);
        }
        Document doc = booksCollection.find(eq("_id", new ObjectId(bookId))).first();
        return (doc != null) ? mapDocumentToBook(doc) : null;
    }

    /**
     * Adds a new book to the database.
     * Constructs a document from the {@link Book} object and inserts it into the MongoDB collection.
     *
     * @param book the {@link Book} object to be added.
     * @throws IllegalStateException if the generated ID for the inserted book cannot be retrieved.
     */
    @Override
    public void addBook(Book book) {
        // Construct the document to insert
        Document bookDoc = new Document("title", book.getTitle())
                .append("isbn", book.getIsbn())
                .append("genre", new Document("id", book.getGenre().getId())
                        .append("name", book.getGenre().getName()))
                .append("authors", book.getAuthors().stream()
                        .map(author -> new Document("name", author.getName())
                                .append("birthdate", author.getBirthdate())
                                .append("user_id", author.getUserId()))
                        .toList())
                .append("reviews", new ArrayList<>())
                .append("ratings", new ArrayList<>())
                .append("user_id", book.getUserId());

        // Insert the document
        booksCollection.insertOne(bookDoc);

        // Fetch the generated _id and set it in the Book object
        ObjectId generatedId = bookDoc.getObjectId("_id");
        if (generatedId != null) {
            book.setId(generatedId.toString());
        } else {
            throw new IllegalStateException("Failed to retrieve generated _id for the inserted book.");
        }
    }

    /**
     * Updates an existing book in the database.
     * Constructs a document from the updated {@link Book} object and applies it to the MongoDB collection.
     *
     * @param bookId the unique ID of the book to update.
     * @param book   the updated {@link Book} object containing the new details.
     * @throws IllegalArgumentException if {@code bookId} is not a valid ObjectId.
     */
    @Override
    public void updateBook(String bookId, Book book) {
        if (!ObjectId.isValid(bookId)) {
            throw new IllegalArgumentException("Invalid ObjectId: " + bookId);
        }
        Document updatedDoc = new Document("title", book.getTitle())
                .append("isbn", book.getIsbn())
                .append("genre", new Document("id", book.getGenre().getId())
                        .append("name", book.getGenre().getName()))
                .append("authors", book.getAuthors().stream()
                        .map(author -> new Document("name", author.getName())
                                .append("birthdate", author.getBirthdate())
                                .append("user_id", author.getUserId()))
                        .toList())
                .append("reviews", book.getReviews())
                .append("ratings", book.getRatings())
                .append("user_id", book.getUserId());
        booksCollection.updateOne(eq("_id", new ObjectId(bookId)), new Document("$set", updatedDoc));
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
     * Adds a review to a specific book.
     * If a review by the same user already exists, it updates the existing review.
     * Otherwise, a new review is added to the book.
     *
     * @param bookId the unique ID of the book to add the review to.
     * @param review the {@link Review} object containing the review details.
     * @throws IllegalArgumentException if {@code bookId} is not a valid ObjectId or {@code review} is null.
     */
    @Override
    public void addReview(String bookId, Review review) {
        if (!ObjectId.isValid(bookId)) {
            throw new IllegalArgumentException("Invalid ObjectId: " + bookId);
        }
        if (review == null) {
            throw new IllegalArgumentException("Review cannot be null.");
        }

        try {
            // Check if a review already exists for this user
            Document existingReview = booksCollection.findOneAndUpdate(
                    Filters.and(
                            eq("_id", new ObjectId(bookId)),
                            Filters.elemMatch("reviews", eq("user_id", review.getUserId()))
                    ),
                    new Document("$set", new Document("reviews.$.review_text", review.getReviewText())
                            .append("reviews.$.review_date", review.getReviewDate().toString()))
            );

            // If no existing review, add a new one
            if (existingReview == null) {
                booksCollection.updateOne(
                        eq("_id", new ObjectId(bookId)),
                        new Document("$push", new Document("reviews", new Document("user_id", review.getUserId())
                                .append("review_text", review.getReviewText())
                                .append("review_date", review.getReviewDate().toString())
                                .append("username", review.getUsername())))
                );
            }
        } catch (Exception e) {
            System.err.println("Error in addReview: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Retrieves all reviews associated with a specific book.
     *
     * @param bookId the unique ID of the book.
     * @return a {@link List} of {@link Review} objects associated with the book, or an empty list if no reviews are found.
     * @throws IllegalArgumentException if {@code bookId} is not a valid ObjectId.
     */
    @Override
    public List<Review> getReviewsByBookId(String bookId) {
        if (!ObjectId.isValid(bookId)) {
            throw new IllegalArgumentException("Invalid ObjectId: " + bookId);
        }

        Document book = booksCollection.find(eq("_id", new ObjectId(bookId))).first();
        if (book != null) {
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


    /**
     * Adds or updates a rating for a specific book.
     * If the user has already rated the book, their existing rating is updated.
     * Otherwise, a new rating is added to the book.
     *
     * @param bookId the unique ID of the book to add or update the rating for.
     * @param rating the {@link Rating} object containing the rating details.
     * @throws IllegalArgumentException if {@code bookId} is not a valid ObjectId or {@code rating} is null.
     */
    @Override
    public void addRating(String bookId, Rating rating) {
        if (!ObjectId.isValid(bookId)) {
            throw new IllegalArgumentException("Invalid ObjectId: " + bookId);
        }
        if (rating == null) {
            throw new IllegalArgumentException("Rating cannot be null.");
        }

        try {
            // Update existing rating if it exists
            booksCollection.updateOne(
                    Filters.and(
                            eq("_id", new ObjectId(bookId)),
                            Filters.elemMatch("ratings", eq("user_id", rating.getUserId()))
                    ),
                    new Document("$set", new Document("ratings.$.rating", rating.getRating()))
            );

            // Add a new rating if no existing rating was found
            booksCollection.updateOne(
                    eq("_id", new ObjectId(bookId)),
                    new Document("$addToSet", new Document("ratings", new Document("user_id", rating.getUserId()).append("rating", rating.getRating())))
            );
        } catch (Exception e) {
            System.err.println("Error in addRating: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Retrieves all ratings for a specific book.
     *
     * @param bookId the unique ID of the book.
     * @return a {@link List} of {@link Rating} objects associated with the book, or an empty list if no ratings are found.
     * @throws IllegalArgumentException if {@code bookId} is not a valid ObjectId.
     */
    @Override
    public List<Rating> getRatingsByBookId(String bookId) {
        if (!ObjectId.isValid(bookId)) {
            throw new IllegalArgumentException("Invalid ObjectId: " + bookId);
        }

        Document book = booksCollection.find(eq("_id", new ObjectId(bookId))).first();
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

    /**
     * Retrieves all authors associated with a specific book.
     *
     * @param bookId the unique ID of the book.
     * @return a {@link List} of {@link Author} objects associated with the book, or an empty list if no authors are found.
     * @throws IllegalArgumentException if {@code bookId} is not a valid ObjectId.
     */
    @Override
    public List<Author> getAuthorsByBookId(String bookId) {
        if (!ObjectId.isValid(bookId)) {
            throw new IllegalArgumentException("Invalid ObjectId: " + bookId);
        }

        Document book = booksCollection.find(eq("_id", new ObjectId(bookId))).first();
        if (book != null) {
            List<Document> authorDocs = book.getList("authors", Document.class);
            List<Author> authors = new ArrayList<>();
            if (authorDocs != null) {
                for (Document doc : authorDocs) {
                    authors.add(new Author(
                            doc.getString("name"),
                            doc.getString("birthdate"),
                            doc.getString("user_id")
                    ));
                }
            }
            return authors;
        }
        return new ArrayList<>();
    }

    /**
     * Adds a new author to a specific book.
     *
     * @param bookId the unique ID of the book to add the author to.
     * @param author the {@link Author} object containing the author's details.
     * @throws IllegalArgumentException if {@code bookId} is not a valid ObjectId or {@code author} is null.
     */
    @Override
    public void addAuthorToBook(String bookId, Author author) {
        if (!ObjectId.isValid(bookId)) {
            throw new IllegalArgumentException("Invalid ObjectId: " + bookId);
        }
        if (author == null) {
            throw new IllegalArgumentException("Author cannot be null.");
        }

        Document authorDoc = new Document("name", author.getName())
                .append("birthdate", author.getBirthdate())
                .append("user_id", author.getUserId());
        booksCollection.updateOne(eq("_id", new ObjectId(bookId)), new Document("$push", new Document("authors", authorDoc)));
    }

    /**
     * Checks whether a specific author exists in a specific book.
     *
     * @param bookId    the unique ID of the book.
     * @param name      the name of the author to check.
     * @param birthdate the birthdate of the author to check.
     * @return {@code true} if the author exists in the book; {@code false} otherwise.
     * @throws IllegalArgumentException if {@code bookId} is not a valid ObjectId, or if {@code name} or {@code birthdate} is null or empty.
     */
    @Override
    public boolean authorExistsInBook(String bookId, String name, String birthdate) {
        if (!ObjectId.isValid(bookId)) {
            throw new IllegalArgumentException("Invalid ObjectId: " + bookId);
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Author name cannot be null or empty.");
        }
        if (birthdate == null || birthdate.trim().isEmpty()) {
            throw new IllegalArgumentException("Author birthdate cannot be null or empty.");
        }

        Document book = booksCollection.find(eq("_id", new ObjectId(bookId))).first();
        if (book != null) {
            List<Document> authorDocs = book.getList("authors", Document.class);
            if (authorDocs != null) {
                for (Document doc : authorDocs) {
                    if (doc.getString("name").equals(name) &&
                            doc.getString("birthdate").equals(birthdate)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * Retrieves all unique genres associated with books in the database.
     *
     * @return a {@link List} of {@link Genre} objects representing all genres found in the database.
     */
    @Override
    public List<Genre> getAllGenres() {
        List<Document> books = booksCollection.find().into(new ArrayList<>());
        List<Genre> genres = new ArrayList<>();
        for (Document book : books) {
            Document genreDoc = book.get("genre", Document.class);
            if (genreDoc != null) {
                genres.add(new Genre(genreDoc.getInteger("id"), genreDoc.getString("name")));
            }
        }
        return genres.stream().distinct().toList();
    }

    /**
     * Retrieves all unique authors from all books in the database.
     * Aggregates the authors by unwinding the "authors" array and grouping by unique author entries.
     *
     * @return a {@link List} of {@link Author} objects representing all unique authors across all books.
     */
    @Override
    public List<Author> getAllAuthorsFromBooks() {
        List<Author> authors = new ArrayList<>();
        booksCollection.aggregate(List.of(
                Aggregates.unwind("$authors"), // Unwind the authors array
                Aggregates.group("$authors", Accumulators.first("author", "$authors")) // Group by unique authors
        )).forEach((Block<? super Document>) doc -> {
            Document authorDoc = doc.get("_id", Document.class);
            if (authorDoc != null) {
                authors.add(new Author(
                        authorDoc.getString("name"),
                        authorDoc.getString("birthdate"),
                        null // userId is optional
                ));
            }
        });
        return authors;
    }

    /**
     * Maps a MongoDB document to a {@link Book} object.
     * Extracts fields such as title, ISBN, genre, authors, reviews, and ratings from the document
     * and constructs a {@link Book} object.
     *
     * @param doc the MongoDB document representing a book.
     * @return the mapped {@link Book} object.
     */
    private Book mapDocumentToBook(Document doc) {
        // Extract the ObjectId as a string
        String id = doc.getObjectId("_id").toString();

        // Extract basic fields
        String title = doc.getString("title");
        String isbn = doc.getString("isbn");

        // Map genre
        Document genreDoc = doc.get("genre", Document.class);
        Genre genre = null;
        if (genreDoc != null) {
            genre = new Genre(genreDoc.getInteger("id"), genreDoc.getString("name"));
        }

        // Map authors
        List<Document> authorDocs = doc.getList("authors", Document.class);
        List<Author> authors = new ArrayList<>();
        if (authorDocs != null) {
            for (Document authorDoc : authorDocs) {
                authors.add(new Author(
                        authorDoc.getString("name"),
                        authorDoc.getString("birthdate"),
                        authorDoc.getString("user_id")
                ));
            }
        }

        // Map reviews
        List<Document> reviewDocs = doc.getList("reviews", Document.class);
        List<Review> reviews = new ArrayList<>();
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

        // Map ratings
        List<Document> ratingDocs = doc.getList("ratings", Document.class);
        List<Rating> ratings = new ArrayList<>();
        if (ratingDocs != null) {
            for (Document ratingDoc : ratingDocs) {
                ratings.add(new Rating(
                        ratingDoc.getString("user_id"),
                        ratingDoc.getInteger("rating")
                ));
            }
        }

        // Extract user_id
        String userId = doc.getString("user_id");

        // Construct and return the Book object
        return new Book(id, title, isbn, genre, authors, reviews, ratings, userId);
    }

}
