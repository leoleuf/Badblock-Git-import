package fr.badblock.gameapi.players;

import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.disguise.Disguise;
import fr.badblock.gameapi.game.result.Result;
import fr.badblock.gameapi.packets.BadblockOutPacket;
import fr.badblock.gameapi.particles.ParticleEffect;
import fr.badblock.gameapi.players.bossbars.BossBarColor;
import fr.badblock.gameapi.players.bossbars.BossBarStyle;
import fr.badblock.gameapi.players.scoreboard.CustomObjective;
import fr.badblock.gameapi.utils.i18n.TranslatableString;
import fr.badblock.gameapi.utils.selections.CuboidSelection;
import lombok.Getter;

/**
 * Classe ajoutant des m�thodes (BadBlock et utilitaires) � la classe
 * Player.<br>
 * Pour l'obtenir il suffit de caster le Player.
 * 
 * @author LeLanN
 */
public interface BadblockPlayer extends Player, BadblockPlayerData {
	public static final int VERSION_1_8    = 47;
	public static final int VERSION_1_9    = 107;
	public static final int VERSION_1_9_1  = 108;
	public static final int VERSION_1_9_2  = 109;
	public static final int VERSION_1_10   = 210;
	
	/**
	 * Repr�sente les diff�rents modes de jeux possibles pour un joueur Badblock
	 * 
	 * @author LeLanN
	 */
	public static enum BadblockMode {
		/**
		 * Repr�sente un joueur 'normal' (entrain de jouer ou au lobby
		 * d'attente)
		 */
		PLAYER(),
		/**
		 * Repr�sente un joueur attendant en spectateur d'�tre respawn
		 */
		RESPAWNING(),
		/**
		 * Repr�sente un joueur observant la partie sans y interf�rer
		 */
		SPECTATOR();
	}

	/**
	 * Repr�sente les permissions basiques des MiniJeux, pour une gestion plus
	 * simple.
	 * 
	 * @author LeLanN
	 */
	public static enum GamePermission {
		PLAYER(null), 
		VIP("badblock.vip"),
		MODERATOR("badblock.modo"),
		BMODERATOR("badblock.modo+"),
		ADMIN("badblock.admin");

		@Getter
		private final String permission;

		private GamePermission(String permission) {
			this.permission = permission;
		}
	}

	/**
	 * V�rifie si le joueur peut build
	 * 
	 * @return Si le joueur peut build
	 */
	public boolean canBuild();

	/**
	 * V�rifie si le joueur peut build instantan�ment
	 * 
	 * @return Si le joueur peut build instantan�ment
	 */
	public boolean canInstantlyBuild();

	/**
	 * Change la dimension affich�e par le joueur sans le changer r�ellement de
	 * monde
	 * 
	 * @param world
	 *            La nouvelle dimension
	 */
	public void changePlayerDimension(World.Environment world);

	/**
	 * Clear l'inventaire du joueur (dont l'armure)
	 */
	public void clearInventory();

	/**
	 * Enl�ve le title de l'�cran du joueur
	 */
	public void clearTitle();

	public void disguise(Disguise disguise);

	/**
	 * Remplit la barre de faim du joueur
	 */
	public void feed();

	public Object getHandle();
	
	/**
	 * R�cup�re les groupes secondaires du joueur (par exemple emeraude et
	 * modo). Peut �tre vide.
	 * 
	 * @return Les groupes secondaires
	 */
	public Collection<String> getAlternateGroups();

	/**
	 * R�cup�re le mode actuel de jeu du joueur
	 * 
	 * @return Le mode
	 */
	public BadblockMode getBadblockMode();

	/**
	 * R�cup�re le CustomObjective vu par le joueur
	 * 
	 * @return Le CustomObjective (null si non d�finit)
	 */
	public CustomObjective getCustomObjective();

	/**
	 * R�cup�re le groupe principal du joueur (par exemple admin)
	 * 
	 * @return Le groupe principal
	 */
	public String getMainGroup();

	/**
	 * R�cup�re le ping du joueur
	 * 
	 * @return
	 */
	public int getPing();

	/**
	 * R�cup�re la s�l�ction d�finie par le joueur via un baton de blaze. Peut
	 * retourner null si non d�finie.
	 * 
	 * @return La s�l�ction ou null
	 */
	public CuboidSelection getSelection();

	/**
	 * R�cup�re un message traduit dans la langue du joueur
	 * 
	 * @param key
	 *            La key du message
	 * @param args
	 *            Les arguments
	 * @return Le message
	 */
	public String[] getTranslatedMessage(String key, Object... args);

	/**
	 * Si le joueur est en mode bypass (pour casser les blocs ect)
	 * 
	 * @return Un boolean
	 */
	public boolean hasAdminMode();
	
	/**
	 * R�cup�re la version du protocol du joueur
	 * @return La version
	 */
	public int getProtocolVersion();

