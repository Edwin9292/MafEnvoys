package me.mafkees92.mafenvoys.Files;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;

import me.mafkees92.mafenvoys.Main;

public class DataFile extends BaseFile {

	ArrayList<Location> locations = new ArrayList<Location>();
	private static DataFile instance;
	
	public DataFile(Main plugin, String fileName) {
		super(plugin, fileName);
		// TODO Auto-generated constructor stub
		
		if(DataFile.instance == null)
			DataFile.instance = this;
		
		loadConfig();
	}
	
	public static DataFile getInstance() {
		return instance;
	}
	
	
	private void loadConfig() {
		
		Object obj = config.get("SpawnedLocations");
		if(obj == null) return;
		
		@SuppressWarnings("unchecked")
		ArrayList<Location> locations = (ArrayList<Location>)obj;
		
		if(locations.size() > 0) {
			for(Location location: locations) {
				location.getBlock().setType(Material.AIR);
			}
		}
		locations.clear();
		saveLocations();
	}

	private void saveLocations() {
		config.set("SpawnedLocations", this.locations);
		save();
	}
	
	public void addLocation(Location location) {
		this.locations.add(location);
		saveLocations();
	}
	
	public void removeLocation(Location location) {
		this.locations.remove(location);
		saveLocations();
	}
	
}
