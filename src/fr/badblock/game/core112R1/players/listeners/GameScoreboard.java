package fr.badblock.game.core112R1.players.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import fr.badblock.api.common.utils.permissions.PermissionsManager;
import fr.badblock.game.core112R1.players.GameBadblockPlayer;
import fr.badblock.game.core112R1.players.ingamedata.VoteInGameData;
import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.events.api.PlayerDataChangedEvent;
import fr.badblock.gameapi.events.api.PlayerJoinTeamEvent;
import fr.badblock.gameapi.events.api.PlayerLoadedEvent;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockTeam;
import fr.badblock.gameapi.players.scoreboard.BadblockScoreboard;
import fr.badblock.gameapi.utils.i18n.I18n;
import fr.badblock.gameapi.utils.i18n.Locale;
import fr.badblock.gameapi.utils.i18n.TranslatableString;
import fr.badblock.gameapi.utils.itemstack.CustomInventory;
import fr.badblock.gameapi.utils.itemstack.ItemAction;
import fr.badblock.gameapi.utils.itemstack.ItemEvent;

public class GameScoreboard extends BadListener implements BadblockScoreboard {
	public static GameScoreboard gsb	   = null;
	public static String map = "";

	public boolean    doTeamsPrefix   = false;
	private boolean    doGroupsPrefix  = false;
	private boolean	   doDamageHolo    = false;

	Map<VoteElement, Integer> votes;

	public GameScoreboard(){
		gsb = this;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		BadblockPlayer p = (BadblockPlayer) e.getPlayer();

		if(doGroupsPrefix){
			p.setVisible(false, player -> true);
			customRanks.entrySet().forEach((entry) -> {
				if (entry.getKey() == null)
				{
					return;
				}
				if (entry.getValue() == null)
				{
					return;
				}

				if (entry.getValue().getKey() == null)
				{
					return;
				}
				sendTeamData(entry.getKey(), entry.getValue().getKey(), p);
			});
		} else if(doTeamsPrefix){
			if(p.getTeam() != null){
				joinTeam(p, null, p.getTeam());
			} else {
				GameAPI.getAPI().getTeams().forEach((knowTeam) -> {
					sendTeam(p, knowTeam, ChatColor.GRAY);
				});
			}
		}
	}

	@EventHandler
	public void onDataReceive(PlayerLoadedEvent e){
		if(!doGroupsPrefix) return;
		GameBadblockPlayer gbp = (GameBadblockPlayer) e.getPlayer();
		if (groups.get(gbp.getMainGroup()) != null && !gbp.getMainGroup().equalsIgnoreCase("gradeperso")) {
			getHandler().getTeam( groups.get(gbp.getMainGroup()) ).addEntry(e.getPlayer().getName());
		}

		new BukkitRunnable() {
			@Override
			public void run() {
				e.getPlayer().setVisible(true);
			}
		}.runTask(GameAPI.getAPI());

		GameBadblockPlayer player = (GameBadblockPlayer) e.getPlayer();

	}

	@EventHandler
	public void onDataReload(PlayerDataChangedEvent e){
		if(!doGroupsPrefix) return;

		Team team = getHandler().getEntryTeam(e.getPlayer().getName());
		GameBadblockPlayer gbp = (GameBadblockPlayer) e.getPlayer();
		if (team != null && !gbp.getMainGroup().equalsIgnoreCase("gradeperso")) {
			if(!team.getName().equals(groups.get(gbp.getMainGroup()))) {
				team.removeEntry(e.getPlayer().getName());

				new BukkitRunnable() {
					@Override
					public void run() {
						getHandler().getTeam( groups.get(gbp.getMainGroup()) ).addEntry(e.getPlayer().getName());
					}
				}.runTaskLater(GameAPI.getAPI(), 5L);
			}
		}

		GameBadblockPlayer player = (GameBadblockPlayer) e.getPlayer();

	}

	@Override
	public Scoreboard getHandler() {
		return null;
	}

	@Override
	public void doTabListHealth(){
	}

	@Override
	public TranslatableString getUsedName(BadblockPlayer player) {
		if(doGroupsPrefix){
			return new TranslatableString("game.customname", player.getName(), player.getTabGroupPrefix());
		} else if(doTeamsPrefix){

			if(player.getTeam() != null){
				return new TranslatableString("game.customname", player.getName(), player.getTeam().getTabPrefix(ChatColor.GRAY));
			}
		}

		return new TranslatableString("game.customname", player.getName(), "");
	}

