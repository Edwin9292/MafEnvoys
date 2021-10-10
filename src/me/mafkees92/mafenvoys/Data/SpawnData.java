package me.mafkees92.mafenvoys.Data;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import me.mafkees92.mafenvoys.EnvoyManager;
import me.mafkees92.mafenvoys.Files.Config;
import me.mafkees92.mafenvoys.Files.TierHandler;

public class SpawnData {

	private int amountOfCrates;
	private int amountOfTries;
	private int minY;
	private int maxY;
	
	private World world;
	private	String regionName;
	private boolean isWorldGuardLocation = false;
	
	
	private boolean randomTier = true; 
	private ArrayList<EnvoyTier> tiers;

	public SpawnData(World world, String regionName, int amountOfCrates, int amountOfTries, int minY, int maxY) {

		this.isWorldGuardLocation = true;
		this.world = world;
		this.regionName = regionName;
		this.amountOfCrates = amountOfCrates;
		this.amountOfTries = amountOfTries;
		this.minY = minY;
		this.maxY = maxY;
	}

	
	public int getAmountOfCrates() {
		return this.amountOfCrates;
	}

	public int getAmountofTries() {
		return this.amountOfTries;
	}
	
	public Location getLocation() {
		if(isWorldGuardLocation) {
			RegionContainer container = WorldGuardPlugin.inst().getRegionContainer();
			RegionManager manager = container.get(this.world);
			ProtectedRegion region = manager.getRegion(this.regionName);
			if(region == null) {
				Bukkit.getLogger().warning("ERROR: Region " + this.regionName + " has not been found.");
				return null;
			}
			
			for (int i = 0; i < this.amountOfTries; i++) {
				BlockVector max = region.getMaximumPoint();
				BlockVector min = region.getMinimumPoint();

				Random random = new Random();
				int x = random.nextInt(max.getBlockX() - min.getBlockX()) + min.getBlockX();
				int y = Config.YLevelToSpawnAt;
				int z = random.nextInt(max.getBlockZ() - min.getBlockZ()) + min.getBlockZ();
				Location location = new Location(world, x,y,z);
				if(!isAlreadySpawning(location) ){
				    if(getDestinationLocation(location) != null){
						return location;
				    }
				}
			}
			return null;
		}
		else {
			return null; // TODO: update it to add fixed positions
		}
	}
	
	public EnvoyTier getTier() {
		if(randomTier) {
			return TierHandler.getInstance().getRandomTier();
		}
		else {
			if(this.tiers != null) {
				return this.tiers.get(new Random().nextInt(this.tiers.size()));
			}
			else {
				return null;
			}
		}
	}
	
	
	private boolean isAlreadySpawning(Location locToSpawn) {
		Location north = locToSpawn.clone().add(1,0,0);
		Location east = locToSpawn.clone().add(-1,0,0);
		Location south = locToSpawn.clone().add(0,0,1);
		Location west = locToSpawn.clone().add(0,0,-1);
		Location below = locToSpawn.clone().add(0,-1,0);
		for (EnvoyCrate crate : EnvoyManager.getInstance().spawnedCrates) {
			if(crate.isFalling()) {
				if(crate.getSpawnLocation().equals(locToSpawn) ||
						crate.getSpawnLocation().equals(north) ||
						crate.getSpawnLocation().equals(east) ||
						crate.getSpawnLocation().equals(south) ||
						crate.getSpawnLocation().equals(west) ||
						crate.getSpawnLocation().equals(below)) { 
					return true;
				}
			}
		}
		return false;
	}
	
	
	private Location getDestinationLocation(Location spawnLocation) {
		Location locToCheck = spawnLocation.clone();	
		
		for (int i = (int)locToCheck.getY(); i > 0 ; i--) {
			//check if there is a solid block that the armor stand can fall on
			if(locToCheck.subtract(0,1,0).getBlock().getType().isSolid()){
					//check if there is an airblock in the 3 blocks above it where the chest can be spawned
				for (int j = 0; j < 3; j++) {
					if(locToCheck.add(0,1,0).getBlock().getType().equals(Material.AIR)){		
						if (!areNeighbourBlocksValid(locToCheck)){
							return null;
						}
						else {
							if((minY != -1 && locToCheck.getBlockY() < minY) || (maxY != -1 && locToCheck.getBlockY() > maxY)) {
								return null;
							}
							else {
								return locToCheck;
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	private boolean areNeighbourBlocksValid(Location locToCheck) {
		Block north = locToCheck.getWorld().getBlockAt(locToCheck.getBlockX(), locToCheck.getBlockY(), locToCheck.getBlockZ() -1);
		Block east = locToCheck.getWorld().getBlockAt(locToCheck.getBlockX() +1, locToCheck.getBlockY(), locToCheck.getBlockZ());
		Block south = locToCheck.getWorld().getBlockAt(locToCheck.getBlockX(), locToCheck.getBlockY(), locToCheck.getBlockZ() +1);
		Block west = locToCheck.getWorld().getBlockAt(locToCheck.getBlockX() -1, locToCheck.getBlockY(), locToCheck.getBlockZ()); 
		Block below = locToCheck.getWorld().getBlockAt(locToCheck.getBlockX(), locToCheck.getBlockY() -1, locToCheck.getBlockZ());

		
		
		return !(
				north.getType().equals(Material.CHEST) ||
				east.getType().equals(Material.CHEST) ||
				south.getType().equals(Material.CHEST) ||
				west.getType().equals(Material.CHEST) ||
				below.getType().equals(Material.CHEST) );
	}
	
	
}
