package org.hydev.mcpm.client.export.storage;

/**
 * A StringStorage where the token is literally the content itself.
 */
public class StringLiteralStorage implements StringStorage {
    @Override
    public String store(String content) {
        return content;
    }

    @Override
    public String load(String token) {
        return token;
    }
}
