package melke.bogdo.kth.lab2.labb2mungodb.Model;

/**
 * Manages the current user session for the application.
 * Provides functionality to track the login state, store user details, and handle logout operations.
 * This class follows the Singleton design pattern to ensure a single instance is used throughout the application.
 */
public class SessionManager {

    private static volatile SessionManager instance; // Ensure volatile for thread safety
    private boolean loggedIn;
    private String currentUserId; // String to support MongoDB ObjectId
    private String currentUsername;

    /**
     * Private constructor to enforce Singleton pattern.
     * Initializes the session as logged out with no user information.
     */
    private SessionManager() {
        this.loggedIn = false;
        this.currentUserId = null;
        this.currentUsername = null;
    }

    /**
     * Retrieves the singleton instance of {@code SessionManager}.
     *
     * @return the {@code SessionManager} instance.
     */
    public static SessionManager getInstance() {
        if (instance == null) {
            synchronized (SessionManager.class) {
                if (instance == null) {
                    instance = new SessionManager();
                }
            }
        }
        return instance;
    }

    /**
     * Logs in a user by setting the session's user details.
     *
     * @param user the {@link User} object representing the logged-in user.
     * @throws IllegalArgumentException if {@code user} is null.
     */
    public void login(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }
        this.loggedIn = true;
        this.currentUserId = user.getId(); // Store userId
        this.currentUsername = user.getUsername(); // Store username
    }

    /**
     * Logs out the current user by resetting the session state.
     */
    public void logout() {
        this.loggedIn = false;
        this.currentUserId = null;
        this.currentUsername = null;
    }

    /**
     * Checks whether a user is currently logged in.
     *
     * @return {@code true} if a user is logged in, {@code false} otherwise.
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * Retrieves the ID of the currently logged-in user.
     *
     * @return the user ID as a {@link String}, or {@code null} if no user is logged in.
     */
    public String getCurrentUserId() {
        return currentUserId;
    }

    /**
     * Retrieves the username of the currently logged-in user.
     *
     * @return the username as a {@link String}, or {@code null} if no user is logged in.
     */
    public String getCurrentUsername() {
        return currentUsername;
    }

    /**
     * Returns a string representation of the {@code SessionManager} object.
     *
     * @return a string containing the session state and current user details.
     */
    @Override
    public String toString() {
        return "SessionManager{" +
                "loggedIn=" + loggedIn +
                ", currentUserId='" + currentUserId + '\'' +
                ", currentUsername='" + currentUsername + '\'' +
                '}';
    }
}
