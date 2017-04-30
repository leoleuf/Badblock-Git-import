package net.md_5.bungee.protocol;

@SuppressWarnings("serial")
public class OverflowPacketException extends RuntimeException
{

    public OverflowPacketException(String message)
    {
        super( message );
    }
}
