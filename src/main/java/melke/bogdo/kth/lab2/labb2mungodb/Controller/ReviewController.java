package melke.bogdo.kth.lab2.labb2mungodb.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import melke.bogdo.kth.lab2.labb2mungodb.Model.Review;
import melke.bogdo.kth.lab2.labb2mungodb.Model.SessionManager;
import melke.bogdo.kth.lab2.labb2mungodb.Model.User;
import melke.bogdo.kth.lab2.labb2mungodb.Model.DAO.Interface.ReviewDAO;
import melke.bogdo.kth.lab2.labb2mungodb.Model.DAO.Interface.UserDAO;
import melke.bogdo.kth.lab2.labb2mungodb.Model.DAO.ReviewDAOImpl;
import melke.bogdo.kth.lab2.labb2mungodb.Model.DAO.UserDAOImpl;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

/**
 * Controller responsible for managing reviews and user-related operations.
 * Includes functionality for adding reviews to books, retrieving reviews,
 * user authentication, and user management.
 */
public class ReviewController {

    private static final Logger logger = Logger.getLogger(ReviewController.class.getName());
    private final ReviewDAO reviewDAO;
    private final UserDAO userDAO;

    /**
     * Constructs a new {@code ReviewController} with default DAO implementations
     * for managing reviews and user operations.
     */
    public ReviewController() {
        this.reviewDAO = new ReviewDAOImpl();
        this.userDAO = new UserDAOImpl();
    }

    /**
     * Adds a review to a specified book.
     *
     * @param bookId     the ID of the book to add the review to.
     * @param userId     the ID of the user writing the review.
     * @param reviewText the content of the review.
     * @throws IllegalArgumentException if the review text is null, empty, or the user does not exist.
     */
    public void addReview(String bookId, String userId, String reviewText) {
        if (reviewText == null || reviewText.trim().isEmpty()) {
            throw new IllegalArgumentException("Review text cannot be null or empty.");
        }

        // Convert userId to ObjectId
        ObjectId userObjectId = new ObjectId(userId);

        // Fetch user by ObjectId
        User user = userDAO.getUserById(userObjectId);
        if (user == null) {
            throw new IllegalArgumentException("User not found for user_id: " + userId);
        }

        // Create and add the review
        Review newReview = new Review(userId, reviewText, LocalDate.now(), user.getUsername());
        reviewDAO.addReviewToBook(bookId, newReview);
        logger.info("Review added for book ID: " + bookId + " by user ID: " + userId);
    }

    /**
     * Retrieves all reviews associated with a specific book.
     *
     * @param bookId the ID of the book.
     * @return an {@link ObservableList} of {@link Review} objects for the specified book.
     * @throws IllegalArgumentException if the {@code bookId} is null or empty.
     */
    public ObservableList<Review> getReviewsByBookId(String bookId) {
        List<Review> reviews = reviewDAO.getReviewsByBookId(bookId);
        return FXCollections.observableArrayList(reviews);
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username the username of the user.
     * @return the {@link User} object if found.
     * @throws IllegalArgumentException if the username is null, empty, or the user does not exist.
     */
    public User getUserByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }

        User user = userDAO.getUserByUsername(username);
        if (user != null) {
            logger.info("User found: " + username);
            return user;
        } else {
            logger.warning("User not found: " + username);
            throw new IllegalArgumentException("User not found: " + username);
        }
    }

    /**
     * Validates a user's credentials.
     *
     * @param username     the username of the user.
     * @param passwordHash the hashed password of the user.
     * @throws IllegalArgumentException if the credentials are invalid.
     */
    public void validateUser(String username, String passwordHash) {
        if (!userDAO.validateUser(username, passwordHash)) {
            throw new IllegalArgumentException("Invalid credentials for user: " + username);
        }
        logger.info("User validated successfully: " + username);
    }

    /**
     * Adds a new user to the database.
     *
     * @param username     the username of the new user.
     * @param passwordHash the hashed password of the new user.
     * @throws IllegalArgumentException if the username or password hash is null or empty.
     */
    public void addUser(String username, String passwordHash) {
        if (username == null || username.trim().isEmpty() || passwordHash == null || passwordHash.trim().isEmpty()) {
            throw new IllegalArgumentException("Username and password hash cannot be null or empty.");
        }

        User newUser = new User(null, username, passwordHash);
        userDAO.addUser(newUser);
        logger.info("New user added: " + username);
    }
}
