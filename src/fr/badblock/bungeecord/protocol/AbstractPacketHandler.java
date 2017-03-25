package fr.badblock.bungeecord.protocol;

import fr.badblock.bungeecord.protocol.packet.BossBar;
import fr.badblock.bungeecord.protocol.packet.Chat;
import fr.badblock.bungeecord.protocol.packet.ClientSettings;
import fr.badblock.bungeecord.protocol.packet.ClientStatus;
import fr.badblock.bungeecord.protocol.packet.EncryptionRequest;
import fr.badblock.bungeecord.protocol.packet.EncryptionResponse;
import fr.badblock.bungeecord.protocol.packet.Handshake;
import fr.badblock.bungeecord.protocol.packet.KeepAlive;
import fr.badblock.bungeecord.protocol.packet.Kick;
import fr.badblock.bungeecord.protocol.packet.LegacyHandshake;
import fr.badblock.bungeecord.protocol.packet.LegacyPing;
import fr.badblock.bungeecord.protocol.packet.Login;
import fr.badblock.bungeecord.protocol.packet.LoginRequest;
import fr.badblock.bungeecord.protocol.packet.LoginSuccess;
import fr.badblock.bungeecord.protocol.packet.PingPacket;
import fr.badblock.bungeecord.protocol.packet.PlayerListHeaderFooter;
import fr.badblock.bungeecord.protocol.packet.PlayerListItem;
import fr.badblock.bungeecord.protocol.packet.PluginMessage;
import fr.badblock.bungeecord.protocol.packet.Respawn;
import fr.badblock.bungeecord.protocol.packet.ScoreboardDisplay;
import fr.badblock.bungeecord.protocol.packet.ScoreboardObjective;
import fr.badblock.bungeecord.protocol.packet.ScoreboardScore;
import fr.badblock.bungeecord.protocol.packet.SetCompression;
import fr.badblock.bungeecord.protocol.packet.StatusRequest;
import fr.badblock.bungeecord.protocol.packet.StatusResponse;
import fr.badblock.bungeecord.protocol.packet.TabCompleteRequest;
import fr.badblock.bungeecord.protocol.packet.TabCompleteResponse;
import fr.badblock.bungeecord.protocol.packet.Team;
import fr.badblock.bungeecord.protocol.packet.Title;

public abstract class AbstractPacketHandler
{

    public void handle(LegacyPing ping) throws Exception
    {
    }

    public void handle(TabCompleteResponse tabResponse) throws Exception
    {
    }

    public void handle(PingPacket ping) throws Exception
    {
    }

    public void handle(StatusRequest statusRequest) throws Exception
    {
    }

    public void handle(StatusResponse statusResponse) throws Exception
    {
    }

    public void handle(Handshake handshake) throws Exception
    {
    }

    public void handle(KeepAlive keepAlive) throws Exception
    {
    }

    public void handle(Login login) throws Exception
    {
    }

    public void handle(Chat chat) throws Exception
    {
    }

    public void handle(Respawn respawn) throws Exception
    {
    }

    public void handle(LoginRequest loginRequest) throws Exception
    {
    }

    public void handle(ClientSettings settings) throws Exception
    {
    }

    public void handle(ClientStatus clientStatus) throws Exception
    {
    }

    public void handle(PlayerListItem playerListItem) throws Exception
    {
    }

    public void handle(PlayerListHeaderFooter playerListHeaderFooter) throws Exception
    {
    }

    public void handle(TabCompleteRequest tabComplete) throws Exception
    {
    }

    public void handle(ScoreboardObjective scoreboardObjective) throws Exception
    {
    }

    public void handle(ScoreboardScore scoreboardScore) throws Exception
    {
    }

    public void handle(EncryptionRequest encryptionRequest) throws Exception
    {
    }

    public void handle(ScoreboardDisplay displayScoreboard) throws Exception
    {
    }

    public void handle(Team team) throws Exception
    {
    }

    public void handle(Title title) throws Exception
    {
    }

    public void handle(PluginMessage pluginMessage) throws Exception
    {
    }

    public void handle(Kick kick) throws Exception
    {
    }

    public void handle(EncryptionResponse encryptionResponse)
    {
    }

    public void handle(LoginSuccess loginSuccess) throws Exception
    {
    }

    public void handle(LegacyHandshake legacyHandshake) throws Exception
    {
    }

    public void handle(SetCompression setCompression) throws Exception
    {
    }

    public void handle(BossBar bossBar) throws Exception
    {
    }
}
