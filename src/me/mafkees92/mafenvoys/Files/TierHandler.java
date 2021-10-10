package me.mafkees92.mafenvoys.Files;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.inventory.ItemStack;

import me.mafkees92.mafenvoys.Main;
import me.mafkees92.mafenvoys.Data.EnvoyTier;
import me.mafkees92.mafenvoys.Utils.Utils;

public class TierHandler extends BaseFile{
	
	private static TierHandler instance;
	ArrayList<EnvoyTier> tiers = new ArrayList<>();

	public TierHandler(Main plugin, String fileName) {
		super(plugin, fileName);
		
		if(instance == null)
			instance = this;
		
		loadTiers();
	}
	
	
	public static TierHandler getInstance() {
		return instance;
	}
	
	@SuppressWarnings("unchecked")
	public void loadTiers() {
		for(String key : config.getConfigurationSection("Tiers").getKeys(false)) {
		    String path = "Tiers." + key;

			Object obj = config.get(path);
			if(obj == null) return;
			ArrayList<ItemStack> items = (ArrayList<ItemStack>)config.get(path + ".items");

			double chance = config.getDouble(path + ".chance");
					
			this.tiers.add(new EnvoyTier(key, items, chance));
    	}
	}

	public void saveEnvoyTiers() {
		for (EnvoyTier tier : tiers) {
			
			config.set("Tiers." + tier.getName() + ".chance", tier.getChance());
			config.set("Tiers." + tier.getName() + ".items", tier.getItems().toArray(new ItemStack[0]));
			save();
		}
	}
	
	public boolean createTier(String tierName, double chance) {
		EnvoyTier tier = tiers.stream().filter(x -> x.getName().equalsIgnoreCase(tierName)).findFirst().orElse(null);
		if(tier != null) {
			return false;
		}
		else {
			tier = new EnvoyTier(tierName, new ArrayList<ItemStack>(), chance);
			this.tiers.add(tier);
			this.saveEnvoyTiers();
			return true;
		}
	}
	
	public boolean addItem(String tierName, ItemStack item) {
		EnvoyTier tier = tiers.stream().filter(x -> x.getName().equalsIgnoreCase(tierName)).findFirst().orElse(null);
		if(tier == null) {
			return false;
		}
		else {
			tier.addItem(item);
			this.saveEnvoyTiers();
			return true;
		}
	}
	
	public void setItems(String tierName, ArrayList<ItemStack> items) {
		EnvoyTier tier = tiers.stream().filter(x -> Utils.colorize(x.getName()).equalsIgnoreCase(tierName)).findFirst().orElse(null);
		if(tier != null) {
			tier.setItems(items);
			this.saveEnvoyTiers();
		}
	}
	
	public EnvoyTier getTier(String name) {
		return tiers.stream().filter(x -> x.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
	}
	
	public EnvoyTier getRandomTier() {
		int combinedChance = 0;
		for (EnvoyTier envoyTier : tiers) {
			combinedChance += envoyTier.getChance() * 100;
		}
		
		Random random = new Random();
		int randomChance = random.nextInt(combinedChance);
		
		int counter = 0;
		for (EnvoyTier envoyTier : tiers) {
			counter += envoyTier.getChance() * 100;
			if(counter > randomChance)
				return envoyTier;
		}
		return tiers.get(new Random().nextInt(tiers.size()));
	}
	
	public ArrayList<EnvoyTier> getAllTiers(){
		return this.tiers;		
	}
	
	
}