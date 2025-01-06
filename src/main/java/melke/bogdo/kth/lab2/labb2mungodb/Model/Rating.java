package melke.bogdo.kth.lab2.labb2mungodb.Model;

/**
 * Represents a rating embedded in a book document.
 * Each rating is associated with a user and contains a numeric value (e.g., 1-5).
 */
public class Rating {

    private String userId; // ID of the user who submitted the rating
    private Integer rating; // Rating value (e.g., 1-5)

    /**
     * Constructs a new {@code Rating} object with the specified details.
     *
     * @param userId the ID of the user who submitted the rating.
     * @param rating the rating value (e.g., 1-5).
     */
    public Rating(String userId, Integer rating) {
        this.userId = userId;
        this.rating = rating;
    }

    /**
     * Gets the ID of the user who submitted the rating.
     *
     * @return the user ID as a {@link String}.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the ID of the user who submitted the rating.
     *
     * @param userId the new user ID.
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Gets the numeric value of the rating.
     *
     * @return the rating value as an {@link Integer}.
     */
    public Integer getRating() {
        return rating;
    }

    /**
     * Sets the numeric value of the rating.
     *
     * @param rating the new rating value (e.g., 1-5).
     */
    public void setRating(Integer rating) {
        this.rating = rating;
    }

    /**
     * Returns a string representation of the {@code Rating} object.
     *
     * @return a string containing the user ID and rating value.
     */
    @Override
    public String toString() {
        return "Rating{" +
                "userId='" + userId + '\'' +
                ", rating=" + rating +
                '}';
    }
}
