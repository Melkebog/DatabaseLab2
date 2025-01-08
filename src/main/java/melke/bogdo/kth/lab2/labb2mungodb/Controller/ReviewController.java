package melke.bogdo.kth.lab2.labb2mungodb.Controller;

import melke.bogdo.kth.lab2.labb2mungodb.Model.DAO.BookDAOImpl;
import melke.bogdo.kth.lab2.labb2mungodb.Model.DAO.Interface.BookDAO;
import melke.bogdo.kth.lab2.labb2mungodb.Model.Review;
import melke.bogdo.kth.lab2.labb2mungodb.Model.User;
import melke.bogdo.kth.lab2.labb2mungodb.Model.DAO.Interface.UserDAO;
import melke.bogdo.kth.lab2.labb2mungodb.Model.DAO.UserDAOImpl;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.logging.Logger;

/**
 * Controller responsible for managing reviews and user-related operations.
 * Includes functionality for adding reviews to books, retrieving reviews,
 * user authentication, and user management.
 */
public class ReviewController {

    private static final Logger logger = Logger.getLogger(ReviewController.class.getName());
    private final BookDAO bookDAO;
    private final UserDAO userDAO;

    /**
     * Constructs a new {@code ReviewController} with default DAO implementations
     * for managing reviews and user operations.
     */
    public ReviewController() {
        this.bookDAO = new BookDAOImpl();
        this.userDAO = new UserDAOImpl();
    }

    /**
     * Adds a new review for a specified book.
     * <p>
     * This method retrieves the user based on the provided {@code userId}, creates a new
     * {@link Review} object, and associates it with the book identified by {@code bookId}.
     * </p>
     *
     * @param bookId     the ID of the book to which the review will be added
     * @param userId     the ID of the user submitting the review
     * @param reviewText the text content of the review
     * @throws IllegalArgumentException if {@code reviewText} is null or empty,
     *                                  or if the user is not found for the given {@code userId}
     */
    public void addReview(String bookId, String userId, String reviewText) {
        if (reviewText == null || reviewText.trim().isEmpty()) {
            throw new IllegalArgumentException("Review text cannot be null or empty.");
        }

        User user = userDAO.getUserById(new ObjectId(userId));
        if (user == null) {
            throw new IllegalArgumentException("User not found for user_id: " + userId);
        }

        Review newReview = new Review(userId, reviewText, LocalDate.now(), user.getUsername());
        bookDAO.addReview(bookId, newReview);
        logger.info("Review added for book ID: " + bookId + " by user ID: " + userId);
    }
}
