package melke.bogdo.kth.lab2.labb2mungodb.View;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import melke.bogdo.kth.lab2.labb2mungodb.Controller.BookController;
import melke.bogdo.kth.lab2.labb2mungodb.Model.Author;
import melke.bogdo.kth.lab2.labb2mungodb.Model.Genre;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides the graphical interface for adding a new book to the system.
 * Users can input the book's title, ISBN, genre, and authors through this view.
 */
public class AddBookView {

    /**
     * Creates the "Add Book" view layout.
     * Allows users to input book details, select authors and genre, and submit the book information.
     *
     * @param stage the primary {@link Stage} of the application.
     * @return a {@link VBox} containing the layout of the "Add Book" view.
     */
    public static VBox create(Stage stage) {
        // Predefined list of authors
        List<Author> predefinedAuthors = List.of(
                new Author("J.K. Rowling", "1965-07-31", null),
                new Author("J.R.R. Tolkien", "1892-01-03", null),
                new Author("George Orwell", "1903-06-25", null),
                new Author("Jane Austen", "1775-12-16", null)
        );

        // Predefined list of genres
        List<Genre> predefinedGenres = List.of(
                new Genre(1, "Fantasy"),
                new Genre(2, "Science Fiction"),
                new Genre(3, "Historical Fiction"),
                new Genre(4, "Non-Fiction")
        );

        // Input fields for book details
        TextField titleField = new TextField();
        titleField.setPromptText("Enter book title");

        TextField isbnField = new TextField();
        isbnField.setPromptText("Enter ISBN");

        // Dropdown for selecting predefined genres
        ComboBox<Genre> genreComboBox = new ComboBox<>();
        ObservableList<Genre> availableGenres = FXCollections.observableArrayList(predefinedGenres);
        genreComboBox.setItems(availableGenres);
        genreComboBox.setPromptText("Select a genre");
        configureGenreComboBox(genreComboBox);

        // Dropdown for selecting predefined authors
        ComboBox<Author> authorComboBox = new ComboBox<>();
        ObservableList<Author> availableAuthors = FXCollections.observableArrayList(predefinedAuthors);
        authorComboBox.setItems(availableAuthors);
        authorComboBox.setPromptText("Select an author");
        configureAuthorComboBox(authorComboBox);

        // ListView for displaying selected authors
        ListView<Author> selectedAuthorsListView = new ListView<>();
        ObservableList<Author> selectedAuthors = FXCollections.observableArrayList();
        selectedAuthorsListView.setItems(selectedAuthors);
        selectedAuthorsListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Author author, boolean empty) {
                super.updateItem(author, empty);
                setText(empty || author == null ? null : author.getName());
            }
        });

        // Add author button
        Button addAuthorButton = new Button("Add Author to Book");
        addAuthorButton.setOnAction(e -> handleAddAuthor(authorComboBox, selectedAuthors));

        // Submit and Back buttons
        Button submitButton = new Button("Submit");
        Button backButton = new Button("Back");

        submitButton.setOnAction(e -> handleSubmit(titleField, isbnField, genreComboBox, selectedAuthors, stage));
        backButton.setOnAction(e -> SceneManager.showUserMenu());

        // Layout
        HBox authorControls = new HBox(10, authorComboBox, addAuthorButton);
        VBox layout = new VBox(10, new Label("Add Book"), titleField, isbnField, genreComboBox,
                new Label("Authors"), authorControls, selectedAuthorsListView, submitButton, backButton);
        layout.setPadding(new Insets(20));

        return layout;
    }

    /**
     * Configures the {@link ComboBox} for selecting genres.
     *
     * @param genreComboBox the {@link ComboBox} to configure.
     */
    private static void configureGenreComboBox(ComboBox<Genre> genreComboBox) {
        genreComboBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Genre genre, boolean empty) {
                super.updateItem(genre, empty);
                setText(empty || genre == null ? null : genre.getName());
            }
        });
        genreComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Genre genre, boolean empty) {
                super.updateItem(genre, empty);
                setText(empty || genre == null ? null : genre.getName());
            }
        });
    }

    /**
     * Configures the {@link ComboBox} for selecting authors.
     *
     * @param authorComboBox the {@link ComboBox} to configure.
     */
    private static void configureAuthorComboBox(ComboBox<Author> authorComboBox) {
        authorComboBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Author author, boolean empty) {
                super.updateItem(author, empty);
                setText(empty || author == null ? null : author.getName());
            }
        });
        authorComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Author author, boolean empty) {
                super.updateItem(author, empty);
                setText(empty || author == null ? null : author.getName());
            }
        });
    }

    /**
     * Handles adding an author to the selected authors list.
     *
     * @param authorComboBox  the {@link ComboBox} for selecting authors.
     * @param selectedAuthors the list of selected authors.
     */
    private static void handleAddAuthor(ComboBox<Author> authorComboBox, ObservableList<Author> selectedAuthors) {
        Author selectedAuthor = authorComboBox.getSelectionModel().getSelectedItem();
        if (selectedAuthor == null) {
            showAlert(Alert.AlertType.ERROR, "Please select an author.");
        } else if (!selectedAuthors.contains(selectedAuthor)) {
            selectedAuthors.add(selectedAuthor);
        } else {
            showAlert(Alert.AlertType.WARNING, "Author already added.");
        }
    }

    /**
     * Handles the submission of the book details to the system.
     *
     * @param titleField       the {@link TextField} for the book's title.
     * @param isbnField        the {@link TextField} for the book's ISBN.
     * @param genreComboBox    the {@link ComboBox} for selecting the book's genre.
     * @param selectedAuthors  the list of selected authors.
     * @param stage            the current {@link Stage} of the application.
     */
    private static void handleSubmit(TextField titleField, TextField isbnField, ComboBox<Genre> genreComboBox,
                                     ObservableList<Author> selectedAuthors, Stage stage) {
        String title = titleField.getText();
        String isbn = isbnField.getText();
        Genre selectedGenre = genreComboBox.getSelectionModel().getSelectedItem();

        if (title.isEmpty() || isbn.isEmpty() || selectedGenre == null || selectedAuthors.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "All fields must be filled, and at least one author must be selected.");
            return;
        }

        BookController bookController = new BookController();
        try {
            bookController.addBook(title, isbn, selectedGenre, new ArrayList<>(selectedAuthors));
            showAlert(Alert.AlertType.INFORMATION, "Book added successfully.");
            SceneManager.showUserMenu();
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Failed to add book: " + ex.getMessage());
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
