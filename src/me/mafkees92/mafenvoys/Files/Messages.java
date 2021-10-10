package me.mafkees92.mafenvoys.Files;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import me.mafkees92.mafenvoys.Main;
import me.mafkees92.mafenvoys.Data.EnvoyTier;
import me.mafkees92.mafenvoys.Utils.Utils;

public class Messages extends BaseFile{

	public Messages(Main plugin, String fileName) {
		super(plugin, fileName);
		
		loadMessages();
	}

	public void loadMessages() {
		Messages.noPermission = Utils.colorize(config.getString("noPermission"));
		Messages.claimedCrate = Utils.colorize(config.getString("claimedCrate"));
		Messages.allCratesClaimed = Utils.colorize(config.getString("allCratesClaimed"));
		Messages.singleEnvoySpawned = Utils.colorize(config.getString("singleEnvoySpawned"));
		Messages.envoyHologramLines = Utils.colorize(config.getStringList("envoyHologramLines"));	
		Map<String, Object> timedMessages = config.getConfigurationSection("timerMessages").getValues(false);
		for (Map.Entry<String, Object> set : timedMessages.entrySet()) {
			int time = Utils.tryParseInt(set.getKey());
			if(time != -1) {
				if(Messages.timedMessages == null) {
					Messages.timedMessages = new HashMap<Integer, String>();
				}
				Messages.timedMessages.put(time, Utils.colorize((String)set.getValue()));
			}
		}
	}
	
	public static String noPermission;
	public static String claimedCrate;
	public static String allCratesClaimed;
	public static String singleEnvoySpawned;
	private static List<String> envoyHologramLines;
	public static List<String> envoyHologramLines(EnvoyTier tier){
		ArrayList<String> hologramLines = new ArrayList<String>(Messages.envoyHologramLines);
		ListIterator<String> it = hologramLines.listIterator();
		while(it.hasNext()) {
			String line = it.next().replace("{tier_name}", tier.getName());
			it.set(line);
		}
		return hologramLines;
	}
	public static HashMap<Integer, String> timedMessages;
	
}
