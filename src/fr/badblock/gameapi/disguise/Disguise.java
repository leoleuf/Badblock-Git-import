package fr.badblock.gameapi.disguise;

import org.bukkit.entity.EntityType;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.fakeentities.FakeEntity;
import fr.badblock.gameapi.packets.watchers.WatcherEntity;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.i18n.TranslatableString;
import lombok.Getter;

/**
 * Repr�sente un d�guisement
 * 
 * @author LeLanN
 */
@Getter
public class Disguise {
	protected EntityType entityType;
	protected TranslatableString customName;
	protected boolean doWithScoreboard;
	protected boolean canSeeHimself;

	/**
	 * Cr�� un nouveau d�guisement
	 * 
	 * @param entityType
	 *            Le type d'entit�
	 * @param customName
	 *            Le nom de l'entit� (null = aucun)
	 * @param doWithScoreboard
	 *            Si le nom de l'entit� doit �tre fait avec le scoreboard
	 * @param canSeeHimself
	 *            Si le joueur peut voir le d�guisement
	 */
	public Disguise(EntityType entityType, TranslatableString customName, boolean doWithScoreboard,
			boolean canSeeHimself) {
		this.entityType = entityType;
		this.customName = customName;
		this.doWithScoreboard = doWithScoreboard;
		this.canSeeHimself = canSeeHimself;
	}

	/**
	 * Cr�� l'entit� qui servivra de d�guisement
	 * 
	 * @param location
	 *            La position de spawn
	 * @return L'entit�
	 */
	public final FakeEntity<?> createEntity(BadblockPlayer player) {
		FakeEntity<?> entity = createFakeEntity(player);

		if (customName != null) {
			entity.getWatchers().setCustomNameVisible(true);
			entity.getWatchers().setCustomName(customName);
		} else if (doWithScoreboard) {
			TranslatableString customName = GameAPI.getAPI().getBadblockScoreboard().getUsedName(player);

			entity.getWatchers().setCustomNameVisible(customName != null);
			entity.getWatchers().setCustomName(customName);
		}

		return entity;
	}

	/**
	 * Cr�� l'entit�, en interne. A override pour faire des entit�s customs
	 * 
	 * @param player
	 *            Le joueur
	 * @return L'entit�
	 */
	public FakeEntity<?> createFakeEntity(BadblockPlayer player) {
		return GameAPI.getAPI().spawnFakeLivingEntity(player.getLocation(), entityType, WatcherEntity.class);
	}
}
