package melke.bogdo.kth.lab2.labb2mungodb.Model.DAO.Interface;

import melke.bogdo.kth.lab2.labb2mungodb.Model.*;

import java.util.List;

/**
 * Interface for managing book-related operations in the database.
 * Provides CRUD operations for books and supports management of embedded entities
 * such as authors, reviews, ratings, and genres.
 */
public interface BookDAO {

    // CRUD Operations for Books

    /**
     * Retrieves all books from the database.
     *
     * @return a {@link List} of all {@link Book} objects in the database.
     */
    List<Book> getAllBooks();

    /**
     * Retrieves a book by its unique ID.
     *
     * @param bookId the unique ID of the book.
     * @return the {@link Book} object if found, or {@code null} if no book is found with the given ID.
     * @throws IllegalArgumentException if {@code bookId} is null or empty.
     */
    Book getBookById(String bookId);

    /**
     * Adds a new book to the database.
     *
     * @param book the {@link Book} object to add.
     * @throws IllegalArgumentException if {@code book} is null.
     */
    void addBook(Book book);

    /**
     * Updates an existing book in the database.
     *
     * @param bookId the unique ID of the book to update.
     * @param book   the updated {@link Book} object.
     * @throws IllegalArgumentException if {@code bookId} is null or empty, or if {@code book} is null.
     */
    void updateBook(String bookId, Book book);

    /**
     * Deletes a book from the database.
     *
     * @param bookId the unique ID of the book to delete.
     * @throws IllegalArgumentException if {@code bookId} is null or empty.
     */
    void deleteBook(String bookId);

    // Embedded Reviews

    /**
     * Adds a review to a specific book.
     *
     * @param bookId the unique ID of the book.
     * @param review the {@link Review} object to add.
     * @throws IllegalArgumentException if {@code bookId} or {@code review} is null.
     */
    void addReview(String bookId, Review review);

    /**
     * Retrieves all reviews for a specific book.
     *
     * @param bookId the unique ID of the book.
     * @return a {@link List} of {@link Review} objects associated with the specified book.
     * @throws IllegalArgumentException if {@code bookId} is null or empty.
     */
    List<Review> getReviewsByBookId(String bookId);

    // Embedded Ratings

    /**
     * Adds a rating to a specific book.
     *
     * @param bookId the unique ID of the book.
     * @param rating the {@link Rating} object to add.
     * @throws IllegalArgumentException if {@code bookId} or {@code rating} is null.
     */
    void addRating(String bookId, Rating rating);

    /**
     * Retrieves all ratings for a specific book.
     *
     * @param bookId the unique ID of the book.
     * @return a {@link List} of {@link Rating} objects associated with the specified book.
     * @throws IllegalArgumentException if {@code bookId} is null or empty.
     */
    List<Rating> getRatingsByBookId(String bookId);

    // Embedded Authors

    /**
     * Retrieves all authors associated with a specific book.
     *
     * @param bookId the unique ID of the book.
     * @return a {@link List} of {@link Author} objects associated with the specified book.
     * @throws IllegalArgumentException if {@code bookId} is null or empty.
     */
    List<Author> getAuthorsByBookId(String bookId);

    /**
     * Adds an author to a specific book.
     *
     * @param bookId the unique ID of the book.
     * @param author the {@link Author} object to add.
     * @throws IllegalArgumentException if {@code bookId} or {@code author} is null.
     */
    void addAuthorToBook(String bookId, Author author);

    /**
     * Checks whether an author exists in a specific book.
     *
     * @param bookId    the unique ID of the book.
     * @param name      the name of the author.
     * @param birthdate the birthdate of the author (formatted as "YYYY-MM-DD").
     * @return {@code true} if the author exists in the book, {@code false} otherwise.
     * @throws IllegalArgumentException if {@code bookId}, {@code name}, or {@code birthdate} is null or empty.
     */
    boolean authorExistsInBook(String bookId, String name, String birthdate);

    // Embedded Genres

    /**
     * Retrieves all genres from the database.
     *
     * @return a {@link List} of {@link Genre} objects available in the database.
     */
    List<Genre> getAllGenres();

    // Search

    /**
     * Searches for books based on the specified criteria.
     *
     * @param type    the type of search (e.g., "Title", "Author", "Genre").
     * @param keyword the search keyword.
     * @return a {@link List} of {@link Book} objects matching the search criteria.
     * @throws IllegalArgumentException if {@code type} or {@code keyword} is null or empty.
     */
    List<Book> searchBooks(String type, String keyword);

    // Authors Across Books

    /**
     * Retrieves all authors across all books in the database.
     *
     * @return a {@link List} of {@link Author} objects representing all authors.
     */
    List<Author> getAllAuthorsFromBooks();
}
