package fr.badblock.gameapi.events;

import fr.badblock.gameapi.Game;
import fr.badblock.gameapi.GameState;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameStateChangeEvent extends Event {

    public static final HandlerList handlers = new HandlerList();
    private Game game;
    private GameState state;

    public GameStateChangeEvent(Game game) {
        this.game = game;
        state = game.getState();
    }

    public Game getGame()
    {
        return game;
    }

    public GameState getState()
    {
        return state;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}
