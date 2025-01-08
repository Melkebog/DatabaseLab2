package melke.bogdo.kth.lab2.labb2mungodb.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import melke.bogdo.kth.lab2.labb2mungodb.Model.*;
import melke.bogdo.kth.lab2.labb2mungodb.Model.DAO.*;
import melke.bogdo.kth.lab2.labb2mungodb.Model.DAO.Interface.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Controller responsible for managing book-related operations,
 * including retrieval, addition, rating, searching, and deletion of books.
 * It interacts with the {@link BookDAO} to perform database operations
 * and ensures the UI remains responsive during these operations.
 */
public class BookController {

    private static final Logger logger = Logger.getLogger(BookController.class.getName());
    private final BookDAO bookDAO;

    /**
     * Constructs a new {@code BookController} with a default implementation of {@link BookDAO}.
     */
    public BookController() {
        this.bookDAO = new BookDAOImpl();
    }

    /**
     * Retrieves all books from the database.
     *
     * @return an {@link ObservableList} containing all books.
     */
    public ObservableList<Book> getAllBooks() {
        return FXCollections.observableArrayList(bookDAO.getAllBooks());
    }

    /**
     * Adds a new book to the database.
     *
     * @param title   the title of the book.
     * @param isbn    the ISBN of the book.
     * @param genre   the genre of the book.
     * @param authors a list of authors associated with the book.
     * @throws IllegalArgumentException if any parameter is invalid.
     */
    public void addBook(String title, String isbn, Genre genre, List<Author> authors) {
        if (title == null || title.isEmpty() || isbn == null || isbn.isEmpty() || genre == null || authors.isEmpty()) {
            throw new IllegalArgumentException("Invalid book details provided.");
        }
        Book newBook = new Book(null, title, isbn, genre, authors, new ArrayList<>(), new ArrayList<>(), SessionManager.getInstance().getCurrentUserId());
        bookDAO.addBook(newBook);
        logger.info("Book added: " + title);
    }

    /**
     * Adds a rating to a specified book.
     *
     * @param bookId the ID of the book.
     * @param rating the rating value (1-5).
     * @throws IllegalArgumentException if the rating is outside the range of 1-5.
     */
    public void addRating(String bookId, int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
        Rating newRating = new Rating(SessionManager.getInstance().getCurrentUserId(), rating);
        bookDAO.addRating(bookId, newRating);
        logger.info("Rating added to book ID " + bookId + ": " + rating);
    }

    /**
     * Searches for books based on a specific criterion.
     *
     * @param type    the type of search (e.g., Title, Author, Genre).
     * @param keyword the search keyword.
     * @return a list of books matching the search criteria.
     * @throws IllegalArgumentException if the search parameters are invalid.
     */
    public List<Book> searchBooks(String type, String keyword) {
        if (type == null || keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid search parameters.");
        }
        return bookDAO.searchBooks(type, keyword);
    }

    /**
     * Deletes a book from the database by its ID.
     *
     * @param bookId the ID of the book.
     */
    public void deleteBook(String bookId) {
        bookDAO.deleteBook(bookId);
        logger.info("Book deleted: " + bookId);
    }
}
