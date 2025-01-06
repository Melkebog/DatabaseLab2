package melke.bogdo.kth.lab2.labb2mungodb.View;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import melke.bogdo.kth.lab2.labb2mungodb.Controller.BookController;
import melke.bogdo.kth.lab2.labb2mungodb.Controller.ReviewController;
import melke.bogdo.kth.lab2.labb2mungodb.Model.Book;
import melke.bogdo.kth.lab2.labb2mungodb.Model.SessionManager;

import java.util.List;

/**
 * Provides the graphical interface for rating and reviewing books.
 * Users can select a book, provide a rating, write a review, or both.
 */
public class RateAndReviewView {

    /**
     * Creates the "Rate and Review" view layout.
     * Allows users to select a book, provide a rating, write a review, and submit their input.
     *
     * @param stage the primary {@link Stage} of the application.
     * @return a {@link VBox} containing the layout of the "Rate and Review" view.
     */
    public static VBox create(Stage stage) {
        BookController bookController = new BookController();
        ReviewController reviewController = new ReviewController();

        // Title
        Label titleLabel = new Label("Rate and Review Book");

        // Book Selection
        Label selectBookLabel = new Label("Select a book:");
        ComboBox<String> bookDropdown = new ComboBox<>();
        populateBookDropdown(bookDropdown, bookController);

        // Rating section
        Label ratingLabel = new Label("Select a rating (1-5):");
        ComboBox<Integer> ratingComboBox = new ComboBox<>();
        ratingComboBox.getItems().addAll(1, 2, 3, 4, 5);
        ratingComboBox.setPromptText("Rating");

        // Review section
        Label reviewLabel = new Label("Write a review:");
        TextArea reviewTextArea = new TextArea();
        reviewTextArea.setPromptText("Enter your review here...");
        reviewTextArea.setWrapText(true);

        // Submit and Back buttons
        Button submitButton = new Button("Submit");
        Button backButton = new Button("Back");

        submitButton.setOnAction(e -> {
            String selectedBook = bookDropdown.getValue();
            Integer selectedRating = ratingComboBox.getValue();
            String reviewText = reviewTextArea.getText().trim();

            // Validate inputs
            if (selectedBook == null || selectedBook.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Please select a book.");
                return;
            }
            if (selectedRating == null && reviewText.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "You must provide either a rating, a review, or both.");
                return;
            }

            try {
                // Extract the ObjectId part from the dropdown value
                String selectedBookId = selectedBook.split(" - ")[0].trim();

                // Add rating if provided
                if (selectedRating != null) {
                    bookController.addRating(selectedBookId, selectedRating);
                }

                // Add review if provided
                if (!reviewText.isEmpty()) {
                    String userId = SessionManager.getInstance().getCurrentUserId(); // Fetch current user ID
                    reviewController.addReview(selectedBookId, userId, reviewText);
                }

                showAlert(Alert.AlertType.INFORMATION, "Your rating and/or review has been submitted.");
                SceneManager.showUserMenu();
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Failed to submit rating and/or review: " + ex.getMessage());
            }
        });

        backButton.setOnAction(e -> SceneManager.showUserMenu());

        // Layout
        VBox layout = new VBox(10, titleLabel, selectBookLabel, bookDropdown, ratingLabel, ratingComboBox, reviewLabel, reviewTextArea, submitButton, backButton);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-alignment: center;");

        return layout;
    }

    /**
     * Populates the {@link ComboBox} with a list of books from the database.
     * Each book is displayed as a combination of its ID and title.
     *
     * @param dropdown       the {@link ComboBox} to populate.
     * @param bookController the {@link BookController} used to retrieve books.
     */
    private static void populateBookDropdown(ComboBox<String> dropdown, BookController bookController) {
        try {
            List<Book> books = bookController.getAllBooks();
            ObservableList<String> bookIds = FXCollections.observableArrayList();
            for (Book book : books) {
                bookIds.add(book.getId() + " - " + book.getTitle());
            }
            dropdown.setItems(bookIds);
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
