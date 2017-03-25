package fr.badblock.bungeecord.forge;

import java.util.ArrayDeque;

import fr.badblock.bungeecord.UserConnection;
import fr.badblock.bungeecord.api.config.ServerInfo;
import fr.badblock.bungeecord.forge.ForgeLogger.LogDirection;
import fr.badblock.bungeecord.netty.ChannelWrapper;
import fr.badblock.bungeecord.protocol.packet.PluginMessage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Contains data about the Forge server, and handles the handshake.
 */
@RequiredArgsConstructor
public class ForgeServerHandler
{

    private final UserConnection con;
    @Getter
    private final ChannelWrapper ch;

    @Getter(AccessLevel.PACKAGE)
    private final ServerInfo serverInfo;

    @Getter
    private ForgeServerHandshakeState state = ForgeServerHandshakeState.START;

    @Getter
    private boolean serverForge = false;

    private final ArrayDeque<PluginMessage> packetQueue = new ArrayDeque<PluginMessage>();

    /**
     * Handles any {@link PluginMessage} that contains a FML Handshake or Forge
     * Register.
     *
     * @param message The message to handle.
     * @throws IllegalArgumentException If the wrong packet is sent down.
     */
    public void handle(PluginMessage message) throws IllegalArgumentException
    {
        if ( !message.getTag().equalsIgnoreCase( ForgeConstants.FML_HANDSHAKE_TAG ) && !message.getTag().equalsIgnoreCase( ForgeConstants.FORGE_REGISTER ) )
        {
            throw new IllegalArgumentException( "Expecting a Forge REGISTER or FML Handshake packet." );
        }

        message.setAllowExtendedPacket( true ); // FML allows extended packets so this must be enabled
        ForgeServerHandshakeState prevState = state;
        packetQueue.add( message );
        state = state.send( message, con );
        if ( state != prevState ) // send packets
        {
            synchronized ( packetQueue )
            {
                while ( !packetQueue.isEmpty() )
                {
                    ForgeLogger.logServer( LogDirection.SENDING, prevState.name(), packetQueue.getFirst() );
                    con.getForgeClientHandler().receive( packetQueue.removeFirst() );
                }
            }
        }
    }

    /**
     * Receives a {@link PluginMessage} from ForgeClientData to pass to Server.
     *
     * @param message The message to being received.
     */
    public void receive(PluginMessage message) throws IllegalArgumentException
    {
        state = state.handle( message, ch );
    }

    /**
     * Flags the server as a Forge server. Cannot be used to set a server back
     * to vanilla (there would be no need)
     */
    public void setServerAsForgeServer()
    {
        serverForge = true;
    }
}
