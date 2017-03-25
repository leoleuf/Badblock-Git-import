package fr.badblock.bungeecord.connection;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.md_5.bungee.connection.CancelSendSignal;

@SuppressWarnings("serial")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CancelSendSignal extends Error
{

    public static final CancelSendSignal INSTANCE = new CancelSendSignal();

    @Override
    public Throwable initCause(Throwable cause)
    {
        return this;
    }

    @Override
    public Throwable fillInStackTrace()
    {
        return this;
    }
}
