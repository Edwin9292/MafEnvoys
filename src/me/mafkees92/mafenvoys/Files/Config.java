package me.mafkees92.mafenvoys.Files;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import me.mafkees92.mafenvoys.Main;
import me.mafkees92.mafenvoys.Data.SpawnData;

public class Config{

	private static ArrayList<SpawnData> spawnPoints = new ArrayList<>();
	
	public static int YLevelToSpawnAt = 200;
	public static int envoyTimerInSeconds;
	
	
	public Config(Main plugin) {
		loadConfig(plugin.getConfig());
	}
	
	
	public void loadConfig(FileConfiguration config) {
		
		Config.envoyTimerInSeconds = config.getInt("envoyTimer", 3600);
		
		if(config.getConfigurationSection("spawnPoints.worldGuard") == null){
			config.createSection("spawnPoints.worldGuard");
		}
		
		if(Config.spawnPoints.size() > 0) {
			Config.spawnPoints.clear();
		}
		
		try {
			for (String key : config.getConfigurationSection("spawnPoints.worldGuard").getKeys(false)) {
				World world = Bukkit.getWorld(config.getString("spawnPoints.worldGuard." + key + ".world"));
				String regionName = config.getString("spawnPoints.worldGuard." + key + ".region");
				int amount = config.getInt("spawnPoints.worldGuard." + key + ".amount");
				int timesToTry = config.getInt("spawnPoints.worldGuard." + key + ".timesToTry");
				int minY = config.getInt("spawnPoints.worldGuard." + key + ".minY", -1);
				int maxY = config.getInt("spawnPoints.worldGuard." + key + ".maxY", -1);
				
				Config.spawnPoints.add(new SpawnData(world, regionName, amount, timesToTry, minY, maxY));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<SpawnData> getSpawnPoints(){
		return Config.spawnPoints;
	}
	
}
