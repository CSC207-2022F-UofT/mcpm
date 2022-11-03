package org.hydev.mcpm.utils;

import org.apache.hc.client5.http.utils.Hex;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtils {
    private final MessageDigest digest;

    public static final String defaultAlgorithm = "SHA-256";

    public HashUtils() throws NoSuchAlgorithmException {
        this(defaultAlgorithm);
    }

    public HashUtils(String algorithm) throws NoSuchAlgorithmException {
        digest = MessageDigest.getInstance(algorithm);
    }

    // After
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

    public String hash(File file) throws IOException {
        return hash(new FileInputStream(file));
    }

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
