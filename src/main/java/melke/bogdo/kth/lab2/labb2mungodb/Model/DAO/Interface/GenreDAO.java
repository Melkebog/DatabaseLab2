package melke.bogdo.kth.lab2.labb2mungodb.Model.DAO.Interface;

import melke.bogdo.kth.lab2.labb2mungodb.Model.Genre;

import java.util.List;

/**
 * Interface for managing genre-related database operations.
 * Provides a method for retrieving all genres from the database.
 */
public interface GenreDAO {

    /**
     * Retrieves all genres from the database.
     *
     * @return a {@link List} of {@link Genre} objects representing all available genres.
     */
    List<Genre> getAllGenres();
}
