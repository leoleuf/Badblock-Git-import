package fr.badblock.bungeecord.protocol;

@SuppressWarnings("serial")
public class OverflowPacketException extends RuntimeException
{

    public OverflowPacketException(String message)
    {
        super( message );
    }
}
