package fr.badblock.api.utils.other;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class ZipUtils {
	public static byte[] compress(byte[] bytes) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(bytes);
        gzip.close();
        
        return out.toByteArray();
	}
	
	public static byte[] decompress(byte[] bytes) throws IOException {
	    GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(bytes));

	    byte[] buffer = new byte[1024];
	    ByteArrayOutputStream out = new ByteArrayOutputStream();

	    int len;
	    while((len = gis.read(buffer)) > 0) {
	        out.write(buffer, 0, len);
	    }

	    gis.close(); out.close();
	    return out.toByteArray();
	}
	
	public static void main(String[] args){
		try {
			System.out.println(new String(decompress(new byte[]{31, -117, 8, 0, 0, 0, 0, 0, 0, 3, -85, 86, 42, 46, 73, 44, 41, 45, 86, -78, 50, 50, 48, -88, 5, 0, -93, 63, -11, 62, 14, 0, 0, 0})));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
