package org.hydev.mcpm.utils;

import org.apache.hc.client5.http.utils.Hex;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Convenience class for quickly hashing data.
 */
public class HashUtils {
    private final MessageDigest digest;

    public static final String defaultAlgorithm = "SHA-256";

    /**
     * Default constructor uses the SHA-256 algorithm.
     */
    public HashUtils() {
        try {
            digest = MessageDigest.getInstance(defaultAlgorithm);
        }
        catch (NoSuchAlgorithmException e) {
            // Would never happen
            throw new RuntimeException(e);
        }
    }

    /**
     * Constructor initializes a new HashUtils object.
     *
     * @param algorithm The algorithm identifier passed to MessageDigest.
     * @throws NoSuchAlgorithmException Thrown in the case that the algorithm doesn't ship with an implementation.
     */
    public HashUtils(String algorithm) throws NoSuchAlgorithmException {
        digest = MessageDigest.getInstance(algorithm);
    }

    /**
     * Hashes the data in InputStream and returns the relevant hash.
     *
     * @param inputStream The input stream to read data from.
     * @return The hex-decoded hash string corresponding to the data in inputStream.
     * @throws IOException Thrown in the case that the inputStream doesn't support a required operation.
     */
    public String hash(InputStream inputStream) throws IOException {
        try (var stream = new BufferedInputStream(inputStream)) {
            byte[] buffer = new byte[8196];
            int count = stream.read(buffer);
            while (count > 0) {
                digest.update(buffer, 0, count);

                count = stream.read(buffer);
            }

            return Hex.encodeHexString(digest.digest());
        }
    }


    /**
     * Hashes the data in the provided File and returns the relevant hash.
     *
     * @param file The input file to read data from.
     * @return The hex-decoded hash string corresponding to the data in the file.
     * @throws IOException Thrown in the case that the file doesn't exist or allow certain reading operations.
     */
    public String hash(File file) throws IOException {
        return hash(new FileInputStream(file));
    }

    /**
     * Hashes the data contained in the String (encoded as UTF-8).
     *
     * @param text The input string which will be hashed.
     * @return The hex-decoded hash string corresponding to the data in the string.
     */
    public String hash(String text) {
        try {
            var stream = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));

            return hash(stream);
        } catch (IOException e) {
            // The hash call should not fail with an IOException.
            throw new RuntimeException(e);
        }
    }
}