	/**
	 * V�rifie si le joueur a une des permissions basiques des mini-jeux.
	 * 
	 * @param permission
	 *            La permission
	 * @return Si le joueur a la permission
	 */
	public boolean hasPermission(GamePermission permission);

	/**
	 * Soigne le joueur (vie, faim, feu et effets n�gatifs de potions)
	 */
	public void heal();

	public boolean isDisguised();

	/**
	 * V�rifie si le joueur est invuln�rable (si il ne peut pas prendre de
	 * d�gats).
	 * 
	 * @return Si le joueur est invuln�rable
	 */
	public boolean isInvulnerable();

	/**
	 * Permet de savoir si le joueur a �t� bloqu� avec
	 * {@link #jailPlayerAt(Location)}.
	 * 
	 * @return Si il est bloqu�
	 */
	public boolean isJailed();

	/**
	 * Permet de v�rifier si le joueur est confin� dans une zone
	 * 
	 * @return Si il est bloqu�
	 */
	public boolean isPseudoJailed();

	/**
	 * Permet de faire que le joueur ne puisse pas du tout bouger � la position
	 * donn�e.<br>
	 * * Si null, le joueur pourra de nouveau bouger.
	 * 
	 * @param location
	 *            La position o� il doit rester.
	 */
	public void jailPlayerAt(Location location);

	/**
	 * Lance un projectile custom, qui appelera une m�thode lorsqu'il touchera un block ou une entit�
	 * @param projectile La classe du projectile
	 * @param action L'action
	 * @return Le projectile
	 */
	public <T extends Projectile> T launchProjectile(Class<T> projectile, BiConsumer<Block, Entity> action);
	
	/**
	 * Lance un projectile custom, qui appelera une m�thode lorsqu'il touchera un block ou une entit�
	 * @param projectile La classe du projectile
	 * @param action L'action
	 * @param range Gb�h
	 * @return Le projectile
	 */
	public <T extends Projectile> T launchProjectile(Class<T> projectile, BiConsumer<Block, Entity> action, int range);

	
	/**
	 * Joue l'animation de l'ouverture ou fermeture d'un coffre au joueur
	 * 
	 * @param block
	 *            Le coffre � 'ouvrir/fermer'
	 * @param open
	 *            Si le coffre est ouvert
	 */
	public void playChestAnimation(Block block, boolean open);

	/**
	 * Force le client � voir de la pluie/neige (bien que cela ne soit pas
	 * r�ellement le cas c�t� serveur).
	 * 
	 * @param rain
	 *            Si le joueur voit de la pluie
	 */
	public void playRain(boolean rain);

	/**
	 * Fait jouer un son au joueur avec des param�tres par d�faut
	 * 
	 * @param location
	 *            L'origine du son
	 * @param sound
	 *            Le son � jouer
	 */
	public void playSound(Location location, Sound sound);

	/**
	 * Fait jouer un son au joueur avec des param�tres par d�faut
	 * 
	 * @param sound
	 *            Le son � jouer
	 */
	public void playSound(Sound sound);

	/**
	 * Upload un r�sultat de partie pour le joueur. Le joueur en sera notifi�.
	 * 
	 * @param result
	 *            Le r�sultat
	 */
	public void postResult(Result result);

	/**
	 * Permet de faire que le joueur ne puisse pas bouger en dehors d'une
	 * certaine zone<br>
	 * Si null, le joueur pourra de nouveau aller partout.
	 * 
	 * @param location
	 *            La position o� il doit rester
	 * @param radius
	 *            Le rayon autour duquel il peut se d�placer
	 */
	public void pseudoJail(Location location, double radius);

	/**
	 * Renvoit tous les chunks autour du joueur
	 */
	public void reloadMap();

	/**
	 * Enl�ve les effets n�gatifs (potions)
	 */
	public void removeBadPotionEffects();

	/**
	 * Enl�ve tous les effets (potions)
	 */
	public void removePotionEffects();

	/**
	 * Sauvegarde (envoit � Ladder) les donn�es joueurs
	 */
	public void saveGameData();

	/**
	 * Envoit une action bar au joueur
	 * 
	 * @param message
	 *            L'action bar
	 */
	public void sendActionBar(String message);

	public void addBossBar(String key, String message, float life, BossBarColor color, BossBarStyle style);
	
	public void changeBossBar(String key, String message);
	
	public void changeBossBarStyle(String key, float life, BossBarColor color, BossBarStyle style);
	
	public void removeBossBar(String key);
	
	public void removeBossBars();
	
	/**
	 * Enl�ve le message de la 'Boss Bar' du joueur (si il y en a un)
	 */
	public default void removeBossBar(){
		removeBossBar("defaultbar");
	}
	
