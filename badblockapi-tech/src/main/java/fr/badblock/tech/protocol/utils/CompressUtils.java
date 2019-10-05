package fr.badblock.tech.protocol.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class CompressUtils {
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
}
