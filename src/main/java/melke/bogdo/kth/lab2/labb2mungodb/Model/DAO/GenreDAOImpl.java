package melke.bogdo.kth.lab2.labb2mungodb.Model.DAO;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import melke.bogdo.kth.lab2.labb2mungodb.Model.DAO.Interface.GenreDAO;
import melke.bogdo.kth.lab2.labb2mungodb.Model.DatabaseConnection;
import melke.bogdo.kth.lab2.labb2mungodb.Model.Genre;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link GenreDAO} interface for managing genres in the MongoDB database.
 * This class retrieves genres from the "books" collection by extracting genre details from each book document.
 */
public class GenreDAOImpl implements GenreDAO {

    private static final Logger logger = Logger.getLogger(GenreDAOImpl.class.getName());
    private final MongoCollection<Document> booksCollection;

    /**
     * Constructs a new {@code GenreDAOImpl} and initializes the connection to the "books" collection.
     */
    public GenreDAOImpl() {
        MongoDatabase database = DatabaseConnection.getDatabase();
        this.booksCollection = database.getCollection("books"); // Fetch data from the "books" collection
    }

    /**
     * Retrieves all unique genres from the books in the database.
     * Iterates over each book in the "books" collection, extracts the "genre" field, and compiles a list of distinct genres.
     *
     * @return a {@link List} of {@link Genre} objects representing all unique genres in the database.
     */
    @Override
    public List<Genre> getAllGenres() {
        List<Document> books = booksCollection.find().into(new ArrayList<>());
        List<Genre> genres = new ArrayList<>();
        for (Document book : books) {
            Document genreDoc = book.get("genre", Document.class);
            if (genreDoc != null) {
                genres.add(new Genre(genreDoc.getInteger("id"), genreDoc.getString("name")));
            }
        }
        // Remove duplicates using stream
        return genres.stream().distinct().collect(Collectors.toList());
    }
}
