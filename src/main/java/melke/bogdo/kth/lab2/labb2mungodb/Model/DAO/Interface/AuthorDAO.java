package melke.bogdo.kth.lab2.labb2mungodb.Model.DAO.Interface;

import melke.bogdo.kth.lab2.labb2mungodb.Model.Author;

import java.util.List;

/**
 * Interface for managing authors embedded within books.
 * Provides methods to retrieve, add, and verify authors associated with specific books.
 */
public interface AuthorDAO {

    /**
     * Retrieves all authors associated with a specific book.
     *
     * @param bookId the ID of the book whose authors are to be retrieved.
     * @return a {@link List} of {@link Author} objects representing the authors of the specified book.
     * @throws IllegalArgumentException if {@code bookId} is null or empty.
     */
    List<Author> getAuthorsByBookId(String bookId);

    /**
     * Adds a new author to a specific book.
     *
     * @param bookId the ID of the book to which the author will be added.
     * @param author the {@link Author} object containing the details of the author to add.
     * @throws IllegalArgumentException if {@code bookId} is null or empty, or if {@code author} is null.
     */
    void addAuthorToBook(String bookId, Author author);

    /**
     * Checks whether an author exists in a specific book.
     *
     * @param bookId    the ID of the book to check.
     * @param name      the name of the author.
     * @param birthdate the birthdate of the author, formatted as a string (e.g., "YYYY-MM-DD").
     * @return {@code true} if the author exists in the specified book, {@code false} otherwise.
     * @throws IllegalArgumentException if {@code bookId}, {@code name}, or {@code birthdate} is null or empty.
     */
    boolean authorExistsInBook(String bookId, String name, String birthdate);
}
