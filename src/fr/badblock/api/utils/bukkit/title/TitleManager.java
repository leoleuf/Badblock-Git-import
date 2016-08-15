package fr.badblock.api.utils.bukkit.title;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.badblock.api.utils.bukkit.Reflection;

public class TitleManager {
	private static boolean	initialized;

	private static Class<?>	nmsChatSerializer;
	private static Class<?>	nmsPacketTitle;
	private static Class<?>	nmsTitleAction;
	private static Class<?>	nmsPlayerConnection;
	private static Class<?>	nmsEntityPlayer;
	private static Class<?>	nmsChatBaseComponent;
	private static Class<?>	ioNettyChannel;

	private static Method	nmsSendPacket;
	private static Method	nmsChatSerializerA;
	private static Method	nmsNetworkGetVersion;

	private static Field	nmsFieldPlayerConnection;
	private static Field	nmsFieldNetworkManager;
	private static Field	nmsFieldNetworkManagerI;
	private static Field	nmsFieldNetworkManagerM;

	private static double	serverVersion	= Bukkit.getBukkitVersion().contains("v1_8") ? 1.8 : 1.7;
	private static int		VERSION			= 47;

	public static void sendTitle(Player p, String title){
		sendTitle(p, title, false);
	}
	public static void sendTitle(Player p, String title, boolean only18) {
		if (p == null || title == null) throw new NullPointerException();
		if (!(getVersion(p) >= VERSION)) {
			if(!only18)
				p.sendMessage(title);
			return;
		}
		if (!title.startsWith("{") || !title.endsWith("}")) {
			title = MessageSerializer.convertToJSON(title);
		}

		try {
			final Object handle = Reflection.getHandle(p);
			final Object connection = nmsFieldPlayerConnection.get(handle);
			final Object serialized = nmsChatSerializerA.invoke(null, title);
			Object packet = null;
			if (serverVersion == 1.7) {
				//packet = PacketTitle.class.getConstructor(PacketTitle.Action.class, nmsChatBaseComponent).newInstance(PacketTitle.Action.TITLE, serialized);
			} else {
				packet = nmsPacketTitle.getConstructor(nmsTitleAction, nmsChatBaseComponent).newInstance(nmsTitleAction.getEnumConstants()[0], serialized);
			}
			nmsSendPacket.invoke(connection, packet);
		} catch (final Exception e) {
			System.out.println("[TitleManager] Error while sending title to Player " + p.getName() + ": " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static void sendTitle(Player p, int fadeIn, int stay, int fadeOut, String title, boolean only18) {
		sendTimings(p, fadeIn, stay, fadeOut);
		sendTitle(p, title, only18);
	}
	public static void sendTitle(Player p, int fadeIn, int stay, int fadeOut, String title) {
		sendTimings(p, fadeIn, stay, fadeOut);
		sendTitle(p, title);
	}

	public static void sendSubTitle(Player p, String subtitle){
		sendSubTitle(p, subtitle, false);
	}
	public static void sendSubTitle(Player p, String subtitle, boolean only18) {
		if (p == null || subtitle == null) throw new NullPointerException();
		if (!(getVersion(p) >= VERSION)) {
			if(!only18)
				p.sendMessage(subtitle);
			return;
		}

		if (!subtitle.startsWith("{") || !subtitle.endsWith("}")) {
			subtitle = MessageSerializer.convertToJSON(subtitle);
		}

		try {
			final Object handle = Reflection.getHandle(p);
			final Object connection = nmsFieldPlayerConnection.get(handle);
			final Object serialized = nmsChatSerializerA.invoke(null, subtitle);
			Object packet = null;
			if (serverVersion == 1.7) {
				//packet = PacketTitle.class.getConstructor(PacketTitle.Action.class, nmsChatBaseComponent).newInstance(PacketTitle.Action.SUBTITLE, serialized);
			} else {
				packet = nmsPacketTitle.getConstructor(nmsTitleAction, nmsChatBaseComponent).newInstance(nmsTitleAction.getEnumConstants()[1], serialized);
			}
			nmsSendPacket.invoke(connection, packet);
		} catch (final Exception e) {
			System.out.println("[TitleManager] Error while sending subtitle to Player " + p.getName() + ": " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static void sendSubTitle(Player p, int fadeIn, int stay, int fadeOut, String subtitle, boolean only18) {
		sendTimings(p, fadeIn, stay, fadeOut);
		sendSubTitle(p, subtitle, only18);
	}
	public static void sendSubTitle(Player p, int fadeIn, int stay, int fadeOut, String subtitle) {
		sendTimings(p, fadeIn, stay, fadeOut);
		sendSubTitle(p, subtitle);
	}

	/**
	 * Set the Title Timings
	 *
	 * @param p
	 *            Player to Update the Timings
	 * @param fadeIn
	 *            Time it should take to fade In
	 * @param stay
	 *            Time the Title should stay on screen
	 * @param fadeOut
	 *            Time it should take to fade Out
	 */
	public static void sendTimings(Player p, int fadeIn, int stay, int fadeOut) {
		if (p == null) throw new NullPointerException();
		if (!(getVersion(p) >= VERSION)) return;
		try {
			final Object handle = Reflection.getHandle(p);
			final Object connection = nmsFieldPlayerConnection.get(handle);
			Object packet = null;
			if (serverVersion == 1.7) {
				//packet = PacketTitle.class.getConstructor(PacketTitle.Action.class, int.class, int.class, int.class).newInstance(PacketTitle.Action.TIMES, fadeIn, stay, fadeOut);
			} else {
				packet = nmsPacketTitle.getConstructor(int.class, int.class, int.class).newInstance(fadeIn, stay, fadeOut);
			}
			nmsSendPacket.invoke(connection, packet);
		} catch (final Exception e) {
			System.out.println("[TitleManager] Error while sending timings to Player " + p.getName() + ": " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Reset the Players Timing, Title, SubTitle
	 *
	 * @param p
	 *            Player to Reset
	 */
	public static void reset(Player p) {
		if (p == null) throw new NullPointerException();
		if (!(getVersion(p) >= VERSION)) return;
		try {
			final Object handle = Reflection.getHandle(p);
			final Object connection = nmsFieldPlayerConnection.get(handle);
			Object packet = null;
			if (serverVersion == 1.7) {
				//packet = PacketTitle.class.getConstructor(PacketTitle.Action.class).newInstance(PacketTitle.Action.RESET);
			} else {
				packet = nmsPacketTitle.getConstructor(nmsTitleAction, nmsChatBaseComponent).newInstance(nmsTitleAction.getEnumConstants()[4], null);
			}
			nmsSendPacket.invoke(connection, packet);
		} catch (final Exception e) {
			System.out.println("[TitleManager] Error while sending reset to Player " + p.getName() + ": " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Clear the Players Title
	 *
	 * @param p
	 *            Player to be cleared
	 */
	public static void clear(Player p) {
		if (p == null) throw new NullPointerException();
		if (!(getVersion(p) >= VERSION)) return;
		try {
			final Object handle = Reflection.getHandle(p);
			final Object connection = nmsFieldPlayerConnection.get(handle);
			Object packet = null;
			if (serverVersion == 1.7) {
				//packet = PacketTitle.class.getConstructor(PacketTitle.Action.class).newInstance(PacketTitle.Action.CLEAR);
			} else {
				packet = nmsPacketTitle.getConstructor(nmsTitleAction, nmsChatBaseComponent).newInstance(nmsTitleAction.getEnumConstants()[3], null);
			}
			nmsSendPacket.invoke(connection, packet);
		} catch (final Exception e) {
			System.out.println("[TitleManager] Error while sending clear to Player " + p.getName() + ": " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static int getVersion(Player p) {
		try {
			final Object handle = Reflection.getHandle(p);
			final Object connection = nmsFieldPlayerConnection.get(handle);
			final Object network = nmsFieldNetworkManager.get(connection);
			final Object channel;
			if (serverVersion == 1.7) {
				channel = nmsFieldNetworkManagerM.get(network);
			} else {
				channel = nmsFieldNetworkManagerI.get(network);
			}
			final Object version = serverVersion == 1.7 ? nmsNetworkGetVersion.invoke(network, channel) : 47;
			return (int) version;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	static {
		if (!initialized) {
			String ver = Reflection.getVersion();
			if (ver.contains("1_7")) {
				serverVersion = 1.7;
			}
			if (ver.contains("1_8")) {
				serverVersion = 1.8;
			}
			if (ver.contains("1_8_R2")) {
				serverVersion = 1.83;
			}
			
			if( ver.contains("1_8_R3")){
				serverVersion = 1.87;
			}
			try {
				nmsChatBaseComponent = Reflection.getNMSClass("IChatBaseComponent");
				if (serverVersion >= 1.83) {
					for (Class<?> c : nmsChatBaseComponent.getDeclaredClasses()) {
						if (c.getSimpleName().equals("ChatSerializer")) {
							nmsChatSerializer = c;
							break;
						}
					}
				} else {
					nmsChatSerializer = Reflection.getNMSClass("ChatSerializer");
				}
				if (Reflection.getVersion().contains("1_8")) {
					nmsPacketTitle = Reflection.getNMSClass("PacketPlayOutTitle");
					if (serverVersion >= 1.83) {
						for (Class<?> c : nmsPacketTitle.getDeclaredClasses()) {
							if (c.getSimpleName().equals("EnumTitleAction")) {
								nmsTitleAction = c;
								break;
							}
						}
					} else {
						nmsTitleAction = Reflection.getNMSClass("EnumTitleAction");
					}
				}
				nmsPlayerConnection = Reflection.getNMSClass("PlayerConnection");
				nmsEntityPlayer = Reflection.getNMSClass("EntityPlayer");
				ioNettyChannel = serverVersion == 1.7 ? Class.forName("net.minecraft.util.io.netty.channel.Channel") : Class.forName("io.netty.channel.Channel");

				nmsFieldPlayerConnection = Reflection.getField(nmsEntityPlayer, "playerConnection");
				nmsFieldNetworkManager = Reflection.getField(nmsPlayerConnection, "networkManager");
				nmsFieldNetworkManagerI = Reflection.getField(nmsFieldNetworkManager.getType(), "i");
				nmsFieldNetworkManagerM = Reflection.getField(nmsFieldNetworkManager.getType(), "m");

				nmsSendPacket = Reflection.getMethod(nmsPlayerConnection, "sendPacket");
				nmsChatSerializerA = Reflection.getMethod(nmsChatSerializer, "a", String.class);
				nmsNetworkGetVersion = Reflection.getMethod(nmsFieldNetworkManager.getType(), "getVersion", ioNettyChannel);

				initialized = true;

			} catch (Exception e) {
				System.out.println("[TitleManager] Error while loading: " + e.getMessage());
				e.printStackTrace();
				Bukkit.getPluginManager().disablePlugin(Bukkit.getPluginManager().getPlugin("TitleManager"));
			}
		}
	}

}
