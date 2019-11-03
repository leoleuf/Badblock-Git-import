package fr.badblock.gameapi.events;

import fr.badblock.gameapi.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerJoinGameEvent extends PlayerEvent {

    public static final HandlerList handlers = new HandlerList();
    private Game game;

    public PlayerJoinGameEvent(Player player, Game game) {
        super(player);
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}
