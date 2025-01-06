package melke.bogdo.kth.lab2.labb2mungodb.View;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import melke.bogdo.kth.lab2.labb2mungodb.Controller.BookController;
import melke.bogdo.kth.lab2.labb2mungodb.Model.Book;

import java.util.List;

/**
 * Provides the graphical interface for deleting a book from the system.
 * Users can select a book from a dropdown and delete it.
 */
public class DeleteBookView {

    /**
     * Creates the "Delete Book" view layout.
     * Displays a list of books to select from, and provides options to delete a book or go back to the main menu.
     *
     * @param stage the primary {@link Stage} of the application.
     * @return a {@link VBox} containing the layout of the "Delete Book" view.
     */
    public static VBox create(Stage stage) {
        BookController bookController = new BookController();

        // Label and ComboBox for books
        Label bookLabel = new Label("Select a book to delete:");
        ComboBox<Book> bookComboBox = new ComboBox<>();
        populateBookList(bookComboBox, bookController);

        // Buttons
        Button deleteButton = new Button("Delete");
        Button backButton = new Button("Back");

        deleteButton.setOnAction(e -> {
            Book selectedBook = bookComboBox.getValue();
            if (selectedBook == null) {
                showAlert(Alert.AlertType.ERROR, "Please select a book to delete.");
                return;
            }

            try {
                bookController.deleteBook(selectedBook.getId());
                showAlert(Alert.AlertType.INFORMATION, "Book deleted successfully!");
                populateBookList(bookComboBox, bookController); // Refresh the list
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Failed to delete book: " + ex.getMessage());
            }
        });

        backButton.setOnAction(e -> SceneManager.showUserMenu());

        // Layout
        VBox layout = new VBox(10, bookLabel, bookComboBox, deleteButton, backButton);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-alignment: center;");

        return layout;
    }

    /**
     * Populates the {@link ComboBox} with a list of available books.
     * Filters out null or invalid book entries and sets a {@link StringConverter} to display book titles.
     *
     * @param comboBox       the {@link ComboBox} to populate with books.
     * @param bookController the {@link BookController} used to retrieve books.
     */
    private static void populateBookList(ComboBox<Book> comboBox, BookController bookController) {
        try {
            List<Book> books = bookController.getAllBooks();

            // Remove null or invalid entries
            books.removeIf(book -> book == null || book.getTitle() == null);

            ObservableList<Book> bookOptions = FXCollections.observableArrayList(books);
            comboBox.setItems(bookOptions);

            // Set StringConverter to display book titles
            comboBox.setConverter(new StringConverter<>() {
                @Override
                public String toString(Book book) {
                    return book != null ? book.getTitle() : "Unknown Book";
                }

                @Override
                public Book fromString(String string) {
                    return comboBox.getItems().stream()
                            .filter(book -> book.getTitle().equals(string))
                            .findFirst()
                            .orElse(null);
                }
            });

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Failed to load books: " + e.getMessage());
        }
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
