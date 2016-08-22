package fr.badblock.protocol.buffers;

import java.io.IOException;

public class ByteFakeInputStream extends ByteInputStream {
	private byte[] bytes;
	private int	   cursor = 0;
	
	public ByteFakeInputStream(byte[] bytes) {
		super(null);
		
		this.bytes = bytes;
	}

	@Override
	public byte readByte() throws IOException {
		return bytes[cursor];
	}
	
	@Override
	public int read() throws IOException {
		return (int) readByte();
	}
}
