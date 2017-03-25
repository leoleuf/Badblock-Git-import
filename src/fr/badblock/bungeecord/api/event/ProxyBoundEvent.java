package fr.badblock.bungeecord.api.event;

import java.net.InetSocketAddress;

import fr.badblock.bungeecord.api.plugin.Event;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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
