package melke.bogdo.kth.lab2.labb2mungodb;

import javafx.application.Application;
import javafx.stage.Stage;
import melke.bogdo.kth.lab2.labb2mungodb.Model.DatabaseConnection;
import melke.bogdo.kth.lab2.labb2mungodb.View.SceneManager;

/**
 * The entry point for the application.
 * Initializes the primary {@link Stage} and sets the initial scene to the main menu.
 * Ensures that resources like the database connection are released on application exit.
 */
public class MainApp extends Application {

    /**
     * The main entry point for the JavaFX application.
     * Initializes the {@link SceneManager} and shows the main menu.
     *
     * @param primaryStage the primary {@link Stage} for the application.
     */
    @Override
    public void start(Stage primaryStage) {
        SceneManager.initialize(primaryStage);
        SceneManager.showMainMenu();

        // shutdown hook to close the database connection on application exit
        primaryStage.setOnCloseRequest(event -> {
            System.out.println("Application is closing. Releasing resources...");
            DatabaseConnection.closeConnection(); // Close the MongoDB connection
        });
    }

    /**
     * The main method for launching the JavaFX application.
     *
     * @param args the command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
