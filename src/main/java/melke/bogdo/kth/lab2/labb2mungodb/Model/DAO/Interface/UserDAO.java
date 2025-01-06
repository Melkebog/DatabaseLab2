package melke.bogdo.kth.lab2.labb2mungodb.Model.DAO.Interface;

import melke.bogdo.kth.lab2.labb2mungodb.Model.User;
import org.bson.types.ObjectId;

/**
 * Interface for managing user-related database operations.
 * Provides methods for retrieving, adding, and validating users in the database.
 */
public interface UserDAO {

    /**
     * Retrieves a user by their unique ID.
     *
     * @param userId the unique {@link ObjectId} of the user.
     * @return the {@link User} object if found, or {@code null} if no user exists with the given ID.
     * @throws IllegalArgumentException if {@code userId} is null.
     */
    User getUserById(ObjectId userId);

    /**
     * Retrieves a user by their username.
     *
     * @param username the username of the user to be fetched.
     * @return the {@link User} object if found, or {@code null} if no user exists with the given username.
     * @throws IllegalArgumentException if {@code username} is null or empty.
     */
    User getUserByUsername(String username);

    /**
     * Adds a new user to the database.
     *
     * @param user the {@link User} object containing the user's details.
     * @throws IllegalArgumentException if {@code user} is null.
     */
    void addUser(User user);

    /**
     * Validates a user's credentials using their username and hashed password.
     *
     * @param username     the username of the user.
     * @param passwordHash the hashed password of the user.
     * @return {@code true} if the credentials are valid; {@code false} otherwise.
     * @throws IllegalArgumentException if {@code username} or {@code passwordHash} is null or empty.
     */
    boolean validateUser(String username, String passwordHash);
}
