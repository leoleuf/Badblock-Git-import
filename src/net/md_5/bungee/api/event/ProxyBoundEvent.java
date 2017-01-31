package net.md_5.bungee.api.event;

import java.net.InetSocketAddress;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.md_5.bungee.api.plugin.Event;

/**
 * Called when the proxy bind the hostname.
 */
@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class ProxyBoundEvent extends Event
{

    /**
     * The socket address who must be bind.
     */
    private InetSocketAddress address;
    

    public ProxyBoundEvent(InetSocketAddress address)
    {
        this.address = address;
    }
}
