package melke.bogdo.kth.lab2.labb2mungodb.Model;

/**
 * Represents a user entity stored in the users collection.
 * Each user has a unique identifier, a username, and a securely hashed password.
 */
public class User {

    private String id; // MongoDB ObjectId as a String
    private String username;
    private String passwordHash; // Secure hashed password

    /**
     * Constructs a new {@code User} object with the specified details.
     *
     * @param id           the unique identifier of the user (MongoDB ObjectId as a String).
     * @param username     the username of the user.
     * @param passwordHash the securely hashed password of the user.
     */
    public User(String id, String username, String passwordHash) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
    }

    /**
     * Gets the unique identifier of the user.
     *
     * @return the user's unique identifier (MongoDB ObjectId as a String).
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the user.
     *
     * @param id the new unique identifier (MongoDB ObjectId as a String).
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the username of the user.
     *
     * @return the user's username as a {@link String}.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     *
     * @param username the new username as a {@link String}.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the securely hashed password of the user.
     *
     * @return the user's hashed password as a {@link String}.
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Sets the securely hashed password of the user.
     *
     * @param passwordHash the new hashed password as a {@link String}.
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * Returns a string representation of the {@code User} object.
     * The string contains the user's ID and username but excludes the password for security reasons.
     *
     * @return a string representation of the user, including ID and username.
     */
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
