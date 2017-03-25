package fr.badblock.bungeecord.jni;

@SuppressWarnings("serial")
public class NativeCodeException extends Exception
{

    public NativeCodeException(String message, int reason)
    {
        super( message + " : " + reason );
    }
}
