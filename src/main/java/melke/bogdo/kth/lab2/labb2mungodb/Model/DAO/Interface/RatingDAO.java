package melke.bogdo.kth.lab2.labb2mungodb.Model.DAO.Interface;

import melke.bogdo.kth.lab2.labb2mungodb.Model.Rating;

import java.util.List;

/**
 * Interface for managing rating-related operations embedded within books.
 * Provides methods to add and retrieve ratings associated with specific books.
 */
public interface RatingDAO {

    /**
     * Adds a rating to a specific book.
     *
     * @param bookId the unique ID of the book to which the rating will be added.
     * @param rating the {@link Rating} object containing the rating details.
     * @throws IllegalArgumentException if {@code bookId} is null or empty, or if {@code rating} is null.
     */
    void addRatingToBook(String bookId, Rating rating);

    /**
     * Retrieves all ratings associated with a specific book.
     *
     * @param bookId the unique ID of the book whose ratings are to be retrieved.
     * @return a {@link List} of {@link Rating} objects representing the ratings for the specified book.
     * @throws IllegalArgumentException if {@code bookId} is null or empty.
     */
    List<Rating> getRatingsByBookId(String bookId);
}
