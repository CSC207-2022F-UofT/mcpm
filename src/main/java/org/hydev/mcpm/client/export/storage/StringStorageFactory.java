package org.hydev.mcpm.client.export.storage;

import org.hydev.mcpm.client.export.ExportPluginsInput;


/**
 *
 */
public class StringStorageFactory {
    public static StringStorage createStringStorage(ExportPluginsInput input) {
        return switch (input.type()) {
            case "file" -> new FixedFileStorage(input.out());
            case "pastebin" -> input.out() == null ? new PasteBinStorage() : new PasteBinStorage(input.out());
            case "literal" -> new StringLiteralStorage();
            default -> throw new IllegalArgumentException("Invalid output type");
        };
    }
}
