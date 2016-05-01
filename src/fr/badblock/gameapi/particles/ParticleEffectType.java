package fr.badblock.gameapi.particles;

import fr.badblock.gameapi.particles.ParticleData.BlockData;
import fr.badblock.gameapi.particles.ParticleData.ItemData;
import lombok.Getter;

/**
 * Repr�sente les diff�rents type de particules possible
 * @author LeLanN
 */
@Getter
public enum ParticleEffectType {
	/**
	 * Une particule affich�e lors de l'explosion d'une TNT ou d'un creeper
	 * <ul>
	 * <li>Cela ressemble � un "nuage blanc"
	 * <li>La valeur de la speed value va influer sur la vitesse � laquelle la particule va s'envoler
	 * </ul>
	 */
	EXPLOSION_NORMAL("explode", 0),
	/**
	 * Une particule affich�e lors de l'explosion d'une fireball (ghast) ou d'un cr�ne de wither:
	 * <ul>
	 * <li>Ressemble � une balle grise qui dispara�t
	 * <li>La valeur de la speed value influe l�g�rement sur la taille de la particule
	 * </ul>
	 */
	EXPLOSION_LARGE("largeexplode", 1),
	/**
	 * Une particule affich�e lors de l'explosion d'une TNT ou d'un creeper
	 * <ul>
	 * <li>Ressemble � une multitude de balles grises qui disparaissent
	 * <li>La speed value n'a aucune influence
	 * </ul>
	 */
	EXPLOSION_HUGE("hugeexplosion", 2),
	/**
	 * Une particule affich�e lors de l'utilisation d'un firework
	 * <ul>
	 * <li>Ressemble � une �toile blanche
	 * <li>La speed value va influer sur la vitesse � laquelle la particule va s'envoler
	 * </ul>
	 */
	FIREWORKS_SPARK("fireworksSpark", 3),
	/**
	 * Une particule affich�e lorsque une entit� nage dans l'eau ou lorsque une fl�che y entre
	 * <ul>
	 * <li>Ressemble � une bulle
	 * <li>La speed value va influer sur la vitesse � laquelle la particule va s'envoler
	 * </ul>
	 */
	WATER_BUBBLE("bubble", 4, null, true),
	/**
	 * Une particule affich�e lorsque une entit� nage ou qu'un loup se secoue
	 * <ul>
	 * <li>Ressemble � une goutte bleue
	 * <li>La speed value n'a aucune influence
	 * </ul>
	 */
	WATER_SPLASH("splash", 5),
	/**
	 * Une particule affich�e lorsque l'on p�che
	 * <ul>
	 * <li>Ressemble � une goutelette bleu
	 * <li>La speed value va influer sur la vitesse � laquelle la particule va s'envoler
	 * </ul>
	 */
	WATER_WAKE("wake", 6),
	/**
	 * Une particule affich�e par l'eau
	 * <ul>
	 * <li>Ressemble � un petit carr� bleu
	 * <li>La speed value n'a aucune influence
	 * </ul>
	 */
	SUSPENDED("suspended", 7, null, true),
	/**
	 * Une particule affich�e lorsque l'on est pr�t de la bedrock
	 * <ul>
	 * <li>Ressemble � un petit carr� gris
	 * <li>La speed value n'a aucune influence
	 * </ul>
	 */
	SUSPENDED_DEPTH("depthSuspend", 8),
	/**
	 * Une particule affich�e lors d'un coup critique ou lorsque l'on re�oit une fl�che
	 * <ul>
	 * <li>Ressemble � une croix marron
	 * <li>La speed value va influer sur la vitesse � laquelle la particule va s'envoler
	 * </ul>
	 */
	CRIT("crit", 9),
	/**
	 * Une particule affich�e lors d'un coup avec une arme enchant�e
	 * <ul>
	 * <li>Ressemble � une �toile cyan
	 * <li>La speed value va influer sur la vitesse � laquelle la particule va s'envoler
	 * </ul>
	 */
	CRIT_MAGIC("magicCrit", 10),
	/**
	 * Une particule affich�e par les tnt allum�es, les torches, les droppers, les dispensers, les portails de l'end et les brewing stands
	 * et les spawners
	 * <ul>
	 * <li>Ressemble � un petit nuage gris
	 * <li>La speed value va influer sur la vitesse � laquelle la particule va s'envoler
	 * </ul>
	 */
	SMOKE_NORMAL("smoke", 11),
	/**
	 * Une particule affich�e par le feu, les fours sur minecart et les blazes
	 * <ul>
	 * <li>Ressemble � un gros nuage gris
	 * <li>La speed value va influer sur la vitesse � laquelle la particule va s'envoler
	 * </ul>
	 */
	SMOKE_LARGE("largesmoke", 12),
	/**
	 * Une particule affich�e lorsque une potion jetable / xp bottle att�rit
	 * <ul>
	 * <li>Ressemble � un petit tourbillon blanc
	 * <li>Si � 0, la particule n'ira que vers le haut
	 * </ul>
	 */
	SPELL("spell", 13),
	/**
	 * Une particule affich�e lorsque une potion jetable att�rit
	 * <ul>
	 * <li>Ressemble � une croix blanche
	 * <li>Si � 0, la particule n'ira que vers le haut
	 * </ul>
	 */
	SPELL_INSTANT("instantSpell", 14),
	/**
	 * Une particule affich�e lorsque qu'un effet de potion est actif
	 * <ul>
	 * <li>Ressemble � un petit tourbillon color�
	 * <li>La speed value influe sur la luminosit� de la particule (0 = noir, 1 = brillant)
	 * </ul>
	 */
	SPELL_MOB("mobSpell", 15),
	/**
	 * Une particule affich�e lorsque qu'un effet de potion est actif via un beacon
	 * <ul>
	 * <li>Ressemble � un petit toubillon transparent color�
	 * <li>La speed value influe sur la luminosit� de la particule (0 = noir, 1 = brillant)
	 * </ul>
	 */
	SPELL_MOB_AMBIENT("mobSpellAmbient", 16),
	/**
	 * Une particule affich�e par les sorci�res
	 * <ul>
	 * <li>Ressemble � une croix violette
	 * <li>Si � 0, la particule n'ira que vers le haut
	 * </ul>
	 */
	SPELL_WITCH("witchMagic", 17),
	/**
	 * A particle effect which is displayed by blocks beneath a water source:
	 * <ul>
	 * <li>It looks like a blue drip
	 * <li>The speed value has no influence on this particle effect
	 * </ul>
	 */
	DRIP_WATER("dripWater", 18),
	/**
	 * A particle effect which is displayed by blocks beneath a lava source:
	 * <ul>
	 * <li>It looks like an orange drip
	 * <li>The speed value has no influence on this particle effect
	 * </ul>
	 */
	DRIP_LAVA("dripLava", 19),
	/**
	 * A particle effect which is displayed when attacking a villager in a village:
	 * <ul>
	 * <li>It looks like a cracked gray heart
	 * <li>The speed value has no influence on this particle effect
	 * </ul>
	 */
	VILLAGER_ANGRY("angryVillager", 20),
	/**
	 * A particle effect which is displayed when using bone meal and trading with a villager in a village:
	 * <ul>
	 * <li>It looks like a green star
	 * <li>The speed value has no influence on this particle effect
	 * </ul>
	 */
	VILLAGER_HAPPY("happyVillager", 21),
	/**
	 * A particle effect which is displayed by mycelium:
	 * <ul>
	 * <li>It looks like a tiny gray square
	 * <li>The speed value has no influence on this particle effect
	 * </ul>
	 */
	TOWN_AURA("townaura", 22),
	/**
	 * A particle effect which is displayed by note blocks:
	 * <ul>
	 * <li>It looks like a colored note
	 * <li>The speed value causes the particle to be colored green when set to 0
	 * </ul>
	 */
	NOTE("note", 23),
	/**
	 * A particle effect which is displayed by nether portals, endermen, ender pearls, eyes of ender, ender chests and
	 * dragon eggs:
	 * <ul>
	 * <li>It looks like a purple cloud
	 * <li>The speed value influences the spread of this particle effect
	 * </ul>
	 */
	PORTAL("portal", 24),
	/**
	 * A particle effect which is displayed by enchantment tables which are nearby bookshelves:
	 * <ul>
	 * <li>It looks like a cryptic white letter
	 * <li>The speed value influences the spread of this particle effect
	 * </ul>
	 */
	ENCHANTMENT_TABLE("enchantmenttable", 25),
	/**
	 * A particle effect which is displayed by torches, active furnaces, magma cubes and monster spawners:
	 * <ul>
	 * <li>It looks like a tiny flame
	 * <li>The speed value influences the velocity at which the particle flies off
	 * </ul>
	 */
	FLAME("flame", 26),
	/**
	 * A particle effect which is displayed by lava:
	 * <ul>
	 * <li>It looks like a spark
	 * <li>The speed value has no influence on this particle effect
	 * </ul>
	 */
	LAVA("lava", 27),
	/**
	 * A particle effect which is currently unused:
	 * <ul>
	 * <li>It looks like a transparent gray square
	 * <li>The speed value has no influence on this particle effect
	 * </ul>
	 */
	FOOTSTEP("footstep", 28),
	/**
	 * A particle effect which is displayed when a mob dies:
	 * <ul>
	 * <li>It looks like a large white cloud
	 * <li>The speed value influences the velocity at which the particle flies off
	 * </ul>
	 */
	CLOUD("cloud", 29),
	/**
	 * A particle effect which is displayed by redstone ore, powered redstone, redstone torches and redstone repeaters:
	 * <ul>
	 * <li>It looks like a tiny colored cloud
	 * <li>The speed value causes the particle to be colored red when set to 0
	 * </ul>
	 */
	REDSTONE("reddust", 30),
	/**
	 * A particle effect which is displayed when snowballs hit a block:
	 * <ul>
	 * <li>It looks like a little piece with the snowball texture
	 * <li>The speed value has no influence on this particle effect
	 * </ul>
	 */
	SNOWBALL("snowballpoof", 31),
	/**
	 * A particle effect which is currently unused:
	 * <ul>
	 * <li>It looks like a tiny white cloud
	 * <li>The speed value influences the velocity at which the particle flies off
	 * </ul>
	 */
	SNOW_SHOVEL("snowshovel", 32),
	/**
	 * A particle effect which is displayed by slimes:
	 * <ul>
	 * <li>It looks like a tiny part of the slimeball icon
	 * <li>The speed value has no influence on this particle effect
	 * </ul>
	 */
	SLIME("slime", 33),
	/**
	 * A particle effect which is displayed when breeding and taming animals:
	 * <ul>
	 * <li>It looks like a red heart
	 * <li>The speed value has no influence on this particle effect
	 * </ul>
	 */
	HEART("heart", 34),
	/**
	 * A particle effect which is displayed by barriers:
	 * <ul>
	 * <li>It looks like a red box with a slash through it
	 * <li>The speed value has no influence on this particle effect
	 * </ul>
	 */
	BARRIER("barrier", 35),
	/**
	 * A particle effect which is displayed when breaking a tool or eggs hit a block:
	 * <ul>
	 * <li>It looks like a little piece with an item texture
	 * </ul>
	 */
	ITEM_CRACK("iconcrack", 36, ItemData.class, false),
	/**
	 * A particle effect which is displayed when breaking blocks or sprinting:
	 * <ul>
	 * <li>It looks like a little piece with a block texture
	 * <li>The speed value has no influence on this particle effect
	 * </ul>
	 */
	BLOCK_CRACK("blockcrack", 37, BlockData.class, false),
	/**
	 * A particle effect which is displayed when falling:
	 * <ul>
	 * <li>It looks like a little piece with a block texture
	 * </ul>
	 */
	BLOCK_DUST("blockdust", 38, BlockData.class, false),
	/**
	 * A particle effect which is displayed when rain hits the ground:
	 * <ul>
	 * <li>It looks like a blue droplet
	 * <li>The speed value has no influence on this particle effect
	 * </ul>
	 */
	WATER_DROP("droplet", 39),
	/**
	 * A particle effect which is currently unused:
	 * <ul>
	 * <li>It has no visual effect
	 * </ul>
	 */
	ITEM_TAKE("take", 40),
	/**
	 * A particle effect which is displayed by elder guardians:
	 * <ul>
	 * <li>It looks like the shape of the elder guardian
	 * <li>The speed value has no influence on this particle effect
	 * </ul>
	 */
	MOB_APPEARANCE("mobappearance", 41);
	
	private String 						  name;
	private int	   						  id;
	private Class<? extends ParticleData> data;
	private boolean						  needWater;
	
	ParticleEffectType(String name, int id, Class<? extends ParticleData> data, boolean needWater){
		this.name      = name;
		this.id	  	   = id;
		this.data 	   = data;
		this.needWater = needWater;
	}
	
	ParticleEffectType(String name, int id){
		this(name, id, null, false);
	}
}