package lk.dulanjaya.miragex.command;

import lk.dulanjaya.miragex.crypto.CryptoEngine;
import lk.dulanjaya.miragex.util.ConsoleUtil;
import lk.dulanjaya.miragex.util.FileUtil;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

@Command (
        name = "read",
        description = "Decrypts a .mgx file and prints the content to the terminal."
)
public class ReadCommand implements Callable<Integer> {
    @Parameters(index = "0", description = "Target file name (<file-name>.mgx)")
    private String fileName;

    @Parameters(index = "1", description = "Decryption password", defaultValue = "")
    private String password;

    @Parameters(index = "2", description = "Step count used during encryption", defaultValue = "0")
    private int stepCount;

    @Override
    public Integer call() {
        try {
            if (password == null || password.isBlank())
                password = ConsoleUtil.promptPassword("Enter password: ");

            if (stepCount < 1) {
                String input = ConsoleUtil.promptInput("Enter step count: ");
                stepCount = Integer.parseInt(input.trim());
            }

            Path inputPath = Paths.get(System.getProperty("user.dir"), fileName);
            FileUtil.validateExists(inputPath);

            ConsoleUtil.printInfo("Decrypting : " + inputPath.getFileName());
            byte[] encryptedBytes = FileUtil.readBytes(inputPath);
            byte[] plainText = CryptoEngine.decrypt(encryptedBytes, password, stepCount);

            ConsoleUtil.printContent(new String(plainText));
            return 0;

        } catch(Exception e) {
            ConsoleUtil.printError("Decryption failed. Wrong password or step count?");
            return 1;
        }
    }
}