	/**
	 * Change le message de la 'Boss Bar' du joueur
	 * 
	 * @param message
	 *            Le message
	 */
	public default void sendBossBar(String message){
		addBossBar("defaultbar", message, 1.0f, BossBarColor.PURPLE, BossBarStyle.SOLID);
	}

	/**
	 * Envoit un packet � un joueur. Pour utiliser les packets voir
	 * {@link fr.badblock.gameapi.GameAPI#createPacket(Class)}.
	 * 
	 * @param packet
	 *            Le packet � envoyer.
	 */
	public void sendPacket(BadblockOutPacket packet);

	/**
	 * Affiche une particule au joueur
	 * 
	 * @param location
	 *            La position de la particule
	 * @param effect
	 *            La particule (r�cup�rable avec
	 *            {@link GameAPI#createParticleEffect(fr.badblock.gameapi.particles.ParticleEffectType)}
	 */
	public void sendParticle(Location location, ParticleEffect effect);

	/**
	 * R�cup�re la valeur d'une perssion de type cl�/valeur
	 * @param key La cl�
	 * @param clazz Le type dmand�
	 * @return Le retour
	 */
	public <T> T getPermissionValue(String key, Class<T> clazz);
	
	/**
	 * Fait changer le joueur de serveur
	 * 
	 * @param server
	 *            Le nouveau serveur
	 */
	public void sendPlayer(String server);

	/**
	 * Envoit le header et le footer de la tablist au joueur. Utiliser le
	 * symbole {@literal \n} pour revenir � la ligne.
	 * 
	 * @param header
	 *            Le header
	 * @param footer
	 *            Le footer
	 */
	public void sendTabHeader(String header, String footer);

	/**
	 * Envoit les timings (du title/action bar) au joueur
	 * 
	 * @param fadeIn
	 *            Le temps que le message met � s'afficher
	 * @param stay
	 *            Le temps o� le message reste
	 * @param fadeOut
	 *            Le temps que le message met � dispara�tre
	 */
	public void sendTimings(long fadeIn, long stay, long fadeOut);

	/**
	 * Envoit un title au joueur
	 * 
	 * @param title
	 *            Le title
	 * @param subtitle
	 *            Le subtitle
	 */
	public void sendTitle(String title, String subtitle);

	/**
	 * Envoit une action bar traduite au joueur. Pour plus d'informations voir
	 * {@link fr.badblock.gameapi.utils.i18n.I18n}
	 * 
	 * @param key
	 *            La key du message
	 * @param args
	 *            Les arguments
	 */
	public void sendTranslatedActionBar(String key, Object... args);

	/**
	 * Envoit une BossBar traduite au joueur. Pour plus d'informations voir
	 * {@link fr.badblock.gameapi.utils.i18n.I18n}
	 * 
	 * @param key
	 *            La key du message
	 * @param args
	 *            Les arguments
	 */
	public void sendTranslatedBossBar(String key, Object... args);

	/**
	 * Envoit un message traduit au joueur. Pour plus d'informations voir
	 * {@link fr.badblock.gameapi.utils.i18n.I18n}
	 * 
	 * @param key
	 *            La key du message
	 * @param args
	 *            Les arguments
	 */
	public void sendTranslatedMessage(String key, Object... args);

	/**
	 * Envoit le header et le footer de la tablist au joueur.
	 * 
	 * @param header
	 *            Le header
	 * @param footer
	 *            Le footer
	 */
	public void sendTranslatedTabHeader(TranslatableString header, TranslatableString footer);

	/**
	 * Envoit un title traduit au joueur (les deux premiers messages de l'array
	 * seront prit). Pour plus d'informations voir
	 * {@link fr.badblock.gameapi.utils.i18n.I18n}
	 * 
	 * @param key
	 *            La key du message
	 * @param args
	 *            Les arguments
	 */
	public void sendTranslatedTitle(String key, Object... args);

	/**
	 * D�finit si le joueur est en mode bypass (pour casser les blocs ect)
	 * 
	 * @param adminMode
	 *            Un boolean
	 */
	public void setAdminMode(boolean adminMode);

	/**
	 * Change le nombre de fl�ches dans le corps du joueurs.
	 * 
	 * @param amount
	 *            Le nombe de fl�ches.
	 */
	public void setArrowsInBody(byte amount);

	/**
	 * Change le mode de jeu du joueur (les protections et items seront g�r�s
	 * automatiquement)
	 * 
	 * @param newMode
	 *            Le mode
	 */
	public void setBadblockMode(BadblockMode newMode);

	/**
	 * D�finit si le joueur peut build
	 * 
	 * @param canBuild
	 *            Si le joueur peut build
	 */
	public void setCanBuild(boolean canBuild);

	/**
	 * Dz�finit si le joueur peut build instantan�ment
	 * 
	 * @param instantlyBuild
	 *            Si le joueur peut build instantan�ment
	 */
	public void setCanInstantlyBuild(boolean instantlyBuild);

