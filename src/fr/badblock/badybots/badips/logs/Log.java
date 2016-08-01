package fr.badblock.badybots.badips.logs;

import java.util.Calendar;

/**
 * Complete log system
 * @author xMalware
 */
public class Log {
	
	/**
	 * Sending a log message
	 * @param message > the full message
	 * @param type > the log type message
	 * @return 
	 */
	public static String log(String message, LogType type) {
		Calendar cal = Calendar.getInstance();
		int hours = cal.get(Calendar.HOUR_OF_DAY);
		int minutes = cal.get(Calendar.MINUTE);
		int seconds = cal.get(Calendar.SECOND);
		String finalMessage = ChatColor.WHITE + type.getColor() + "[" + type.name() + "] " + type.getSpace() + "" + ChatColor.RESET + "[" + o(hours) + ":" + o(minutes) + ":" + o(seconds) + "] " + message + ChatColor.WHITE;
		System.out.println(finalMessage);
		return finalMessage;
	}
	
	/**
	 * Getting a good number!
	 * @param value > a integer
	 * @return
	 */
	public static String o(int value) {
		return value > 9 ? Integer.toString(value) : "0" + value;		
	}
	
	/**
	 * Log type with colors
	 * @author xMalware
	 */
	public enum LogType {
		
		// DATA
		DEBUG(ChatColor.PURPLE, "  "),
		INFO (ChatColor.CYAN, "   "),
		SUCCESS(ChatColor.GREEN, ""),
		WARNING(ChatColor.RED, "");
		
		// Private fields
		private String color;
		private String space;
		
		/**
		 * LogType constructor
		 * @param color > the string color (ascii)
		 * @param space > spaces for be perpendicular
		 */
		private LogType(String color, String space) {
			this.color = color;
			this.space = space;
		}
		
		/**
		 * Getting the space
		 * @return
		 */
		public String getSpace() {
			return this.space;
		}
		
		/**
		 * Getting the string color
		 * @return
		 */
		public String getColor() {
			return this.color;
		}
		
	}
	
	
}