	@Override
	public void doBelowNameHealth(){
	}

	@Override
	public void doOnDamageHologram() {
		doDamageHolo = true;
	}

	@EventHandler
	public void onPlayerJoinTeam(PlayerJoinTeamEvent e){
		if(doTeamsPrefix){
			new BukkitRunnable(){
				@Override
				public void run(){
					joinTeam(e.getPlayer(), e.getPreviousTeam(), e.getNewTeam());
				}
			}.runTaskLater(GameAPI.getAPI(), 10L);
		}
	}

	@EventHandler(priority=EventPriority.MONITOR,ignoreCancelled=true)
	public void onDamage(EntityDamageEvent e){
		if(!doDamageHolo)
			return;

		if(e.getEntityType() == EntityType.PLAYER && e.getDamage() >= 1.0d){
			BadblockPlayer player = (BadblockPlayer) e.getEntity();

			for(Entity entity : player.getNearbyEntities(16.0d, 16.0d, 16.0d)){
				if(entity.getType() == EntityType.PLAYER){
					BadblockPlayer viewer = (BadblockPlayer) entity;
					viewer.showFloatingText(ChatColor.RED + "-" + (int) e.getDamage(), player.getLocation().add(0, 4, 0), 10, 0.1d);
				}

			}
		}
	}

	public void joinTeam(BadblockPlayer p, BadblockTeam previous, BadblockTeam team){
		if(!getHandler().getTeam(team.getKey()).hasEntry(p.getName()))
			getHandler().getTeam(team.getKey()).addEntry(p.getName());

		if(previous != null){
			getHandler().getTeam(previous.getKey()).removeEntry(p.getName());
		}

		GameAPI.getAPI().getTeams().forEach((knowTeam) -> {
			if(team.equals(knowTeam)){
				sendTeam(p, knowTeam, ChatColor.GREEN);
			} else {
				sendTeam(p, knowTeam, ChatColor.GRAY);
			}
		});

	}

	public static Map<String, String> groups = new HashMap<>();
	public static Map<String, Entry<String, String>> customRanks = new HashMap<>();

	/*
	 * Envoit le nouveau pr�fixe via packet, pour l'avoir custom sans le d�clarer dans un scoreboard personnel (opti)
	 */
	public void sendTeam(BadblockPlayer p, BadblockTeam team, ChatColor color){
		String displayName = team.getTabPrefix(color).getAsLine(p);
		sendTeamData(team.getKey(), displayName, p);
	}

	public void sendTeamData(String teamName, String displayName, BadblockPlayer p){
		if (p.getScoreboard() == null)
		{
			return;
		}

		Team t = p.getScoreboard().getTeam(teamName);
		if (t == null)
		{
			t = p.getScoreboard().registerNewTeam(teamName);
		}
		
		t.setDisplayName(displayName);
		t.setAllowFriendlyFire(!doTeamsPrefix);
		t.setPrefix(displayName);
		t.setSuffix(ChatColor.RESET.toString());
		t.setColor(ChatColor.WHITE);
	}

	private int i = 0;
	public static String customRankId;

	@Override
	public void doGroupsPrefix(){
		if(doTeamsPrefix){
			throw new RuntimeException("Already doing team prefixs");
		}

		doGroupsPrefix = true;
		i = 0;
		PermissionsManager.getManager().getGroups().stream().sorted((a, b) -> {
			return Integer.compare(b.getPower(), a.getPower());
		}).forEach(group -> {
			String id = generateForId(i) + "";

			if (group.getName().equalsIgnoreCase("gradeperso"))
			{
				customRankId = id;
			}

			groups.put(group.getName(), id);
			
			System.out.println("Group : " + group.getName() + " : " + GameAPI.getAPI().getI18n().get(Locale.FRENCH_FRANCE, "permissions.tab." + group.getName())[0]);

			if(getHandler().getTeam(id) == null){
				getHandler().registerNewTeam(id);
			}

			Team teamHandler = getHandler().getTeam(id);

			teamHandler.setAllowFriendlyFire(true);
			i++;
		});
	}

	public char generateForId(int id){
		int A = 'A';

		if(id > 26){
			A   = 'a';
			id -= 26;

			return (char) (A + id);
		} else {
			return (char) (A + id);
		}
	}

