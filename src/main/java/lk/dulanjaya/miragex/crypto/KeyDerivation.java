package lk.dulanjaya.miragex.crypto;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

import java.security.SecureRandom;

public class KeyDerivation {
    private static final int KEY_LENGTH = 32; // 256bit AES key
    private static final int SALT_LENGTH = 16;
    private static final int ARGON2_MEMORY = 65536; // 64MB
    private static final int ARGON2_PARALLELISM = 2;

    private KeyDerivation() {
    }

    // generate a cryptographically secure random salt
    public static byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    // derives a 256bit AES key from the given password, salt, and step count
    // `stepCount` is used as the Argon2 iteration count
    public static byte[] deriveKey(String password, byte[] salt, int stepCount) {
        if (password == null || password.isBlank())
            throw new IllegalArgumentException("Password must not be empty.");
        if (stepCount < 1)
            throw new IllegalArgumentException("Step count must be al least 1.");

        Argon2Parameters params = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withSalt(salt)
                .withMemoryAsKB(ARGON2_MEMORY)
                .withIterations(stepCount)
                .withParallelism(ARGON2_PARALLELISM)
                .build();

        Argon2BytesGenerator generator = new Argon2BytesGenerator();
        generator.init(params);

        byte[] key = new byte[KEY_LENGTH];
        generator.generateBytes(password.toCharArray(), key);
        return key;
    }
}
