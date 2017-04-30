package net.md_5.bungee.api.event;

import java.net.InetSocketAddress;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.md_5.bungee.api.plugin.Event;

/**
 * Called when the proxy can't bind the hostname.
 */
@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class ProxyUnableToBindEvent extends Event
{

    /**
     * The socket address who must be bind.
     */
    private InetSocketAddress address;
    /**
     * The throw error.
     */
    private Throwable throwable;
    

    public ProxyUnableToBindEvent(InetSocketAddress address, Throwable throwable)
    {
        this.address = address;
        this.throwable = throwable;
    }
}
