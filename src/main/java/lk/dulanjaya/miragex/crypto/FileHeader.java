package lk.dulanjaya.miragex.crypto;

import java.nio.ByteBuffer;

public class FileHeader {
    // represents the binary header stored at the beginning of every `.mgx` file
    public static final byte[] MAGIC = {'M', 'R', 'G', 'X'};
    public static final byte VERSION = (byte) 0x01;

    public static final int MAGIC_LENGTH = 4;
    public static final int VERSION_LENGTH = 1;
    public static final int SALT_LENGTH = 16;
    public static final int IV_LENGTH = 12;
    public static final int HEADER_SIZE = MAGIC_LENGTH + VERSION_LENGTH + SALT_LENGTH + IV_LENGTH;

    private final byte[] salt;
    private final byte[] iv;

    public FileHeader(byte[] salt, byte[] iv) {
        this.salt = salt;
        this.iv = iv;
    }

    // serialize the header into a byte array to prepared to the encrypted file
    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(HEADER_SIZE);
        buffer.put(MAGIC);
        buffer.put((byte) VERSION);
        buffer.put(salt);
        buffer.put(iv);
        return buffer.array();
    }

    // parses a header from the first HEADER_SIZE bytes of an encrypted file.
    public static FileHeader fromBytes(byte[] raw) {
        if (raw.length < HEADER_SIZE)
            throw new IllegalArgumentException("Invalid file: header too short.");

        // validate magic
        for (int i = 0; i < MAGIC_LENGTH; i++) {
            if (raw[i] != MAGIC[i])
                throw new IllegalArgumentException("Invalid file: not a MirageX encrypted file.");
        }

        // validate version
        if (raw[MAGIC_LENGTH] != VERSION)
            throw new IllegalArgumentException("Unsupported MirageX file version: " + raw[MAGIC_LENGTH]);

        ByteBuffer buffer = ByteBuffer.wrap(raw, MAGIC_LENGTH + VERSION_LENGTH, SALT_LENGTH + IV_LENGTH);

        byte[] salt = new byte[SALT_LENGTH];
        byte[] iv = new byte[IV_LENGTH];
        buffer.get(salt);
        buffer.get(iv);

        return new FileHeader(salt, iv);
    }

    public byte[] getSalt() {
        return this.salt;
    }

    public byte[] getIv() {
        return this.iv;
    }
}