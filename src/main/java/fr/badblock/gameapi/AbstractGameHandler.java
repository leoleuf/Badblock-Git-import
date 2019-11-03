package fr.badblock.gameapi;

import fr.badblock.gameapi.tasks.StartingTask;

public abstract class AbstractGameHandler {

    private GameAPI instance = GameAPI.getInstance();

    public void startGame()
    {
        Game game = instance.getGame();
        if (!game.isState(GameState.WAITING) || game.getCache().size() < game.getRequiredPlayers()) return;
        game.setState(GameState.STARTING);
        game.setTask(new StartingTask(game, 30).runTaskTimer(instance.getPlugin(), 0, 20));
    }

}
