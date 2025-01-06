package melke.bogdo.kth.lab2.labb2mungodb.Model;

import java.time.LocalDate;

/**
 * Represents a review embedded in a book document.
 * Each review contains details about the user who submitted it, the content of the review,
 * the date it was submitted, and the username of the reviewer.
 */
public class Review {

    private String userId; // ID of the user who submitted the review
    private String reviewText; // Review content
    private LocalDate reviewDate; // Date of the review
    private String username; // Username of the reviewer

    /**
     * Constructs a new {@code Review} object with the specified details.
     *
     * @param userId     the ID of the user who submitted the review.
     * @param reviewText the content of the review.
     * @param reviewDate the date the review was submitted.
     * @param username   the username of the reviewer.
     */
    public Review(String userId, String reviewText, LocalDate reviewDate, String username) {
        this.userId = userId;
        this.reviewText = reviewText;
        this.reviewDate = reviewDate;
        this.username = username;
    }

    /**
     * Gets the ID of the user who submitted the review.
     *
     * @return the user ID as a {@link String}.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the ID of the user who submitted the review.
     *
     * @param userId the new user ID.
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Gets the content of the review.
     *
     * @return the review content as a {@link String}.
     */
    public String getReviewText() {
        return reviewText;
    }

    /**
     * Sets the content of the review.
     *
     * @param reviewText the new review content.
     */
    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    /**
     * Gets the date the review was submitted.
     *
     * @return the review date as a {@link LocalDate}.
     */
    public LocalDate getReviewDate() {
        return reviewDate;
    }

    /**
     * Sets the date the review was submitted.
     *
     * @param reviewDate the new review date.
     */
    public void setReviewDate(LocalDate reviewDate) {
        this.reviewDate = reviewDate;
    }

    /**
     * Gets the username of the reviewer.
     *
     * @return the reviewer's username as a {@link String}.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the reviewer.
     *
     * @param username the new username.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns a string representation of the {@code Review} object.
     *
     * @return a string containing the user ID, review text, review date, and username.
     */
    @Override
    public String toString() {
        return "Review{" +
                "userId='" + userId + '\'' +
                ", reviewText='" + reviewText + '\'' +
                ", reviewDate=" + reviewDate +
                ", username='" + username + '\'' +
                '}';
    }
}
