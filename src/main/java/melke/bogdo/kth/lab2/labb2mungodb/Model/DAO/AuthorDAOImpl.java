package melke.bogdo.kth.lab2.labb2mungodb.Model.DAO;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import melke.bogdo.kth.lab2.labb2mungodb.Model.Author;
import melke.bogdo.kth.lab2.labb2mungodb.Model.DAO.Interface.AuthorDAO;
import melke.bogdo.kth.lab2.labb2mungodb.Model.DatabaseConnection;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

/**
 * Implementation of the {@link AuthorDAO} interface for managing authors embedded within books in a MongoDB collection.
 */
public class AuthorDAOImpl implements AuthorDAO {

    private final MongoCollection<Document> booksCollection;

    /**
     * Constructs a new {@code AuthorDAOImpl} and initializes the connection to the "books" collection in the database.
     */
    public AuthorDAOImpl() {
        MongoDatabase database = DatabaseConnection.getDatabase();
        this.booksCollection = database.getCollection("books");
    }

    /**
     * Retrieves all authors associated with a specific book.
     *
     * @param bookId the unique ID of the book whose authors are to be retrieved.
     * @return a {@link List} of {@link Author} objects representing the authors of the specified book.
     */
    @Override
    public List<Author> getAuthorsByBookId(String bookId) {
        Document book = booksCollection.find(eq("_id", bookId)).first();
        if (book != null) {
            List<Document> authorDocs = book.getList("authors", Document.class);
            List<Author> authors = new ArrayList<>();
            for (Document doc : authorDocs) {
                authors.add(new Author(
                        doc.getString("name"),
                        doc.getString("birthdate"),
                        doc.getString("user_id")
                ));
            }
            return authors;
        }
        return new ArrayList<>(); // Return an empty list if the book or authors are not found.
    }

    /**
     * Adds a new author to a specific book.
     *
     * @param bookId the unique ID of the book to which the author will be added.
     * @param author the {@link Author} object containing the author's details.
     */
    @Override
    public void addAuthorToBook(String bookId, Author author) {
        Document authorDoc = new Document("name", author.getName())
                .append("birthdate", author.getBirthdate())
                .append("user_id", author.getUserId());

        booksCollection.updateOne(
                eq("_id", bookId),
                new Document("$push", new Document("authors", authorDoc))
        );
    }

    /**
     * Checks whether an author exists in a specific book.
     *
     * @param bookId    the unique ID of the book.
     * @param name      the name of the author.
     * @param birthdate the birthdate of the author, formatted as "YYYY-MM-DD".
     * @return {@code true} if the author exists in the specified book; {@code false} otherwise.
     */
    @Override
    public boolean authorExistsInBook(String bookId, String name, String birthdate) {
        Document book = booksCollection.find(eq("_id", bookId)).first();
        if (book != null) {
            List<Document> authorDocs = book.getList("authors", Document.class);
            return authorDocs.stream().anyMatch(doc ->
                    doc.getString("name").equals(name) &&
                            doc.getString("birthdate").equals(birthdate)
            );
        }
        return false; // Return false if the book or authors are not found.
    }
}
