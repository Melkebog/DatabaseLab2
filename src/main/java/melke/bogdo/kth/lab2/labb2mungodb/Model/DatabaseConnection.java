package melke.bogdo.kth.lab2.labb2mungodb.Model;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/**
 * Provides a singleton-style connection to the MongoDB database.
 * This class handles the initialization and management of the MongoDB client and database.
 */
public class DatabaseConnection {

    private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "bookdatabase";
    private static MongoClient mongoClient;

    // Static initializer to configure the MongoDB client
    static {
        mongoClient = MongoClients.create(CONNECTION_STRING);
    }

    /**
     * Retrieves the MongoDB database instance.
     *
     * @return the {@link MongoDatabase} object representing the database connection.
     */
    public static MongoDatabase getDatabase() {
        return mongoClient.getDatabase(DATABASE_NAME);
    }

    /**
     * Closes the MongoDB client connection.
     * Should be called when the application is shutting down to release resources.
     */
    public static void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
