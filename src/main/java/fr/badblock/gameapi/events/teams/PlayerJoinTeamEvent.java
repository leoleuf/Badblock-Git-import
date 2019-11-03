package fr.badblock.gameapi.events.teams;

import fr.badblock.gameapi.GameTeam;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerJoinTeamEvent extends PlayerEvent {

    public static final HandlerList handlers = new HandlerList();
    private GameTeam team;

    public PlayerJoinTeamEvent(Player player, GameTeam team) {
        super(player);
        this.team = team;
    }

    public GameTeam getTeam() {
        return team;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}
