package fr.badblock.badybots.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import fr.badblock.badybots.service.logs.Log;
import fr.badblock.badybots.service.logs.Log.LogType;

public class Main {

	private Thread			thread	= Thread.currentThread();
	private HashSet<String> ips;
	private File			file	= new File("auto_db.dat");

	public Main() {
		System.out.println("__________________________________________________________________");
		System.out.println(" ____            _       ____        _       ");
		System.out.println("|  _ \\          | |     |  _ \\      | |      ");
		System.out.println("| |_) | __ _  __| |_   _| |_) | ___ | |_ ___ ");
		System.out.println("|  _ < / _` |/ _` | | | |  _ < / _ \\| __/ __|");
		System.out.println("| |_) | (_| | (_| | |_| | |_) | (_) | |_\\__ \\");
		System.out.println("|____/ \\__,_|\\__,_|\\__, |____/ \\___/ \\__|___/");
		System.out.println("                    __/ |                    ");
		System.out.println("                   |___/                     ");
		System.out.println("");
		System.out.println("        Version 0.1 • by xMalware for BadBlock");
		System.out.println("__________________________________________________________________");
		System.out.println(" ");
		long time = System.currentTimeMillis();
		Log.log("Loading configuration...", LogType.INFO);
		load();
		Log.log("Loaded configuration in " + String.format("%.2f", (System.currentTimeMillis() - time) / 1000.0D) + "s.", LogType.SUCCESS);
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				launch();
			}
		}, 0, 30000);
	}

	public void launch() {
		newThread(new Runnable() {
			@Override
			public void run() {
				try {
					List<String> lines = getUrlSource("http://myip.ms/files/blacklist/csf/latest_blacklist.txt");
					for (String string : lines) {
						if (string.equals("") || string.startsWith("#")) continue;
						if (!ips.contains(string)) {
							ips.add(string);
							save();
							Log.log("[" + ips.size() + "] Added IP " + string, LogType.SUCCESS);
						}
					}
					System.gc();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		newThread(new Runnable() {
			@Override
			public void run() {
				try {
					List<String> lines = getUrlSource("http://lists.blocklist.de/lists/all.txt");
					for (String string : lines) {
						if (string.equals("") || string.startsWith("#")) continue;
						if (!ips.contains(string)) {
							ips.add(string);
							save();
							Log.log("[" + ips.size() + "] Added IP " + string, LogType.SUCCESS);
						}
					}
					System.gc();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void newThread(Runnable runnable) {
		new Thread() {
			@Override
			public void run() {
				runnable.run();
			}
		}.start();
	}

	public void addIP(String ip) {
		synchronized (thread) {
			ips.add(ip);
		}
	}

	public void load() {
		if (!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		ips = new HashSet<>();
		try {
			try(BufferedReader br = new BufferedReader(new FileReader(file))) {
				for(String line; (line = br.readLine()) != null;) {
					if (line.isEmpty()) continue;
					ips.add(line);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	public void save() {
		try {
			FileWriter fileWriter = new FileWriter(file, false);
			StringBuilder stringBuilder = new StringBuilder();
			Iterator<String> iterator = ips.iterator();
			while (iterator.hasNext()) {
				stringBuilder.append(iterator.next());
				if (iterator.hasNext()) stringBuilder.append(System.lineSeparator());
			}
			fileWriter.write(stringBuilder.toString());
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	private List<String> getUrlSource(String url) throws IOException {
		URL yahoo = new URL(url);
		URLConnection yc = yahoo.openConnection();
		yc.setRequestProperty("User-Agent", "Mozilla/5.0");
		BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream(), "UTF-8"));
		String inputLine;
		List<String> lines = new ArrayList<>();
		while ((inputLine = in.readLine()) != null)
			lines.add(inputLine);
		in.close();
		return lines;
	}

}
