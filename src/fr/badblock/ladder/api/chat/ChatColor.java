package fr.badblock.ladder.api.chat;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Repr�sente toutes les couleurs du chat.
 * @author BungeeCord (modified by LeLanN)
 */
public enum ChatColor {

	/**
	 * Repr�sente le noir.
	 */
	BLACK('0', "black"),
	/**
	 * Repr�sente le bleu fonc�
	 */
	DARK_BLUE('1', "dark_blue"),
	/**
	 * Repr�sente le vert fonc�
	 */
	DARK_GREEN('2', "dark_green"),
	/**
	 * Repr�sente le bleu marine fonc�
	 */
	DARK_AQUA('3', "dark_aqua", "darkaqua"),
	/**
	 * Repr�sente le rouge fonc�
	 */
	DARK_RED('4', "dark_red", "darkred"),
	/**
	 * Repr�sente le violet vonc�
	 */
	DARK_PURPLE('5', "dark_purple"),
	/**
	 * Repr�sente la couleur or
	 */
	GOLD('6', "gold"),
	/**
	 * Repr�sente le gris
	 */
	GRAY('7', "gray"),
	/**
	 * Repr�sente le gris fonc�
	 */
	DARK_GRAY('8', "dark_gray"),
	/**
	 * Repr�sente le bleu
	 */
	BLUE('9', "blue"),
	/**
	 * Repr�sente le vert
	 */
	GREEN('a', "green"),
	/**
	 * Repr�sente le bleu marine
	 */
	AQUA('b', "aqua"),
	/**
	 * Repr�sente le rouge
	 */
	RED('c', "red"),
	/**
	 * Repr�sente le violet clair
	 */
	LIGHT_PURPLE('d', "light_purple"),
	/**
	 * Repr�sente le jaune
	 */
	YELLOW('e', "yellow"),
	/**
	 * Repr�sente le blanc
	 */
	WHITE('f', "white"),
	/**
	 * Change les caract�res en caract�res changeant de mani�re al�atoire
	 */
	MAGIC('k', "obfuscated"),
	/**
	 * Change le texte en gras
	 */
	BOLD('l', "bold"),
	/**
	 * Barre le texte
	 */
	STRIKETHROUGH('m', "strikethrough"),
	/**
	 * Souligne le texte
	 */
	UNDERLINE('n', "underline"),
	/**
	 * Met le texte en italique
	 */
	ITALIC('o', "italic"),
	/**
	 * Met le texte par d�faut
	 */
	RESET('r', "reset");
	
	public static final char COLOR_CHAR = '\u00A7';
	public static final String ALL_CODES = "0123456789AaBbCcDdEeFfKkLlMmNnOoRr";

	public static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + String.valueOf(COLOR_CHAR) + "[0-9A-FK-OR]");

	private static final Map<Character, ChatColor> BY_CHAR = new HashMap<Character, ChatColor>();

	private final char code;
	private final String toString;
	private final String name;
	private final String alternateCode;
	
	static {
		for (ChatColor colour : values()) {
			BY_CHAR.put(colour.code, colour);
		}
	}

	private ChatColor(char code, String name){
		this(code, name, name);
	}
	
	private ChatColor(char code, String name, String alternateCode){
		this.code = code;
		this.name = name;
		this.toString = new String(new char[] { COLOR_CHAR, code });
		this.alternateCode = "%" + alternateCode.replace("dark_", "d").replace("light_", "l") + "%";
	}
	
	public String getName(){
		return name;
	}
	
	public String getAlternateCode(){
		return alternateCode;
	}
	
	@Override
	public String toString() {
		return toString;
	}

	/**
	 * Enl�ve toute la couleur du message
	 *
	 * @param input La cha�ne de base
	 * @return La nouvelle cha�ne, sans couleur
	 */
	public static String stripColor(String input){
		if(input == null) {
			return null;
		}
		input = translateAlternateColorCodes('&', input);
		return STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
	}

	/**
	 * Appelle translateAlternateColorCodes avec comme code &. Juste un moyen plus court.
	 * @param input La cha�ne de base
	 * @return La cha�ne de retour
	 */
	public static String replaceColor(String input){
		return translateAlternateColorCodes('&', input);
	}
	
	public static String translateAlternateColorCodes(char altColorChar, String textToTranslate) {
		char[] b = textToTranslate.toCharArray();
		for(int i = 0; i < b.length - 1; i++) {
			if(b[i] == altColorChar && ALL_CODES.indexOf(b[i + 1]) > -1) {
				b[i] = ChatColor.COLOR_CHAR;
				b[i + 1] = Character.toLowerCase(b[i + 1]);
			}
		}
		for(ChatColor chatColor : values()){
			textToTranslate = textToTranslate.replace(chatColor.getAlternateCode(), "�" + chatColor.code);
		}
		return new String(b);
	}

	/**
	 * R�cup�re la couleur par le code qui le repr�sente (par exemple, a : ChatColor.GREEN)
	 *
	 * @param code Le code de la couleur
	 * @return la couleur (null si le code ne correspond pas � une couleur)
	 */
	public static ChatColor getByChar(char code) {
		return BY_CHAR.get(Character.toLowerCase(code));
	}
}