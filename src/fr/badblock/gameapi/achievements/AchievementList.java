package fr.badblock.gameapi.achievements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.data.PlayerAchievementState;
import fr.badblock.gameapi.utils.itemstack.CustomInventory;
import fr.badblock.gameapi.utils.itemstack.ItemAction;

public class AchievementList {
	private static Map<String, Map<String, PlayerAchievement>> achievements = Maps.newConcurrentMap();
	private static Set<PlayerAchievement>					   all			= Sets.newConcurrentHashSet();

	/*
	 * Tuer X personnes
	 */
	public static final PlayerAchievement RUSH_KILL_1 = addAchievement("rush", new PlayerAchievement("rush_kill_1", 10, 5, 10));
	public static final PlayerAchievement RUSH_KILL_2 = addAchievement("rush", new PlayerAchievement("rush_kill_2", 50, 25, 100));
	public static final PlayerAchievement RUSH_KILL_3 = addAchievement("rush", new PlayerAchievement("rush_kill_3", 250, 100, 1000));
	public static final PlayerAchievement RUSH_KILL_4 = addAchievement("rush", new PlayerAchievement("rush_kill_4", 500, 250, 10000));
	
	/*
	 * Casser X lits
	 */
	public static final PlayerAchievement RUSH_BED_1  = addAchievement("rush", new PlayerAchievement("rush_bed_1", 10, 5, 5));
	public static final PlayerAchievement RUSH_BED_2  = addAchievement("rush", new PlayerAchievement("rush_bed_2", 50, 25, 50));
	public static final PlayerAchievement RUSH_BED_3  = addAchievement("rush", new PlayerAchievement("rush_bed_3", 250, 100, 500));
	public static final PlayerAchievement RUSH_BED_4  = addAchievement("rush", new PlayerAchievement("rush_bed_4", 500, 250, 5000));

	/*
	 * Faire exploser X lits 
	 */
	public static final PlayerAchievement RUSH_EBED_1  = addAchievement("rush", new PlayerAchievement("rush_ebed_1", 10, 5, 5));
	public static final PlayerAchievement RUSH_EBED_2  = addAchievement("rush", new PlayerAchievement("rush_ebed_2", 50, 25, 50));
	public static final PlayerAchievement RUSH_EBED_3  = addAchievement("rush", new PlayerAchievement("rush_ebed_3", 250, 100, 500));
	public static final PlayerAchievement RUSH_EBED_4  = addAchievement("rush", new PlayerAchievement("rush_ebed_4", 500, 250, 5000));

	/*
	 * Gagner X parties
	 */
	public static final PlayerAchievement RUSH_WIN_1  = addAchievement("rush", new PlayerAchievement("rush_win_1", 10, 2, 1));
	public static final PlayerAchievement RUSH_WIN_2  = addAchievement("rush", new PlayerAchievement("rush_win_2", 50, 25, 100));
	public static final PlayerAchievement RUSH_WIN_3  = addAchievement("rush", new PlayerAchievement("rush_win_3", 250, 100, 1000));
	public static final PlayerAchievement RUSH_WIN_4  = addAchievement("rush", new PlayerAchievement("rush_win_4", 500, 250, 10000));

	/*
	 * Gagner X parties en moins de 10 minutes
	 */
	public static final PlayerAchievement RUSH_RUSHER_1  = addAchievement("rush", new PlayerAchievement("rush_rusher_1", 10, 5, 5));
	public static final PlayerAchievement RUSH_RUSHER_2  = addAchievement("rush", new PlayerAchievement("rush_rusher_2", 50, 25, 50));
	public static final PlayerAchievement RUSH_RUSHER_3  = addAchievement("rush", new PlayerAchievement("rush_rusher_3", 250, 100, 500));
	public static final PlayerAchievement RUSH_RUSHER_4  = addAchievement("rush", new PlayerAchievement("rush_rusher_4", 500, 250, 5000));
	
