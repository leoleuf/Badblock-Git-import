package fr.badblock.protocol.buffers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ByteFakeOutputStream extends ByteOutputStream {
	private List<Byte> bytes = new ArrayList<>();
	
	public ByteFakeOutputStream() {
		super(null);
	}
	
	@Override
	public void writeByte(byte value) throws IOException {
		bytes.add(value);
	}
	
	@Override
	public void writeBytes(byte... values) throws IOException {
		for(byte value : values)
			bytes.add(value);
	}
	
	@Override
	public void write(int b) throws IOException {
		writeByte( (byte) b );
	}
	
	public int size(){
		return bytes.size();
	}
	
	public void pastResult(ByteOutputStream stream) throws IOException {
		byte[] bytes = new byte[this.bytes.size()];
		
		for(int i=0;i<bytes.length;i++){
			bytes[i] = this.bytes.get(i);
		}
		
		stream.writeBytes(bytes);
	}
}
