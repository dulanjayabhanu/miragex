package lk.dulanjaya.miragex;

import lk.dulanjaya.miragex.command.LockCommand;
import lk.dulanjaya.miragex.command.ReadCommand;
import lk.dulanjaya.miragex.command.UnlockCommand;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(
        name = "miragex",
        version = "MirageX 1.1.1",
        mixinStandardHelpOptions = true,
        subcommands = {
                LockCommand.class,
                ReadCommand.class,
                UnlockCommand.class
        },
        description = {
                "",
                "@|bold,green MirageX - Secure File Encryption Tool|@",
                "",
                "  Protect your sensitive .txt files using AES-256-GCM encryption",
                "  with Argon2id key derivation — fully offline, no data leaves your machine.",
                "",
                "@|bold Features:|@",
                "  - Industry-standard AES-256-GCM authenticated encryption",
                "  - Argon2id password-based key derivation (resistant to brute force)",
                "  - Unique encryption output every run (random salt + IV)",
                "  - Tamper detection built-in",
                "  - 100%% offline — no network, no cloud, no tracking",
                "",
                "@|bold Usage:|@",
                "  miragex lock   <file> <password> <steps>  Encrypt a file",
                "  miragex read   <file> <password> <steps>  Decrypt to terminal",
                "  miragex unlock <file> <password> <steps>  Decrypt to new file",
                "",
                "@|bold Developer:|@  Dulanjaya | github.com/dulanjayabhanu",
                "@|bold License:|@    MIT",
                ""
        }
)
public class MirageXApp implements Runnable {
    @Override
    public void run() {
        CommandLine.usage(this, System.out);
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new MirageXApp()).execute(args);
        System.exit(exitCode);
    }
}
