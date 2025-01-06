package melke.bogdo.kth.lab2.labb2mungodb.View;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Provides the graphical interface for the user menu.
 * Users can navigate to various features such as searching, adding, deleting, or rating books, or logging out.
 */
public class UserMenuView {

    /**
     * Creates the user menu layout.
     * Provides buttons for navigating to different features and actions within the application.
     *
     * @param stage the primary {@link Stage} where the user menu is displayed.
     * @return a {@link VBox} containing the layout of the user menu.
     */
    public static VBox create(Stage stage) {
        // Create buttons for menu options
        Button searchButton = new Button("Search for a Book");
        Button addBookButton = new Button("Add Book");
        Button deleteBookButton = new Button("Delete Book");
        Button rateAndReviewBookButton = new Button("Rate");
        Button logoutButton = new Button("Logout");

        // Set button actions
        searchButton.setOnAction(e -> SceneManager.showSearchBooks());
        addBookButton.setOnAction(e -> SceneManager.showAddBook());
        deleteBookButton.setOnAction(e -> SceneManager.showDeleteBook());
        rateAndReviewBookButton.setOnAction(e -> SceneManager.showRateAndReviewMenu());
        logoutButton.setOnAction(e -> SceneManager.showMainMenu());

        // Layout configuration
        VBox layout = new VBox(20, searchButton, addBookButton, rateAndReviewBookButton, deleteBookButton, logoutButton);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        return layout;
    }

    /**
     * Displays the user menu on the provided stage.
     *
     * @param stage the primary {@link Stage} to display the user menu.
     */
    public static void show(Stage stage) {
        stage.setScene(new Scene(create(stage), 400, 300));
        stage.setTitle("User Menu");
        stage.show();
    }
}
