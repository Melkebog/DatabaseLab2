package melke.bogdo.kth.lab2.labb2mungodb.View;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import melke.bogdo.kth.lab2.labb2mungodb.Controller.HashUtil;
import melke.bogdo.kth.lab2.labb2mungodb.Controller.UserController;
import melke.bogdo.kth.lab2.labb2mungodb.View.SceneManager;

/**
 * Provides the graphical interface for the user login functionality.
 * Users can enter their username and password to log into the system.
 */
public class LoginView {

    /**
     * Creates the "Login" view layout.
     * Allows users to input their username and password and perform the login action.
     *
     * @param stage the primary {@link Stage} of the application.
     * @return a {@link VBox} containing the layout of the "Login" view.
     */
    public static VBox create(Stage stage) {
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        // Login button
        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            UserController userController = new UserController();
            if (userController.validateUser(username, HashUtil.hash(password))) {
                SceneManager.showUserMenu(); // Redirect to UserMenu after login
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Invalid credentials.");
                alert.showAndWait();
            }
        });

        // Back button
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> SceneManager.showMainMenu());

        // Layout
        VBox layout = new VBox(10, usernameField, passwordField, loginButton, backButton);
        return layout;
    }
}
