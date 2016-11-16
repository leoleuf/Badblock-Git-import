package net.md_5.bungee.protocol;

@SuppressWarnings("serial")
public class BadPacketException extends RuntimeException
{

    public BadPacketException(String message)
    {
        super( message );
    }

    public BadPacketException(String message, Throwable cause)
    {
        super( message, cause );
    }
}
