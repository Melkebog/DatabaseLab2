package melke.bogdo.kth.lab2.labb2mungodb.Model.DAO.Interface;

import melke.bogdo.kth.lab2.labb2mungodb.Model.*;

import java.util.List;

/**
 * Interface for managing book-related operations in the database.
 * <p>
 * Provides methods for CRUD operations on books and supports the management
 * of embedded entities such as authors, reviews, ratings, and genres.
 * This interface abstracts the database interactions for book-related features.
 * </p>
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
     * Adds a new book to the database.
     *
     * @param book the {@link Book} object to add.
     * @throws IllegalArgumentException if {@code book} is null.
     */
    void addBook(Book book);

    /**
     * Deletes a book from the database by its unique identifier.
     *
     * @param bookId the unique ID of the book to delete.
     * @throws IllegalArgumentException if {@code bookId} is null or empty.
     */
    void deleteBook(String bookId);

    // Embedded Reviews

    /**
     * Adds or updates a review for a specific book.
     * <p>
     * If a review by the same user already exists for the book, it is updated.
     * Otherwise, a new review is added to the book's reviews.
     * </p>
     *
     * @param bookId the unique ID of the book.
     * @param review the {@link Review} object to add or update.
     * @throws IllegalArgumentException if {@code bookId} or {@code review} is null.
     */
    void addReview(String bookId, Review review);

    // Embedded Ratings

    /**
     * Adds a rating to a specific book.
     * <p>
     * Ratings can be used to calculate the average score or for other statistical purposes.
     * </p>
     *
     * @param bookId the unique ID of the book.
     * @param rating the {@link Rating} object to add.
     * @throws IllegalArgumentException if {@code bookId} or {@code rating} is null.
     */
    void addRating(String bookId, Rating rating);

    // Embedded Search

    /**
     * Searches for books in the database based on the specified criteria.
     * <p>
     * Supports searching by book title, author name, genre, or other attributes.
     * The {@code type} parameter specifies the attribute to search by,
     * and {@code keyword} specifies the search term.
     * </p>
     *
     * @param type    the type of search (e.g., "Title", "Author", "Genre").
     * @param keyword the search keyword.
     * @return a {@link List} of {@link Book} objects matching the search criteria.
     * @throws IllegalArgumentException if {@code type} or {@code keyword} is null or empty.
     */
    List<Book> searchBooks(String type, String keyword);
}
