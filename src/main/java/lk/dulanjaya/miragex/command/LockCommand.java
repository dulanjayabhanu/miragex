package lk.dulanjaya.miragex.command;

import lk.dulanjaya.miragex.crypto.CryptoEngine;
import lk.dulanjaya.miragex.util.ConsoleUtil;
import lk.dulanjaya.miragex.util.FileUtil;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

@Command(
        name = "lock",
        description = "Encrypts a .txt file and saves it as a .mgx file."
)
public class LockCommand implements Callable<Integer> {
    @Parameters(index = "0", description = "Target file name (<file-name>.txt)")
    private String fileName;

    @Parameters(index = "1", description = "Encryption password", defaultValue = "")
    private String password;

    @Parameters(index = "2", description = "Step count (Argon2 iterations, min 1)", defaultValue = "0")
    private int stepCount;

    @Override
    public Integer call() {
        try {
            // if password not provided as argument, prompt securely
            if (password == null || password.isBlank())
                password = ConsoleUtil.promptPassword("Enter password: ");

            // if step count not provided as argument, prompt separately
            if (stepCount < 1) {
                String input = ConsoleUtil.promptInput("Enter step count (recommended 3-5): ");
                stepCount = Integer.parseInt(input.trim());
            }

            Path inputPath = Paths.get(System.getProperty("user.dir"), fileName);
            FileUtil.validateExists(inputPath);

            ConsoleUtil.printInfo("Reading file: " + inputPath.getFileName());
            byte[] plaintext = FileUtil.readBytes(inputPath);

            ConsoleUtil.printInfo("Encrypting... (step count : " + stepCount + ")");
            byte[] encrypted = CryptoEngine.encrypt(plaintext, password, stepCount);

            Path outputPath = FileUtil.resolveEncryptedPath(inputPath).toAbsolutePath();
            FileUtil.writeBytes(outputPath, encrypted);

            ConsoleUtil.printSuccess("Encrypted file save: " + outputPath.getFileName());
            return 0;

        } catch (Exception e) {
            ConsoleUtil.printError(e.getMessage());
            return 1;
        }
    }
}