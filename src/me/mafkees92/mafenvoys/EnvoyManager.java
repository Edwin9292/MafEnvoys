package me.mafkees92.mafenvoys;

import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.mafkees92.mafenvoys.Data.EnvoyCrate;
import me.mafkees92.mafenvoys.Data.EnvoyTier;
import me.mafkees92.mafenvoys.Data.SpawnData;
import me.mafkees92.mafenvoys.Files.Config;
import me.mafkees92.mafenvoys.Files.DataFile;
import me.mafkees92.mafenvoys.Files.Messages;
import me.mafkees92.mafenvoys.Files.TierHandler;
import me.mafkees92.mafenvoys.Utils.Utils;


public class EnvoyManager {

	Main plugin;
	private static EnvoyManager instance; 

	DataFile dataFile;
	TierHandler tierHandler;
	public static int timer;
	
	public ArrayList<EnvoyCrate> spawnedCrates = new ArrayList<>();
	
	
	public EnvoyManager(Main main) {
		this.plugin = main;
		
		if(EnvoyManager.instance == null)
			EnvoyManager.instance = this;
		
		this.dataFile = new DataFile(main, "data.yml");
		this.tierHandler = new TierHandler(main, "TierItems.yml");
		EnvoyManager.timer = Config.envoyTimerInSeconds;
		
		new BukkitRunnable() {
			@Override
			public void run() {
				if(EnvoyManager.timer == 0) {
					spawnEnvoys();
					EnvoyManager.timer = Config.envoyTimerInSeconds;
					return;
				}
				else if(Messages.timedMessages != null) {
					if(Messages.timedMessages.containsKey(EnvoyManager.timer)) {
						Bukkit.broadcastMessage(Messages.timedMessages.get(EnvoyManager.timer));
					}
				}
				EnvoyManager.timer --;
			}
		}.runTaskTimer(main, 20, 20);
	}

	public static EnvoyManager getInstance() {
		return EnvoyManager.instance;
	}
	
	public void spawnEnvoys() {
		int counter = Math.max(2, Bukkit.getOnlinePlayers().size());
		
		int totalAmountOfCrates  = 0;
		for(SpawnData data: Config.getSpawnPoints()) {
			totalAmountOfCrates += data.getAmountOfCrates();
		}
		
		//if we should spawn less crates than possible, pick random locations
		if(counter < totalAmountOfCrates) {
			@SuppressWarnings("unchecked")
			ArrayList<SpawnData> spawnData = (ArrayList<SpawnData>) Config.getSpawnPoints().clone();
			Random random = new Random();
			while(counter > 0 && spawnData.size() > 0) {
				SpawnData randomSpawnData = spawnData.remove(random.nextInt(spawnData.size()));
				for (int i = 0; i < randomSpawnData.getAmountOfCrates(); i++) {
					Location loc = randomSpawnData.getLocation();
					if(loc != null) {
						if(counter > 0) {
							spawnEnvoy(loc, randomSpawnData.getTier());
							counter--;
						}
						else {
							break;
						}
					}
				}
			}
			
		}
		//else just loop trough all locations and spawn them all
		else {
			for(SpawnData data : Config.getSpawnPoints()) { //loop trough all spawnData
				for (int i = 0; i < data.getAmountOfCrates(); i++) { //loop x amount of times (amount of crates to spawn)
					Location loc = data.getLocation();
					if(loc != null) {
						spawnEnvoy(loc, data.getTier());
					}
				}
			}
		}
		Bukkit.broadcastMessage(Utils.colorize("&eEnvoys have been spawned at &6/warp pvp"));
		new BukkitRunnable() {
			@Override
			public void run() {
				removeAllCrates();
			}
		}.runTaskLater(plugin, 180*20);
	}
	
	public void spawnEnvoy(Location locToSpawn, EnvoyTier tierToSpawn) {
		EnvoyCrate crate = new EnvoyCrate(locToSpawn, tierToSpawn);
		spawnedCrates.add(crate);	
	}
	
	public void claimEnvoy(Location location, Player player) {
		for (int i = 0; i < spawnedCrates.size(); i++) {
			EnvoyCrate envoyCrate = spawnedCrates.get(i);
			if(envoyCrate.getChestLocation() == null) return;
			if(envoyCrate.getChestLocation().equals(location)) {
				envoyCrate.remove();
				this.spawnedCrates.remove(envoyCrate);
				dataFile.removeLocation(envoyCrate.getChestLocation());
				
				EnvoyTier tier = envoyCrate.getTier();
				ItemStack item = tier.getItems().get(new Random().nextInt(tier.getItems().size()));
				if(item != null) {
					if(player.getInventory().firstEmpty() != -1) {
						player.getInventory().addItem(item);
					}
					else {
						player.getWorld().dropItemNaturally(location, item);
					}
				}
				player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
				String message = Utils.colorize(Messages.claimedCrate
						.replace("{name}", player.getDisplayName())
						.replace("{tier_name}", tier.getName())
						.replace("{amount_left}", "" +spawnedCrates.size()));
				String[] messages = message.split(Pattern.quote("{nl}"));
				
				for(String string : messages) {
					Bukkit.broadcastMessage(string);
				}
						
				if(spawnedCrates.size() == 0) {
					Bukkit.broadcastMessage(Messages.allCratesClaimed);
					return;
				}
			}
		}
	}
	
	public boolean isEnvoyCrate(Location location) {
		for (EnvoyCrate envoyCrate : spawnedCrates) {
			if(envoyCrate.getChestLocation() == null) continue;
			if(envoyCrate.getChestLocation().equals(location))
				return true;
		}
		return false;
	}
	
	public void removeAllCrates() {
		for (EnvoyCrate crate : spawnedCrates) {
			crate.remove();
		}
		spawnedCrates.clear();
	}
}

