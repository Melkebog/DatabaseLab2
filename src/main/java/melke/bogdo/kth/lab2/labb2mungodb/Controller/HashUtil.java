package melke.bogdo.kth.lab2.labb2mungodb.Controller;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

/**
 * Utility class for generating secure hash values using the SHA-256 algorithm.
 * This class provides a static method to hash input strings and return their
 * hexadecimal representation, ensuring data integrity and security.
 */
public class HashUtil {

    private static final Logger logger = Logger.getLogger(HashUtil.class.getName());

    /**
     * Hashes the given input string using the SHA-256 cryptographic hash function
     * and returns the resulting hash in hexadecimal format.
     *
     * @param input the string to hash; must not be null or empty.
     * @return the hexadecimal representation of the SHA-256 hash.
     * @throws IllegalArgumentException if the input string is null or empty.
     * @throws RuntimeException if the SHA-256 algorithm is not available in the environment.
     */
    public static String hash(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Input for hashing cannot be null or empty.");
        }

        try {
            // Create a MessageDigest instance for SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            // Compute the SHA-256 hash as a byte array
            byte[] encodedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            // Convert the byte array into a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            logger.info("Hash generated successfully.");
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            logger.severe("SHA-256 algorithm is not available: " + e.getMessage());
            // Rethrow as a runtime exception to avoid forcing callers to handle it
            throw new RuntimeException("Error generating hash: SHA-256 algorithm not available.", e);
        }
    }
}
