package org.hydev.mcpm.client.export;

/**
 * The input for the import use case.
 *
 * @param type The type of the import source (pastebin, file, etc)
 * @param input The location of the input
 */
public record ImportInput(
        String type,
        String input
) {
}
