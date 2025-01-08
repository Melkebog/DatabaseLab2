package melke.bogdo.kth.lab2.labb2mungodb.Model.DAO;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import melke.bogdo.kth.lab2.labb2mungodb.Model.User;
import melke.bogdo.kth.lab2.labb2mungodb.Model.DAO.Interface.UserDAO;
import melke.bogdo.kth.lab2.labb2mungodb.Model.DatabaseConnection;
import org.bson.Document;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

// user1, password: password
// user2, password: password

/**
 * Implementation of the {@link UserDAO} interface for managing user-related operations in MongoDB.
 * This class provides methods to retrieve, add, and validate users in the database.
 */
public class UserDAOImpl implements UserDAO {

    private final MongoCollection<Document> usersCollection;

    /**
     * Constructs a new {@code UserDAOImpl} and initializes the connection to the "users" collection.
     */
    public UserDAOImpl() {
        MongoDatabase database = DatabaseConnection.getDatabase();
        this.usersCollection = database.getCollection("users");
    }

    /**
     * Retrieves a user by their unique ID.
     *
     * @param userId the {@link ObjectId} of the user.
     * @return a {@link User} object if the user is found, or {@code null} if not found.
     */
    @Override
    public User getUserById(ObjectId userId) {
        Document userDoc = usersCollection.find(eq("_id", userId)).first();
        if (userDoc != null) {
            return new User(
                    userDoc.getObjectId("_id").toString(),
                    userDoc.getString("username"),
                    userDoc.getString("password_hash")
            );
        }
        return null;
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username the username of the user.
     * @return a {@link User} object if the user is found.
     * @throws IllegalArgumentException if {@code username} is null, empty, or if no user is found with the given username.
     */
    @Override
    public User getUserByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }

        System.out.println("Fetching user with username: " + username);

        // Find the user document in the MongoDB collection
        Document userDoc = usersCollection.find(eq("username", username)).first();
        if (userDoc != null) {
            System.out.println("User found: " + userDoc.toJson());
            // Convert the document to a User object
            return new User(
                    userDoc.getObjectId("_id").toString(),
                    userDoc.getString("username"),
                    userDoc.getString("password_hash")
            );
        } else {
            System.err.println("User not found for username: " + username);
            throw new IllegalArgumentException("User not found: " + username);
        }
    }

    /**
     * Validates a user's credentials by matching their username and hashed password.
     *
     * @param username     the username of the user.
     * @param passwordHash the hashed password of the user.
     * @return {@code true} if the credentials are valid, {@code false} otherwise.
     */
    @Override
    public boolean validateUser(String username, String passwordHash) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }
        if (passwordHash == null || passwordHash.trim().isEmpty()) {
            throw new IllegalArgumentException("Password hash cannot be null or empty.");
        }

        Document user = usersCollection.find(and(
                eq("username", username),
                eq("password_hash", passwordHash)
        )).first();
        return user != null;
    }
}
