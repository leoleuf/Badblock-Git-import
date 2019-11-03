package fr.badblock.gameapi;

public class GameConstructor {

    private Game game = new Game();

    public GameConstructor(String name, String description)
    {
        setName(name);
        setDescription(description);
    }

    public GameConstructor(String name)
    {
        this(name, null);
    }

    public GameConstructor(AbstractGameHandler handler)
    {
        setGameHandler(handler);
    }

    public GameConstructor setName(String name)
    {
        game.name = name;
        return this;
    }

    public GameConstructor setDescription(String description)
    {
        game.description = description;
        return this;
    }

    public GameConstructor setRequiredPlayers(int requiredPlayers)
    {
        game.requiredPlayers = requiredPlayers;
        return this;
    }

    public GameConstructor setGameHandler(AbstractGameHandler handler)
    {
        game.handler = handler;
        return this;
    }

    // Return the configured game
    public Game getGame() {
        return game;
    }

}
