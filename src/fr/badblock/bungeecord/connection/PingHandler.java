package fr.badblock.bungeecord.connection;

import com.google.gson.Gson;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import fr.badblock.bungeecord.BungeeCord;
import fr.badblock.bungeecord.api.Callback;
import fr.badblock.bungeecord.api.ProxyServer;
import fr.badblock.bungeecord.api.ServerPing;
import fr.badblock.bungeecord.api.config.ServerInfo;
import fr.badblock.bungeecord.netty.ChannelWrapper;
import fr.badblock.bungeecord.netty.PacketHandler;
import fr.badblock.bungeecord.netty.PipelineUtils;
import fr.badblock.bungeecord.protocol.MinecraftDecoder;
import fr.badblock.bungeecord.protocol.MinecraftEncoder;
import fr.badblock.bungeecord.protocol.Protocol;
import fr.badblock.bungeecord.protocol.packet.Handshake;
import fr.badblock.bungeecord.protocol.packet.StatusRequest;
import fr.badblock.bungeecord.protocol.packet.StatusResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PingHandler extends PacketHandler
{

    private final ServerInfo target;
    private final Callback<ServerPing> callback;
    private final int protocol;
    private ChannelWrapper channel;

    @SuppressWarnings("deprecation")
	@Override
    public void connected(ChannelWrapper channel) throws Exception
    {
        this.channel = channel;
        MinecraftEncoder encoder = new MinecraftEncoder( Protocol.HANDSHAKE, false, protocol );

        channel.getHandle().pipeline().addAfter( PipelineUtils.FRAME_DECODER, PipelineUtils.PACKET_DECODER, new MinecraftDecoder( Protocol.STATUS, false, ProxyServer.getInstance().getProtocolVersion() ) );
        channel.getHandle().pipeline().addAfter( PipelineUtils.FRAME_PREPENDER, PipelineUtils.PACKET_ENCODER, encoder );

        channel.write( new Handshake( protocol, target.getAddress().getHostString(), target.getAddress().getPort(), 1 ) );

        encoder.setProtocol( Protocol.STATUS );
        channel.write( new StatusRequest() );
    }

    @Override
    public void exception(Throwable t) throws Exception
    {
        callback.done( null, t );
    }

    @Override
    @SuppressFBWarnings("UWF_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR")
    public void handle(StatusResponse statusResponse) throws Exception
    {
        Gson gson = BungeeCord.getInstance().gson;
        callback.done( gson.fromJson( statusResponse.getResponse(), ServerPing.class ), null );
        channel.close();
    }

    @Override
    public String toString()
    {
        return "[Ping Handler] -> " + target.getName();
    }
}