	/**
	 * Active ou d�sactive la collision entre le joueur et les entit�s
	 * 
	 * @param collision
	 *            Si les collisions sont activ�s
	 */
	public void setEntityCollision(boolean collision);

	/**
	 * D�finit si le joueur est invuln�rable (si il ne peut pas prendre de
	 * d�gats).<br>
	 * Attention, un changement de gamemode l'annule.
	 * 
	 * @param invulnerable
	 *            Si le joueur est invuln�rable
	 */
	public void setInvulnerable(boolean invulnerable);

	/**
	 * D�finit si le joueur a ou non des informations de d�bug (F3) r�duites
	 * (par exemple plus les coordonn�es).<br>
	 * Bien entendu, cela est fait c�t� client, ce n'est donc pas fiable (le
	 * client peut �tre modd�).
	 * 
	 * @param reducedDebugInfo
	 *            Si le joueur a des informations de d�bug r�duites
	 */
	public void setReducedDebugInfo(boolean reducedDebugInfo);

	/**
	 * Affiche un CustomObjective au joueur
	 * 
	 * @param objective
	 *            L'objective
	 */
	public void showCustomObjective(CustomObjective objective);

	/**
	 * Fait voir l'image de 'd�mo' de MineCraft au joueur. Utile uniquement si
	 * le serveur est dot� d'un resource pack.<br>
	 * Le joueur doit �tre en GameMode 3
	 */
	public void showDemoScreen();

	/**
	 * Affiche un texte 'volant' au joueur.
	 * 
	 * @param text
	 *            Le texte � afficher.
	 * @param location
	 *            La position du texte (si null, juste devant ses yeux)
	 * @param lifeTime
	 *            La dur�e de vie du texte (en ticks)
	 * @param offset
	 *            Le d�placement que peut subir le texte par rapport � la
	 *            position initiale (random). Aucun si 0.
	 */
	public void showFloatingText(String text, Location location, long lifeTime, double offset);

	/**
	 * Affiche un texte 'volant' traduit au joueur.
	 * 
	 * @param location
	 *            La position du texte (si null, juste devant ses yeux)
	 * @param lifeTime
	 *            La dur�e de vie du texte (en ticks)
	 * @param offset
	 *            Le d�placement que peut subir le texte par rapport � la
	 *            position initiale (random). Aucun si 0.
	 * @param key
	 *            La key du message
	 * @param args
	 *            Les arguments
	 */
	public void showTranslatedFloatingText(Location location, long lifeTime, double offset, String key, Object... args);

	public void undisguise();

	/**
	 * Renvoit un chunk au joueur
	 * 
	 * @param chunk
	 *            Le chunk
	 */
	public void updateChunk(Chunk chunk);
	
	/**
	 * Compte les items d'un certains type
	 * @param type Le material
	 * @param data La data
	 * @return Le nombre d'items
	 */
	public int countItems(Material type, byte data);
	
	/**
	 * Enl�ve les items d'un certains type
	 * @param type Le material
	 * @param data La data
	 * @param amount Le nombre d'items � enlever (-1 = tous)
	 * @return Le nombre qui n'a pas pu �tre retir�
	 */
	public int removeItems(Material type, byte data, int amount);

	/**
	 * R�cup�rer le timestamp quand le joueur a utilis� une 
	 * fausse entit�
	 * @return un timestamp en millisecondes
	 */
	public long getLastFakeEntityUsedTime();
	
	/**
	 * Dire � l'objet que l'on a utilis� une fausse entit�
	 * r�cemment afin de le comptabiliser
	 */
	public void useFakeEntity();
	
	public void setVisible(boolean visible);
	
	public void setVisible(boolean visible, Predicate<BadblockPlayer> applicable);
	
	/**
	 * @deprecated
	 */
	public boolean isVisible();

	public Predicate<BadblockPlayer> getVisiblePredicate();
	public Predicate<BadblockPlayer> getInvisiblePredicate();
	
	/**
	 * Si les data sont fetch
	 * @return
	 */
	public boolean isDataFetch();
	
	public int getVipLevel();
	
	public boolean hasVipLevel(int level, boolean showErrorMessage);

	public boolean canOnlyJoinWhileWaiting();
	
	public void setOnlyJoinWhileWaiting(long time);
	
	public void setLeaves(List<Long> leaves);
	public List<Long> getLeaves();
	
	/**
	 * A ajouter : - toutes les statistiques BadBlock (achievements, points,
	 * ...) - Le scoreboard BadBlock - Des m�thodes en plus pour la gestion du
	 * joueur (voir, en gros, BPlayer.class et EpicPlayer.class) - sendPacket,
	 * sendMessage/ActionBar/Title/... - ?
	 */
}