	@Override
	public void doTeamsPrefix(){
		if(doGroupsPrefix){
			throw new RuntimeException("Already doing group prefixs");
		}

		doTeamsPrefix = true;

		GameAPI.getAPI().getTeams().forEach((team) -> {

			if(getHandler().getTeam(team.getKey()) == null){
				getHandler().registerNewTeam(team.getKey());
			}

			Team teamHandler = getHandler().getTeam(team.getKey());

			// On ne d�finit pas le pr�fixe car il faut le faire pour chaque joueur (pr�fixe alli� / ennemi)
			teamHandler.setAllowFriendlyFire(false);
		});
	}

	@Override
	public void beginVote(JsonArray maps) {
		List<VoteElement> elements = new ArrayList<VoteElement>();

		for(int i=0;i<maps.size();i++){
			JsonElement element = maps.get(i);

			if(element.isJsonObject()){
				VoteElement vote = GameAPI.getGson().fromJson(element, VoteElement.class);
				elements.add(vote);
			}
		}

		beginVote(elements);
	}

	@Override
	public void beginVote(List<VoteElement> elements)
	{
		votes = Maps.newLinkedHashMap();
	}

	@Override
	public void endVote() {
		map = getWinner().getDisplayName();

		GameAPI.i18n().broadcast("vote.end", getWinner().getDisplayName(), getVotesForWinner());
	}

	@Override
	public void openVoteInventory(BadblockPlayer player) {
		Locale locale = player.getPlayerData().getLocale();

		I18n i18n = GameAPI.i18n();

		int lines = votes.size() / 9 + (votes.size() % 9 == 0 ? 0 : 1);

		if(lines == 0) lines++;
		else if(lines > 6) lines = 6;

		int slot   = 0;

		CustomInventory inventory = GameAPI.getAPI().createCustomInventory(lines, i18n.get(locale, "vote.inventoryDisplayname")[0]);

		for(Entry<VoteElement, Integer> entry : votes.entrySet()){
			if(slot == inventory.size()) break;

			final VoteElement element = entry.getKey();

			inventory.addClickableItem(slot, GameAPI.getAPI().createItemStackFactory()
					.type(Material.PAPER)
					.displayName(entry.getKey().getDisplayName())
					.lore(i18n.get(locale, "maps.infovip"))
					.create(1)
					, new ItemEvent(){
				@Override
				public boolean call(ItemAction action, BadblockPlayer player) {
					VoteInGameData data = player.inGameData(VoteInGameData.class);

					Integer o = player.getPermissionValue("votesCountAs");
					o = o == null || o < 1 ? 1 : o;
					if(data.getElement() != null && votes.containsKey(data.getElement())){
						votes.put(data.getElement(), votes.get(data.getElement()) - o);
					}

					data.setElement(element);

					votes.put(data.getElement(), votes.get(data.getElement()) + o);

					player.closeInventory();
					player.sendTranslatedActionBar("vote.success", data.getElement().getDisplayName());

					return true;
				}
			});

			slot++;
		}

		inventory.openInventory(player);
	}
	
	@Override
	public VoteElement getWinner() {
		if(votes == null) return null;

		Entry<VoteElement, Integer> max = null;

		for(Entry<VoteElement, Integer> value : votes.entrySet()){
			if(max == null || max.getValue() < value.getValue())
				max = value;
		}
		// Random :o
		if (max != null && max.getKey().getInternalName().equals("random")) {
			List<Entry<VoteElement, Integer>> list = new ArrayList<>(votes.entrySet());
			list.remove(max);
		}

		boolean voted = false;
		for (Entry<VoteElement, Integer> value : votes.entrySet())
			if (value.getValue() > 0) voted = true;
		if (!voted) {
			for (Entry<VoteElement, Integer> entry : votes.entrySet()) {
				if (entry.getKey().getInternalName().contains("classique")) {
					max = entry;
				}
			}
		}

		return max == null ? null : max.getKey();
	}

	@Override
	public int getVotesForWinner() {
		if(votes == null) return 0;

		Entry<VoteElement, Integer> max = null;

		for(Entry<VoteElement, Integer> value : votes.entrySet()){
			if(max == null || max.getValue() < value.getValue())
				max = value;
		}

		return max == null ? 0 : max.getValue();
	}

	@Override
	public boolean hasShownGroupPrefix() {
		return this.doGroupsPrefix;
	}
}
