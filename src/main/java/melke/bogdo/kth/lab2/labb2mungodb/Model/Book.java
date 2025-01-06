package melke.bogdo.kth.lab2.labb2mungodb.Model;

import java.util.List;

/**
 * Represents a book stored in the database.
 * A book includes details such as title, ISBN, genre, authors, reviews, ratings, and the user who added it.
 */
public class Book {
    private String id; // MongoDB ObjectId as String
    private String title;
    private String isbn;
    private Genre genre; // Embedded genre
    private List<Author> authors; // Embedded authors
    private List<Review> reviews; // Embedded reviews
    private List<Rating> ratings; // Embedded ratings
    private String userId; // User who added the book

    /**
     * Constructs a new {@code Book} object with the specified details.
     *
     * @param id       the unique identifier for the book (MongoDB ObjectId as a String).
     * @param title    the title of the book.
     * @param isbn     the ISBN of the book.
     * @param genre    the {@link Genre} object representing the book's genre.
     * @param authors  a {@link List} of {@link Author} objects representing the book's authors.
     * @param reviews  a {@link List} of {@link Review} objects associated with the book.
     * @param ratings  a {@link List} of {@link Rating} objects associated with the book.
     * @param userId   the ID of the user who added the book.
     */
    public Book(String id, String title, String isbn, Genre genre, List<Author> authors, List<Review> reviews, List<Rating> ratings, String userId) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.genre = genre;
        this.authors = authors;
        this.reviews = reviews;
        this.ratings = ratings;
        this.userId = userId;
    }

    /**
     * Gets the unique identifier for the book.
     *
     * @return the book's unique identifier (MongoDB ObjectId as a String).
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the book.
     *
     * @param id the new identifier (MongoDB ObjectId as a String).
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the title of the book.
     *
     * @return the book's title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the book.
     *
     * @param title the new title of the book.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the ISBN of the book.
     *
     * @return the book's ISBN.
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Sets the ISBN of the book.
     *
     * @param isbn the new ISBN of the book.
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Gets the genre of the book.
     *
     * @return the {@link Genre} object representing the book's genre.
     */
    public Genre getGenre() {
        return genre;
    }

    /**
     * Sets the genre of the book.
     *
     * @param genre the new {@link Genre} object.
     */
    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    /**
     * Gets the list of reviews associated with the book.
     *
     * @return a {@link List} of {@link Review} objects.
     */
    public List<Review> getReviews() {
        return reviews;
    }

    /**
     * Sets the list of reviews associated with the book.
     *
     * @param reviews the new {@link List} of {@link Review} objects.
     */
    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    /**
     * Gets the list of authors associated with the book.
     *
     * @return a {@link List} of {@link Author} objects.
     */
    public List<Author> getAuthors() {
        return authors;
    }

    /**
     * Sets the list of authors associated with the book.
     *
     * @param authors the new {@link List} of {@link Author} objects.
     */
    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    /**
     * Gets the list of ratings associated with the book.
     *
     * @return a {@link List} of {@link Rating} objects.
     */
    public List<Rating> getRatings() {
        return ratings;
    }

    /**
     * Sets the list of ratings associated with the book.
     *
     * @param ratings the new {@link List} of {@link Rating} objects.
     */
    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    /**
     * Gets the ID of the user who added the book.
     *
     * @return the user's ID.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the ID of the user who added the book.
     *
     * @param userId the new user's ID.
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Calculates the average rating for the book.
     *
     * @return the average rating as a {@code double}, or {@code 0.0} if there are no ratings.
     */
    public double getAverageRating() {
        if (ratings == null || ratings.isEmpty()) {
            return 0.0;
        }
        return ratings.stream()
                .mapToInt(Rating::getRating)
                .average()
                .orElse(0.0);
    }
}