	/*
	 * Tuer 10 joueurs dans une m�me partie
	 */
	public static final PlayerAchievement RUSH_KILLER = addAchievement("rush", new PlayerAchievement("rush_killer", 100, 50, 10, true));
	/*
	 * Tuer 20 joueurs dans une m�me partie
	 */
	public static final PlayerAchievement RUSH_UKILLER = addAchievement("rush", new PlayerAchievement("rush_ukiller", 250, 100, 25, true));

	/*
	 * Tuer 15 � l'arc joueurs dans une m�me partie
	 */
	public static final PlayerAchievement RUSH_SHOOTER = addAchievement("rush", new PlayerAchievement("rush_shooter", 100, 50, 15, true));
	
	/*
	 * Ne frapper les adverseraires qu'� l'arc et faire 20 kills
	 */
	public static final PlayerAchievement RUSH_USHOOTER = addAchievement("rush", new PlayerAchievement("rush_ushooter", 250, 150, 25, true));
	
	/**
	 * Casser 3 lits dans une m�me partie
	 */
	public static final PlayerAchievement RUSH_BROKER = addAchievement("rush", new PlayerAchievement("rush_broker", 100, 50, 3, true));

	/**
	 * Exploser 3 lits dans une m�me partie
	 */
	public static final PlayerAchievement RUSH_EXPLODER = addAchievement("rush", new PlayerAchievement("rush_exploder", 150, 75, 3, true));

	/**
	 * Exploser 3 lits dans une m�me partie
	 */
	public static final PlayerAchievement RUSH_ALLKITS = addAchievement("rush", new PlayerAchievement("rush_allkits", 300, 150, 3, true));

	/*
	 * Tuer X personnes
	 */
	public static final PlayerAchievement TOWER_KILL_1 = addAchievement("tower", new PlayerAchievement("tower_kill_1", 10, 5, 10));
	public static final PlayerAchievement TOWER_KILL_2 = addAchievement("tower", new PlayerAchievement("tower_kill_2", 50, 25, 100));
	public static final PlayerAchievement TOWER_KILL_3 = addAchievement("tower", new PlayerAchievement("tower_kill_3", 250, 100, 1000));
	public static final PlayerAchievement TOWER_KILL_4 = addAchievement("tower", new PlayerAchievement("tower_kill_4", 500, 250, 10000));
	
	/*
	 * Marquer X points
	 */
	public static final PlayerAchievement TOWER_MARK_1  = addAchievement("tower", new PlayerAchievement("tower_mark_1", 10, 5, 5));
	public static final PlayerAchievement TOWER_MARK_2  = addAchievement("tower", new PlayerAchievement("tower_mark_2", 50, 25, 500));
	public static final PlayerAchievement TOWER_MARK_3  = addAchievement("tower", new PlayerAchievement("tower_mark_3", 250, 100, 5000));
	public static final PlayerAchievement TOWER_MARK_4  = addAchievement("tower", new PlayerAchievement("tower_mark_4", 500, 250, 20000));

    /*
	 * Marquer 5 points en une partie sur X parties
	 */
	public static final PlayerAchievement TOWER_MARKER_1  = addAchievement("tower", new PlayerAchievement("tower_marker_1", 10, 5, 5));
	public static final PlayerAchievement TOWER_MARKER_2  = addAchievement("tower", new PlayerAchievement("tower_marker_2", 50, 25, 50));
	public static final PlayerAchievement TOWER_MARKER_3  = addAchievement("tower", new PlayerAchievement("tower_marker_3", 250, 100, 500));
	public static final PlayerAchievement TOWER_MARKER_4  = addAchievement("tower", new PlayerAchievement("tower_marker_4", 500, 250, 5000));

    
	/*
	 * Gagner X parties
	 */
	public static final PlayerAchievement TOWER_WIN_1  = addAchievement("tower", new PlayerAchievement("tower_win_1", 10, 2, 1));
	public static final PlayerAchievement TOWER_WIN_2  = addAchievement("tower", new PlayerAchievement("tower_win_2", 50, 25, 100));
	public static final PlayerAchievement TOWER_WIN_3  = addAchievement("tower", new PlayerAchievement("tower_win_3", 250, 100, 1000));
	public static final PlayerAchievement TOWER_WIN_4  = addAchievement("tower", new PlayerAchievement("tower_win_4", 500, 250, 10000));
	
