package melke.bogdo.kth.lab2.labb2mungodb.Controller;

import melke.bogdo.kth.lab2.labb2mungodb.Model.DAO.Interface.UserDAO;
import melke.bogdo.kth.lab2.labb2mungodb.Model.DAO.UserDAOImpl;
import melke.bogdo.kth.lab2.labb2mungodb.Model.SessionManager;
import melke.bogdo.kth.lab2.labb2mungodb.Model.User;

/**
 * Controller for managing user authentication and session operations.
 * Provides methods for validating user credentials, managing sessions,
 * and checking the current session status.
 */
public class UserController {

    private final UserDAO userDAO;

    /**
     * Constructs a new {@code UserController} with a default implementation of {@link UserDAO}.
     */
    public UserController() {
        this.userDAO = new UserDAOImpl();
    }

    /**
     * Validates the provided username and password hash.
     * If the credentials are valid, logs the user in by associating the user object with the current session.
     *
     * @param username     the username provided by the user.
     * @param passwordHash the hashed password corresponding to the username.
     * @return {@code true} if the credentials are valid and the user is logged in; {@code false} otherwise.
     * @throws IllegalArgumentException if either {@code username} or {@code passwordHash} is null or empty.
     */
    public boolean validateUser(String username, String passwordHash) {
        if (username == null || username.trim().isEmpty() || passwordHash == null || passwordHash.trim().isEmpty()) {
            throw new IllegalArgumentException("Username and password hash cannot be null or empty.");
        }

        boolean isValid = userDAO.validateUser(username, passwordHash);
        if (isValid) {
            // Fetch the full user object
            User user = userDAO.getUserByUsername(username);
            if (user != null) {
                SessionManager.getInstance().login(user); // Set the user in the session
            }
        }
        return isValid;
    }

    /**
     * Logs out the currently logged-in user by clearing the user session.
     */
    public void logout() {
        SessionManager.getInstance().logout();
    }

    /**
     * Checks whether a user is currently logged into the system.
     *
     * @return {@code true} if a user is logged in; {@code false} otherwise.
     */
    public boolean isLoggedIn() {
        return SessionManager.getInstance().isLoggedIn();
    }
}
