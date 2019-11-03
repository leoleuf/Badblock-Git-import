package fr.badblock.gameapi.tasks;

import fr.badblock.gameapi.Game;
import fr.badblock.gameapi.GameState;
import org.bukkit.scheduler.BukkitRunnable;

public class StartingTask extends BukkitRunnable {

    private Game game;
    private int i;

    public StartingTask(Game game, int i)
    {
        this.game = game;
        this.i = i;
    }

    @Override
    public void run()
    {
        if (game.isState(GameState.STARTING)) {
            game.setLevel(i);
            if (i == 15 || i == 10 || (i <= 5 && i >= 1)) {
                game.sendMessage("§eLa partie commence dans §c" + i + " §e" + (i > 1 ? "secondes" : "seconde") + " !");
            }
            if (i == 0) {
                game.sendMessage("§eDébut de la partie, bon jeu !");
                cancel();
            }
            i--;
        }
    }

}
