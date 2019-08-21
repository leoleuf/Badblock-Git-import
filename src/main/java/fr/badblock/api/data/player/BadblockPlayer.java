package fr.badblock.api.data.player;

import java.util.function.Predicate;

import org.bukkit.entity.Player;

/**
 * Représentation d'un joueur Bukkit, mais avec plus de fonctionnalitées
 * @author audran
 */
public interface BadblockPlayer extends Player
{
    /**
     * @return L'implémentation NMS du joueur
     */
    public Object getHandle();

    /**
     * Rend de nouveau visible le joueur
     */
    public void setVisible();

    /**
     * Rend le joueur invisible pour certains joueur
     * @param applicable Filtre pour savoir quels joueurs ne verront pas le joueur actuel
     */
    public void setInvisible(Predicate<BadblockPlayer> applicable);

    /**
     * Rend le joueur invisible pour tous les joueurs
     */
    public void setInvisible();
}
