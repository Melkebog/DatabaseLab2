package melke.bogdo.kth.lab2.labb2mungodb.View;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Manages the scenes of the application.
 * Provides methods to switch between different views such as the main menu, user menu, and book-related operations.
 */
public class SceneManager {

    private static Stage primaryStage;

    /**
     * Initializes the {@code SceneManager} with the primary {@link Stage}.
     * This method must be called before any other methods in this class.
     *
     * @param stage the primary {@link Stage} of the application.
     */
    public static void initialize(Stage stage) {
        primaryStage = stage;
        System.out.println("SceneManager initialized with primaryStage: " + stage);
    }

    /**
     * Displays the user menu scene.
     *
     * @throws IllegalStateException if the {@code primaryStage} is not initialized.
     */
    public static void showUserMenu() {
        ensureInitialized();
        primaryStage.setScene(new Scene(UserMenuView.create(primaryStage)));
    }

    /**
     * Displays the main menu scene.
     *
     * @throws IllegalStateException if the {@code primaryStage} is not initialized.
     */
    public static void showMainMenu() {
        ensureInitialized();
        primaryStage.setScene(new Scene(MainMenuView.create(primaryStage)));
        primaryStage.show();
    }

    /**
     * Displays the "Rate and Review" menu scene.
     *
     * @throws IllegalStateException if the {@code primaryStage} is not initialized.
     */
    public static void showRateAndReviewMenu() {
        ensureInitialized();
        primaryStage.setScene(new Scene(RateAndReviewView.create(primaryStage)));
        primaryStage.show();
    }

    /**
     * Displays the login scene.
     *
     * @throws IllegalStateException if the {@code primaryStage} is not initialized.
     */
    public static void showLogin() {
        ensureInitialized();
        primaryStage.setScene(new Scene(LoginView.create(primaryStage)));
    }

    /**
     * Displays the book search scene.
     *
     * @throws IllegalStateException if the {@code primaryStage} is not initialized.
     */
    public static void showSearchBooks() {
        ensureInitialized();
        primaryStage.setScene(new Scene(BookSearchView.create(primaryStage)));
    }

    /**
     * Displays the "Add Book" scene.
     *
     * @throws IllegalStateException if the {@code primaryStage} is not initialized.
     */
    public static void showAddBook() {
        ensureInitialized();
        primaryStage.setScene(new Scene(AddBookView.create(primaryStage)));
    }

    /**
     * Displays the "Delete Book" scene.
     *
     * @throws IllegalStateException if the {@code primaryStage} is not initialized.
     */
    public static void showDeleteBook() {
        ensureInitialized();
        primaryStage.setScene(new Scene(DeleteBookView.create(primaryStage)));
    }

    /**
     * Ensures that the {@code primaryStage} is initialized before performing any actions.
     *
     * @throws IllegalStateException if the {@code primaryStage} is not initialized.
     */
    private static void ensureInitialized() {
        if (primaryStage == null) {
            throw new IllegalStateException("Primary stage is not initialized. Call SceneManager.initialize() first.");
        }
    }
}
