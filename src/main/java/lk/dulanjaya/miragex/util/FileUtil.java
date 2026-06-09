package lk.dulanjaya.miragex.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileUtil {
    private static final String ENCRYPTED_EXTENSION = ".mgx";

    private FileUtil() {
    }

    // read all the bytes from the given file path
    public static byte[] readBytes(Path path) throws IOException {
        validateExists(path);
        return Files.readAllBytes(path);
    }

    // read all text content from the given file path
    public static String readText(Path path) throws IOException {
        validateExists(path);
        return Files.readString(path);
    }

    // writes bytes to the given path. Overwrites if exists
    public static void writeBytes(Path path, byte[] data) throws IOException {
        Files.write(
                path,
                data,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        );
    }

    // writes text to the given path. Overwrites if exists
    public static void writeText(Path path, String content) throws IOException {
        Files.writeString(
                path,
                content,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        );
    }

    // resolves the output `.mgx` path for a given input file (`<file-name>.txt` -> `<file-name>.mgx`)
    public static Path resolveEncryptedPath(Path inputPath) {
        String fileName = inputPath.getFileName().toString();
        int doIndex = fileName.lastIndexOf('.');
        String baseName = doIndex != -1 ? fileName.substring(0, doIndex) : fileName;
        return inputPath.resolveSibling(baseName + ENCRYPTED_EXTENSION);
    }

    // resolves the decrypted output `.txt` path for given `.mgx` file (`<file-name>.mgx` -> `<file-name>.txt`)
    public static Path resolveDecryptedPath(Path inputPath) {
        String fileName = inputPath.getFileName().toString();
        int dotIndex = fileName.lastIndexOf('.');
        String baseName = dotIndex != -1 ? fileName.substring(0, dotIndex) : fileName;
        return inputPath.resolveSibling(baseName + ".txt");
    }

    // validates that a file exists and is a regular file
    public static void validateExists(Path path) throws IOException {
        if (!Files.exists(path))
            throw new IOException("File not found: " + path.toAbsolutePath());

        if (!Files.isRegularFile(path))
            throw new IOException("Not a regular file : " + path.toAbsolutePath());
    }
}
