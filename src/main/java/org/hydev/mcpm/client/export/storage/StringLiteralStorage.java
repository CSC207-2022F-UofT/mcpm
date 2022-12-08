package org.hydev.mcpm.client.export.storage;

import java.io.IOException;

/**
 * A StringStorage where the token is literally the content itself.
 */
public class StringLiteralStorage implements StringStorage {
    @Override
    public String store(String content) {
        return content;
    }

    @Override
    public String load(String token) throws IOException {
        return token;
    }

    @Override
    public String instruction() {
        return "Your content is saved in the memory. It will disappear";
    }
}
