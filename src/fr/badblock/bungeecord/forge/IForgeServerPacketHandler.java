package fr.badblock.bungeecord.forge;

import fr.badblock.bungeecord.UserConnection;
import fr.badblock.bungeecord.netty.ChannelWrapper;
import fr.badblock.bungeecord.protocol.packet.PluginMessage;

/**
 * An interface that defines a Forge Handshake Server packet.
 *
 * @param <S> The State to transition to.
 */
public interface IForgeServerPacketHandler<S>
{

    /**
     * Handles any {@link fr.badblock.bungeecord.protocol.packet.PluginMessage}
     * packets.
     *
     * @param message The {@link fr.badblock.bungeecord.protocol.packet.PluginMessage}
     * to handle.
     * @param ch The {@link ChannelWrapper} to send packets to.
     * @return The state to transition to.
     */
    public S handle(PluginMessage message, ChannelWrapper ch);

    /**
     * Sends any {@link fr.badblock.bungeecord.protocol.packet.PluginMessage} packets.
     *
     * @param message The {@link fr.badblock.bungeecord.protocol.packet.PluginMessage}
     * to send.
     * @param con The {@link fr.badblock.bungeecord.UserConnection} to send packets to
     * or read from.
     * @return The state to transition to.
     */
    public S send(PluginMessage message, UserConnection con);
}
