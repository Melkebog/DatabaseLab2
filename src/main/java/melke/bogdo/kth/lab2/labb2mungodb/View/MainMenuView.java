package melke.bogdo.kth.lab2.labb2mungodb.View;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Provides the graphical interface for the main menu of the application.
 * The main menu allows users to search for books or log into the system.
 */
public class MainMenuView {

    /**
     * Creates the main menu layout with options for searching books and logging in.
     *
     * @param stage the primary {@link Stage} where the menu is displayed.
     * @return a {@link VBox} containing the layout for the main menu.
     */
    public static VBox create(Stage stage) {
        // Create buttons for menu options
        Button searchButton = new Button("Search for a Book");
        Button loginButton = new Button("Login");

        // Set button actions
        searchButton.setOnAction(e -> SceneManager.showSearchBooks());
        loginButton.setOnAction(e -> SceneManager.showLogin());

        // Style and layout configuration
        VBox layout = new VBox(20, searchButton, loginButton);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 20;");

        // Button styling
        searchButton.setPrefWidth(200);
        loginButton.setPrefWidth(200);

        return layout;
    }

    /**
     * Displays the main menu on the provided stage.
     *
     * @param stage the primary {@link Stage} to display the menu.
     */
    public static void show(Stage stage) {
        Scene mainMenuScene = new Scene(create(stage), 400, 300);
        stage.setScene(mainMenuScene);
        stage.setTitle("Main Menu");
        stage.show();
    }
}