	/*
	 * Tuer 10 joueurs dans une m�me partie
	 */
	public static final PlayerAchievement TOWER_KILLER = addAchievement("tower", new PlayerAchievement("tower_killer", 100, 50, 10, true));
	/*
	 * Tuer 20 joueurs dans une m�me partie
	 */
	public static final PlayerAchievement TOWER_UKILLER = addAchievement("tower", new PlayerAchievement("tower_ukiller", 250, 100, 25, true));

	/*
	 * Tuer 15 � l'arc joueurs dans une m�me partie
	 */
	public static final PlayerAchievement TOWER_SHOOTER = addAchievement("tower", new PlayerAchievement("tower_shooter", 100, 50, 15, true));
	
	/*
	 * Ne frapper les adverseraires qu'� l'arc et faire 20 kills
	 */
	public static final PlayerAchievement TOWER_USHOOTER = addAchievement("tower", new PlayerAchievement("tower_ushooter", 250, 150, 25, true));
	
	/**
	 * Marquer 10 points dans la m�me partie
	 */
	public static final PlayerAchievement TOWER_MARKER = addAchievement("tower", new PlayerAchievement("tower_umarker", 100, 50, 10, true));

	/**
	 * Exploser 3 lits dans une m�me partie
	 */
	public static final PlayerAchievement TOWER_ALLKITS = addAchievement("tower", new PlayerAchievement("tower_allkits", 300, 150, 3, true));

	private static <T extends PlayerAchievement> T addAchievement(String game, T achievement){
		if(!achievements.containsKey(game))
			achievements.put(game, Maps.newLinkedHashMap());
		
		achievements.get(game).put(achievement.getName(), achievement);
		all.add(achievement);
		
		return achievement;
	}
	
	public static Collection<PlayerAchievement> getAllAchievements(){
		return Collections.unmodifiableCollection(all);
	}
	
	public static PlayerAchievement getGameAchievement(String game, String achievement){
		if(achievements.containsKey(game))
			return achievements.get(game).get(achievement);
		
		return null;
	}
	
	public static Collection<PlayerAchievement> getGameAchievements(String game){
		if(achievements.containsKey(game))
			return achievements.get(game).values();
		
		return new ArrayList<>();
	}
	
	public static CustomInventory createInventory(BadblockPlayer player, String game, int canUseSize, int size){
		CustomInventory inv = GameAPI.getAPI().createCustomInventory(size, player.getTranslatedMessage("achievements.inventory." + game)[0]);
		
		int line   = 0;
		int column = 0;
		
		for(PlayerAchievement achievement : getGameAchievements(game)){
			PlayerAchievementState state = player.getPlayerData().getAchievementState(achievement);
			
			boolean has 	 = state.isSucceeds();
			int     progress = (int) state.getProgress();
			
			if(has) progress = achievement.getNeededValue();
			
			Material type = achievement.isTemp() ? (has ? Material.EMERALD : Material.COAL) : (has ? Material.EMERALD_BLOCK : Material.COAL_BLOCK);
			
			inv.addClickableItem(line * 9 + column, GameAPI.getAPI().createItemStackFactory()
																	.type(type)
																	.doWithI18n(player.getPlayerData().getLocale())
																	.displayName(achievement.getDisplayName())
																	.lore(achievement.getDescription(progress))
																	.asExtra(1)
																	.disallow(ItemAction.values())); // aucune action, juste lecture :o
			
			column++;
			
			if(column == 4){
				line++;
				column = 0;
			} else if(column == 9){
				line++;
				column = 5;
			}
			
			if(line == canUseSize){
				
				if(column == 0){
					line = 0;
					column = 5;
				} else break;
				
			}
		}

		return inv;
	}
	
	public static void openInventory(BadblockPlayer player, String game){
		createInventory(player, game, 4, 4).openInventory(player);
	}
}
