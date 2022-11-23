package org.hydev.mcpm.client.database.export;

import java.io.IOException;

/**
 * Basic string content storage
 *
 * @author Peter (https://github.com/MstrPikachu)
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-11-23
 */
public interface StringStorage
{
    /**
     * Store content
     *
     * @param content String content
     * @return Token for retrieval
     */
    String store(String content) throws IOException;

    /**
     * Load content
     *
     * @param token Token for retrieval
     * @return String content
     */
    String load(String token) throws IOException;

    /**
     * Instruction for manually generating the file
     *
     * @return Instructions text
     */
    String instruction();
}
