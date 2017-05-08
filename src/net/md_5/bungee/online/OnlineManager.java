package net.md_5.bungee.online;

import java.sql.ResultSet;
import java.util.Timer;
import java.util.TimerTask;

import net.md_5.bungee.api.Callback;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.database.BadblockDatabase;
import net.md_5.bungee.database.Request;
import net.md_5.bungee.database.Request.RequestType;

public class OnlineManager {
	
	private static boolean online = true;
	
	static {
		new Timer().schedule(new TimerTask() {
			
			@Override
			public void run() {
				online = Utils.isSessionServerOnline();
			}
			
		}, 1, 5000);
	}
	
	public static boolean isOnline() {
		return online;
	}
	
	public static void joinBypass(InitialHandler initialHandler, Callback<Boolean> callback) {
		if (isOnline()) {
			callback.done(false, null);
			return;
		}
		String name = BadblockDatabase.getInstance().mysql_real_escape_string(initialHandler.getName());
		BadblockDatabase.getInstance().addRequest(new Request("SELECT * FROM ipOnlines WHERE playerName = '" + name + "'", RequestType.GETTER) {
			@Override
			public void done(ResultSet resultSet) {
				try {
					if (resultSet.next()) {
						String ipAddress = resultSet.getString("ipAddress");
						if (ipAddress.equalsIgnoreCase(initialHandler.getAddress().getHostString())) {
							callback.done(true, null);
						}else{
							callback.done(false, null);
						}
					}else callback.done(false, null);
				}catch(Exception error) {
					error.printStackTrace();
					callback.done(false, null);
				}
			}
		});
	}

}
