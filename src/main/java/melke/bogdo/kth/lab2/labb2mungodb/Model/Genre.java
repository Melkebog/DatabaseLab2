package melke.bogdo.kth.lab2.labb2mungodb.Model;

/**
 * Represents a genre entity embedded in a book document.
 * Each genre has a unique identifier and a name.
 */
public class Genre {

    private int id;
    private String name;

    /**
     * Constructs a new {@code Genre} object with the specified details.
     *
     * @param id   the unique identifier of the genre.
     * @param name the name of the genre.
     */
    public Genre(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Gets the unique identifier of the genre.
     *
     * @return the genre's unique identifier.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the genre.
     *
     * @param id the new unique identifier of the genre.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the name of the genre.
     *
     * @return the genre's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the genre.
     *
     * @param name the new name of the genre.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns a string representation of the {@code Genre} object.
     *
     * @return a string containing the genre's ID and name.
     */
    @Override
    public String toString() {
        return "Genre{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
