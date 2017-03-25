package fr.badblock.bungeecord.compress;

import fr.badblock.bungeecord.jni.NativeCode;
import fr.badblock.bungeecord.jni.zlib.BungeeZlib;
import fr.badblock.bungeecord.jni.zlib.JavaZlib;
import fr.badblock.bungeecord.jni.zlib.NativeZlib;

public class CompressFactory
{

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static final NativeCode<BungeeZlib> zlib = new NativeCode( "native-compress", JavaZlib.class, NativeZlib.class );
}
