package lk.dulanjaya.miragex.crypto;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

public class CryptoEngine {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int GCM_TAG_BITS = 128;
    private static final int IV_LENGTH = 12;

    private CryptoEngine() {
    }

    // encrypts plaintext bytes using `AES-256-GCM`
    // returns the `header bytes` and `ciphertext bytes` combined into one array
    public static byte[] encrypt(byte[] plaintext, String password, int stepCount) throws Exception {
        byte[] salt = KeyDerivation.generateSalt();
        byte[] iv = generateIV();
        byte[] key = KeyDerivation.deriveKey(password, salt, stepCount);

        SecretKey secretKey = new SecretKeySpec(key, ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(GCM_TAG_BITS, iv));

        byte[] ciphertext = cipher.doFinal(plaintext);
        byte[] headerBytes = new FileHeader(salt, iv).toBytes();

        byte[] output = new byte[headerBytes.length + ciphertext.length];
        System.arraycopy(headerBytes, 0, output, 0, headerBytes.length);
        System.arraycopy(ciphertext, 0, output, headerBytes.length, ciphertext.length);

        return output;
    }

    // decrypts an encrypted MirageX file byte array
    // Returns the original plaintext bytes
    public static byte[] decrypt(byte[] encryptedFileBytes, String password, int stepCount) throws Exception {
        if (encryptedFileBytes.length <= FileHeader.HEADER_SIZE)
            throw new IllegalArgumentException("Invalid file: content too short.");

        // parse header
        byte[] headerBytes = new byte[FileHeader.HEADER_SIZE];
        System.arraycopy(encryptedFileBytes, 0, headerBytes, 0, FileHeader.HEADER_SIZE);
        FileHeader header = FileHeader.fromBytes(headerBytes);

        // extract ciphertext
        int ciphertextLength = encryptedFileBytes.length - FileHeader.HEADER_SIZE;
        byte[] ciphertext = new byte[ciphertextLength];
        System.arraycopy(encryptedFileBytes, FileHeader.HEADER_SIZE, ciphertext, 0, ciphertextLength);

        // derive key
        byte[] key = KeyDerivation.deriveKey(password, header.getSalt(), stepCount);

        SecretKey secretKey = new SecretKeySpec(key, ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(GCM_TAG_BITS, header.getIv()));

        return cipher.doFinal(ciphertext);
    }

    public static byte[] generateIV () {
        byte[] iv = new byte[IV_LENGTH];
        new SecureRandom().nextBytes(iv);
        return iv;
    }
}
