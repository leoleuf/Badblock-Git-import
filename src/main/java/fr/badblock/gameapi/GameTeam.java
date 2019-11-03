package fr.badblock.gameapi;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameTeam {

    // Team name
    private String name;
    // Team color
    private ChatColor color;
    // Array with all team player
    private List<UUID> players = new ArrayList<>();

    public GameTeam(String name, ChatColor color)
    {
        this.name = name;
        this.color = color;
    }

    public String getName()
    {
        return name;
    }

    public ChatColor getColor()
    {
        return color;
    }

    public List<UUID> getPlayers()
    {
        return players;
    }

}
