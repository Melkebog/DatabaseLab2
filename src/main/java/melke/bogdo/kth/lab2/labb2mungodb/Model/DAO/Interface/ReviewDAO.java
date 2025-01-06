package melke.bogdo.kth.lab2.labb2mungodb.Model.DAO.Interface;

import melke.bogdo.kth.lab2.labb2mungodb.Model.Review;

import java.util.List;

/**
 * Interface for managing reviews embedded within books.
 * Provides methods to add and retrieve reviews associated with specific books.
 */
public interface ReviewDAO {

    /**
     * Adds a review to a specific book.
     *
     * @param bookId the unique ID of the book to which the review will be added.
     * @param review the {@link Review} object containing the details of the review.
     * @return {@code true} if the review was added successfully; {@code false} otherwise.
     * @throws IllegalArgumentException if {@code bookId} is null or empty, or if {@code review} is null.
     */
    boolean addReviewToBook(String bookId, Review review);

    /**
     * Retrieves all reviews associated with a specific book.
     *
     * @param bookId the unique ID of the book whose reviews are to be retrieved.
     * @return a {@link List} of {@link Review} objects representing the reviews for the specified book.
     * @throws IllegalArgumentException if {@code bookId} is null or empty.
     */
    List<Review> getReviewsByBookId(String bookId);
}
