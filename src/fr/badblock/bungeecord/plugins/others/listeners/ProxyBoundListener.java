package fr.badblock.bungeecord.plugins.others.listeners;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.cloudflare.api.constants.RecordType;
import com.cloudflare.api.requests.dns.DNSAddRecord;
import com.cloudflare.api.requests.dns.DNSDeleteRecord;
import com.cloudflare.api.results.CloudflareError;
import com.cloudflare.api.utils.TimeUnit;
import com.cloudflare.api.utils.TimeUnit.UnitType;

import fr.badblock.bungeecord.plugins.ladder.LadderBungee;
import fr.badblock.bungeecord.plugins.others.BadBlockBungeeOthers;
import fr.badblock.bungeecord.plugins.others.database.BadblockDatabase;
import fr.badblock.bungeecord.plugins.others.database.Request;
import fr.badblock.bungeecord.plugins.others.database.Request.RequestType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.ProxyBoundEvent;
import net.md_5.bungee.api.event.ProxyUnableToBindEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import net.sf.json.JSONObject;

public class ProxyBoundListener implements Listener {

	@EventHandler (priority = EventPriority.HIGHEST)
	public void onProxyBound(ProxyBoundEvent event) {
		BadBlockBungeeOthers bungeeOthers = BadBlockBungeeOthers.getInstance();
		@SuppressWarnings("deprecation")
		String a = ProxyServer.getInstance().getConfig().getListeners().iterator().next().getHost().getHostString() + ":" + ProxyServer.getInstance().getConfig().getListeners().iterator().next().getHost().getPort();
		BadblockDatabase.getInstance().addSyncRequest(new Request("SELECT recordId FROM absorbances WHERE ip = '" + a + "'", RequestType.GETTER) {
			@Override
			public void done(ResultSet resultSet) {
				try {
					if (resultSet.next()) {
						int recordId = resultSet.getInt("recordId");
						if (recordId != 0) {
							DNSDeleteRecord dns = new DNSDeleteRecord(bungeeOthers.getAccess(), "badblock.fr", recordId);
							try {
								JSONObject jsonObject = dns.executeBasic();
								if (jsonObject != null) {
									System.out.println(jsonObject);
									BadblockDatabase.getInstance().addSyncRequest(new Request("INSERT INTO history(ip, action, log, date) VALUES('" + a + "', 'Deleted record in cloudflare (ProxyBoundEvent1 / ID " + recordId + ")', '" + jsonObject.toString() + "', '" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SS").format(new Date()) + "')", RequestType.SETTER));
									BadblockDatabase.getInstance().addSyncRequest(new Request("UPDATE absorbances SET recordId = '0' WHERE ip = '" + a + "'", RequestType.SETTER));
								}else{
									BadblockDatabase.getInstance().addSyncRequest(new Request("INSERT INTO history(ip, action, log, date) VALUES('" + a + "', 'Fail: Deleted record in cloudflare (ProxyBoundEvent1 / ID " + recordId + ")', 'null', '" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SS").format(new Date()) + "')", RequestType.SETTER));
								}
								System.out.println("Deleted " + recordId + " record.");
							} catch (CloudflareError e) {
								e.printStackTrace();
							}
							try {
								dns.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		@SuppressWarnings("deprecation")
		DNSAddRecord dns = new DNSAddRecord(bungeeOthers.getAccess(), "badblock.fr", RecordType.IPV4Address, "roundrobin." + (LadderBungee.getInstance().countEnvironment == 1 ? "eu" : "na") + ".badblock.fr", ProxyServer.getInstance().getConfig().getListeners().iterator().next().getHost().getHostString(), new TimeUnit(UnitType.MINUTES, 2));
		try {
			JSONObject object = dns.executeBasic();
			if (object != null) {
				if (object.has("rec")) {
					JSONObject rec = object.getJSONObject("rec");
					if (rec != null) {
						if (rec.has("obj")) {
							JSONObject obj = rec.getJSONObject("obj");
							if (obj != null) {
								if (obj.has("rec_id")) {
									bungeeOthers.setRecordId(obj.getInt("rec_id"));
									System.out.println(obj.getInt("rec_id"));
									BadblockDatabase.getInstance().addSyncRequest(new Request("INSERT INTO history(ip, action, log, date) VALUES('" + a + "', 'Added record in cloudflare (ID " + bungeeOthers.getRecordId() + ")', '" + object.toString() + "', '" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SS").format(new Date()) + "')", RequestType.SETTER));
									BadblockDatabase.getInstance().addSyncRequest(new Request("UPDATE absorbances SET recordId = '" + bungeeOthers.getRecordId() + "' WHERE ip = '" + a + "'", RequestType.SETTER));
								}
							}
						}
					}
					System.out.println(rec.toString());
				}
				System.out.println("/!\\ BUNGEEDNS<ADDED/" + LadderBungee.getInstance().bungeePlayerCount + "/" + BadBlockBungeeOthers.getInstance().getConnections() + "> /!\\");
			}else System.out.println("/!\\ BUNGEEDNS<NOT-ADDED/" + LadderBungee.getInstance().bungeePlayerCount + "/" + BadBlockBungeeOthers.getInstance().getConnections() + "> /!\\");
			dns.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Added RecordID: " + bungeeOthers.getRecordId());
	}

	@EventHandler (priority = EventPriority.HIGHEST)
	public void onProxyUnableToBind(ProxyUnableToBindEvent event) {
		System.out.println("Unable to bind!");
		if (event.getThrowable() != null)
			event.getThrowable().printStackTrace();
		BadBlockBungeeOthers bungeeOthers = BadBlockBungeeOthers.getInstance();
		@SuppressWarnings("deprecation")
		String a = ProxyServer.getInstance().getConfig().getListeners().iterator().next().getHost().getHostString() + ":" + ProxyServer.getInstance().getConfig().getListeners().iterator().next().getHost().getPort();
		BadblockDatabase.getInstance().addSyncRequest(new Request("SELECT recordId FROM absorbances WHERE ip = '" + a + "'", RequestType.GETTER) {
			@Override
			public void done(ResultSet resultSet) {
				try {
					if (resultSet.next()) {
						int recordId = resultSet.getInt("recordId");
						if (recordId != 0) {
							DNSDeleteRecord dns = new DNSDeleteRecord(bungeeOthers.getAccess(), "badblock.fr", recordId);
							try {
								JSONObject jsonObject = dns.executeBasic();
								if (jsonObject != null) {
									BadblockDatabase.getInstance().addSyncRequest(new Request("INSERT INTO history(ip, action, log, date) VALUES('" + a + "', 'Deleted record in cloudflare (ProxyUnableToBindEvent / ID " + recordId + ")', '" + jsonObject.toString() + "', '" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SS").format(new Date()) + "')", RequestType.SETTER));
									BadblockDatabase.getInstance().addSyncRequest(new Request("UPDATE absorbances SET recordId = '0' WHERE ip = '" + a + "'", RequestType.SETTER));
								}else{
									BadblockDatabase.getInstance().addSyncRequest(new Request("INSERT INTO history(ip, action, log, date) VALUES('" + a + "', 'Fail: Deleted record in cloudflare (ProxyUnableToBindEvent / ID " + recordId + ")', 'null', '" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SS").format(new Date()) + "')", RequestType.SETTER));
								}
								System.out.println("Deleted " + recordId + " record.");
							} catch (CloudflareError e) {
								e.printStackTrace();
							}
							try {
								dns.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		System.exit(-1);
	}

}
