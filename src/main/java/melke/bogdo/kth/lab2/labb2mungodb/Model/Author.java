package melke.bogdo.kth.lab2.labb2mungodb.Model;

/**
 * Represents an author associated with a book.
 * Each author has a name, birthdate, and an optional user ID linking them to a user in the system.
 */
public class Author {

    private String name;
    private String birthdate;
    private String userId; // Nullable user ID if the author is linked to a user (not used for this labb, side project)

    /**
     * Constructs a new {@code Author} object with the specified details.
     *
     * @param name      the name of the author.
     * @param birthdate the birthdate of the author (formatted as "YYYY-MM-DD").
     * @param userId    the optional user ID associated with the author, or {@code null} if not linked to a user.
     */
    public Author(String name, String birthdate, String userId) {
        this.name = name;
        this.birthdate = birthdate;
        this.userId = userId;
    }

    /**
     * Gets the name of the author.
     *
     * @return the author's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the author.
     *
     * @param name the new name of the author.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the birthdate of the author.
     *
     * @return the author's birthdate (formatted as "YYYY-MM-DD").
     */
    public String getBirthdate() {
        return birthdate;
    }

    /**
     * Sets the birthdate of the author.
     *
     * @param birthdate the new birthdate of the author (formatted as "YYYY-MM-DD").
     */
    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    /**
     * Gets the user ID associated with the author.
     *
     * @return the user ID, or {@code null} if the author is not linked to a user.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the user ID associated with the author.
     *
     * @param userId the new user ID, or {@code null} if the author is not linked to a user.
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Returns a string representation of the {@code Author} object.
     *
     * @return a string containing the author's name, birthdate, and user ID.
     */
    @Override
    public String toString() {
        return "Author{" +
                "name='" + name + '\'' +
                ", birthdate='" + birthdate + '\'' +
                ", userId=" + userId +
                '}';
    }
}
