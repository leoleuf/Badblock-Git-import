package fr.badblock.tech.protocol.buffers;

import fr.badblock.tech.protocol.utils.CompressUtils;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.UUID;

public class ByteInputStream extends InputStream {
    @Getter
    private final InputStream realStream;

    public ByteInputStream(InputStream stream) {
        this.realStream = stream;
    }

    public byte readByte() throws IOException {
        return (byte) realStream.read();
    }

    private int readUnsignedByte() throws IOException {
        return readByte() & 0xFF;
    }

    private byte[] readBytes(int length) throws IOException {
        byte[] result = new byte[length];

        for (int i = 0; i < length; i++)
            result[i] = readByte();

        return result;
    }

    public int readShort() throws IOException {
        int low = readUnsignedShort();
        int high = 0;
        if ((low & 0x8000) != 0) {
            low = low & 0x7FFF;
            high = readUnsignedByte();
        }

        return ((high & 0xFF) << 15) | low;
    }

    private int readUnsignedShort() throws IOException {
        byte[] bytes = readBytes(2);
        short result = 0;

        for (byte aByte : bytes) {
            result <<= 8;
            result |= (int) aByte & 0xFF;
        }

        return result & 0xffff;
    }

    public int readInt() throws IOException {
        return readInt(5);
    }

    private int readInt(int maxBytes) throws IOException {
        int result = 0, bytes = 0;
        byte in;

        while (true) {
            in = readByte();
            result |= (in & 0x7F) << (bytes++ * 7);


            if (bytes > maxBytes) {
                throw new RuntimeException("VarInt too big");
            }

            if ((in & 0x80) != 0x80) break;
        }

        return result;
    }

    private long readLong() throws IOException {
        byte[] bytes = readBytes(8);
        long result = 0;

        for (byte aByte : bytes) {
            result <<= 8;
            result |= (int) aByte & 0xFF;
        }

        return result;
    }

    public boolean readBoolean() throws IOException {
        return readByte() == (byte) 1;
    }

    public UUID readUUID() throws IOException {
        return new UUID(readLong(), readLong());
    }

    public String readUTF() throws IOException {
        int length = readInt();
        return new String(CompressUtils.decompress(readBytes(length)), Charset.forName("UTF8"));
    }

    public String[] readArrayUTF() throws IOException {
        int length = readInt();
        String[] result = new String[length];

        for (int i = 0; i < length; i++)
            result[i] = readUTF();

        return result;
    }

    @Override
    public int read() throws IOException {
        return realStream.read();
    }
}
