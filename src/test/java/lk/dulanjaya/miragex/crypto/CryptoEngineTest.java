package lk.dulanjaya.miragex.crypto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CryptoEngineTest {

    private static final String PASSWORD   = "testpassword";
    private static final int    STEP_COUNT = 1;

    @Test
    void testEncryptDecryptRoundTrip() throws Exception {
        String original = "username: admin\npassword: secret123";
        byte[] encrypted = CryptoEngine.encrypt(original.getBytes(), PASSWORD, STEP_COUNT);
        byte[] decrypted = CryptoEngine.decrypt(encrypted, PASSWORD, STEP_COUNT);
        assertEquals(original, new String(decrypted));
    }

    @Test
    void testWrongPasswordFails() {
        assertThrows(Exception.class, () -> {
            byte[] encrypted = CryptoEngine.encrypt("sensitive data".getBytes(), PASSWORD, STEP_COUNT);
            CryptoEngine.decrypt(encrypted, "wrongpassword", STEP_COUNT);
        });
    }

    @Test
    void testWrongStepCountFails() {
        assertThrows(Exception.class, () -> {
            byte[] encrypted = CryptoEngine.encrypt("sensitive data".getBytes(), PASSWORD, STEP_COUNT);
            CryptoEngine.decrypt(encrypted, PASSWORD, 2);
        });
    }

    @Test
    void testEncryptProducesUniqueOutputs() throws Exception {
        byte[] plaintext = "same content".getBytes();
        byte[] encrypted1 = CryptoEngine.encrypt(plaintext, PASSWORD, STEP_COUNT);
        byte[] encrypted2 = CryptoEngine.encrypt(plaintext, PASSWORD, STEP_COUNT);
        assertNotEquals(new String(encrypted1), new String(encrypted2));
    }

    @Test
    void testEmptyPasswordThrows() {
        assertThrows(Exception.class, () ->
                CryptoEngine.encrypt("data".getBytes(), "", STEP_COUNT)
        );
    }

    @Test
    void testInvalidStepCountThrows() {
        assertThrows(Exception.class, () ->
                CryptoEngine.encrypt("data".getBytes(), PASSWORD, 0)
        );
    }
}