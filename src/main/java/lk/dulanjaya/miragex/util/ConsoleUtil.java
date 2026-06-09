package lk.dulanjaya.miragex.util;

import java.io.Console;

public class ConsoleUtil {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_CYAN = "\u001B[336m";
    private static final String ANSI_BOLD = "\u001B[1m";

    private ConsoleUtil() {
    }

    public static void printSuccess(String message) {
        System.out.println(ANSI_GREEN + "[OK] " + message + ANSI_RESET);
    }

    public static void printError(String message) {
        System.out.println(ANSI_RED + "[ERROR] " + message + ANSI_RESET);
    }

    public static void printWarning(String message) {
        System.out.println(ANSI_YELLOW + "[WARN] " + message + ANSI_RESET);
    }

    public static void printInfo(String message) {
        System.out.println(ANSI_CYAN + "[INFO] " + message + ANSI_RESET);
    }

    public static void printContent(String content) {
        System.out.println("\n" + ANSI_BOLD + "--- Decrypted Content ---" + ANSI_RESET);
        System.out.println(content);
        System.out.println(ANSI_BOLD + "-------------------------" + ANSI_RESET + "\n");
    }

    // prompt the user for a password securely with hidden input
    // falls back to plain input if no system console is available (example - IDE)
    public static String promptPassword(String prompt) {
        Console console = System.console();
        if (console != null) {
            char[] password = console.readPassword(ANSI_YELLOW + prompt + ANSI_RESET);
            return new String(password);
        }
        // fallback for IDE runners
        System.out.print(ANSI_YELLOW + prompt + ANSI_RESET);
        return new java.util.Scanner(System.in).nextLine();
    }
}