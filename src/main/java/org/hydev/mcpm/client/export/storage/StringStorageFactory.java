package org.hydev.mcpm.client.export.storage;

import org.hydev.mcpm.client.export.ExportPluginsInput;
import org.hydev.mcpm.client.export.ImportInput;


/**
 * Factory for creating StringStorage instances for ExportInteractor
 */
public class StringStorageFactory {
    /**
     * Create a StringStorage based on the export type.
     *
     * @param input The export query
     * @return a StringStorage satisfying the query
     */
    public static StringStorage createStringStorage(ExportPluginsInput input) {
        return switch (input.type()) {
            case "file" -> input.out() == null ?
                    new FixedFileStorage("export.json") : new FixedFileStorage(input.out());

            case "pastebin" -> input.out() == null ? new PasteBinStorage() : new PasteBinStorage(input.out());
            case "literal" -> new StringLiteralStorage();
            default -> throw new IllegalArgumentException("Invalid output type");
        };
    }

    /**
     * Create a StringStorage based on the input type.
     *
     * @param input The input query
     * @return a StringStorage satisfying the query
     */
    public static StringStorage createStringStorage(ImportInput input) {
        return switch (input.type()) {
            case "file" -> input.input() == null ?
                    new FixedFileStorage("export.json") : new FixedFileStorage(input.input());

            case "pastebin" -> input.input() == null ? new PasteBinStorage() : new PasteBinStorage(input.input());
            case "literal" -> new StringLiteralStorage();
            default -> throw new IllegalArgumentException("Invalid input type");
        };
    }
}
