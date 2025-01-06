package melke.bogdo.kth.lab2.labb2mungodb.View;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import melke.bogdo.kth.lab2.labb2mungodb.Controller.BookController;
import melke.bogdo.kth.lab2.labb2mungodb.Model.Book;
import melke.bogdo.kth.lab2.labb2mungodb.Model.SessionManager;

import java.util.List;

/**
 * Provides the graphical interface for searching and displaying books.
 * Users can search books by title, genre, author, or ISBN and view the results in a list.
 */
public class BookSearchView {

    /**
     * Creates the "Book Search" view layout.
     * Allows users to search for books using various criteria and view the results.
     *
     * @param stage the primary {@link Stage} of the application.
     * @return a {@link BorderPane} containing the layout of the "Book Search" view.
     */
    public static BorderPane create(Stage stage) {
        BookController bookController = new BookController();

        // Search bar
        TextField searchField = new TextField();
        searchField.setPromptText("Enter search keyword");

        ComboBox<String> searchTypeCombo = new ComboBox<>();
        searchTypeCombo.getItems().addAll("Title", "Genre", "Author", "ISBN");
        searchTypeCombo.setValue("Title");

        Button searchButton = new Button("Search");
        Button backButton = new Button("Back");

        HBox searchBar = new HBox(10, searchTypeCombo, searchField, searchButton, backButton);
        searchBar.setPadding(new Insets(10));

        // Results list
        ListView<String> bookListView = new ListView<>();
        ObservableList<String> results = FXCollections.observableArrayList();
        bookListView.setItems(results);

        // Search action
        searchButton.setOnAction(e -> {
            String searchType = searchTypeCombo.getValue();
            String keyword = searchField.getText().trim();

            if (keyword.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Search keyword cannot be empty.");
                return;
            }

            List<Book> books = bookController.searchBooks(searchType, keyword); // Pass both arguments
            results.clear();

            if (books.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "No books found for the given search criteria.");
            } else {
                for (Book book : books) {
                    results.add(formatBookDetails(book));
                }
            }
        });

        // Back action
        if (SessionManager.getInstance().isLoggedIn()) {
            backButton.setOnAction(e -> SceneManager.showUserMenu());
        } else {
            backButton.setOnAction(e -> SceneManager.showMainMenu());
        }

        // Layout
        BorderPane layout = new BorderPane();
        layout.setTop(searchBar);
        layout.setCenter(bookListView);
        return layout;
    }

    /**
     * Formats the details of a {@link Book} object into a string representation for display.
     *
     * @param book the {@link Book} object whose details are to be formatted.
     * @return a string containing the formatted details of the book.
     */
    private static String formatBookDetails(Book book) {
        StringBuilder details = new StringBuilder();
        details.append("Title: ").append(book.getTitle()).append("\n")
                .append("ISBN: ").append(book.getIsbn()).append("\n")
                .append("Genre: ").append(book.getGenre() != null ? book.getGenre().getName() : "Unknown").append("\n")
                .append("Authors: ");

        if (book.getAuthors() != null && !book.getAuthors().isEmpty()) {
            book.getAuthors().forEach(author -> details.append(author.getName()).append(", "));
            details.delete(details.length() - 2, details.length()); // Remove trailing comma
        } else {
            details.append("None");
        }

        details.append("\nAverage Rating: ").append(book.getAverageRating());
        return details.toString();
    }

    /**
     * Displays an alert message to the user.
     *
     * @param type    the {@link Alert.AlertType} of the alert.
     * @param message the message to display in the alert.
     */
    private static void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
