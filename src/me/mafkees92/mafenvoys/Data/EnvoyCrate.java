package me.mafkees92.mafenvoys.Data;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.mafkees92.mafenvoys.Holograms;
import me.mafkees92.mafenvoys.Main;
import me.mafkees92.mafenvoys.Files.DataFile;
import me.mafkees92.mafenvoys.Files.Messages;

public class EnvoyCrate {
	
	EnvoyTier tier;
	Location spawnLocation;
	Location chestLocation;
	ArmorStand fallingArmorStand;
	
	private final Vector hologramOffset = new Vector(0.5, 1.99D, 0.5);
	
	public EnvoyCrate(Location location, EnvoyTier tier) {
		this.tier = tier;
		this.spawnLocation = location;
		spawnArmorStand(location);
		//Bukkit.broadcastMessage(Messages.singleEnvoySpawned
		//		.replace("{tier_name}", tier.getName())
		//		.replace("{world_name}", location.getWorld().getName())
		//		.replace("{location_X}", "" + location.getBlockX())
		//		.replace("{location_Z}", "" + location.getBlockZ()));
	}
	
	private void spawnArmorStand(Location locToSpawn) {
		ArmorStand stand = (ArmorStand) locToSpawn.getWorld().spawnEntity(locToSpawn.clone().add(0.5,0,0.5), EntityType.ARMOR_STAND);
		stand.setHelmet(new ItemStack(Material.CHEST));
		stand.setVisible(false);
		
		this.fallingArmorStand = stand;
		
		new BukkitRunnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (stand.isOnGround()) {
					fallingArmorStand = null;
					Location loc = stand.getLocation();
					loc.setY(Math.round(loc.getY()));
					stand.remove();
					
					Location emptyBlock = loc.clone();
					for (int i = 0; i < 10; i++) {
						if (!emptyBlock.getBlock().getType().equals(Material.AIR)) {
							emptyBlock.add(0, 1, 0);
							continue;
						} 
						else {
							spawnChest(emptyBlock.getBlock().getLocation());
							break;
						}
					}
					this.cancel();
				} else {
					stand.setVelocity(new Vector(0, -0.75, 0));
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 3);
	}
	
	private void spawnChest(Location locationToSpawn) {
		DataFile.getInstance().addLocation(locationToSpawn);
		locationToSpawn.getBlock().setType(Material.CHEST);
		this.chestLocation = locationToSpawn;

		Holograms.AddHologram(this.chestLocation.clone().add(hologramOffset), Messages.envoyHologramLines(this.tier));
	}
		
	
	public void remove() {
		removeArmorStand();
		if(this.chestLocation!= null) {
			this.chestLocation.getBlock().setType(Material.AIR);
			Holograms.RemoveHologram(this.chestLocation.clone().add(hologramOffset));
		}
	}
	
	private void removeArmorStand() {
		if(this.fallingArmorStand != null)
			this.fallingArmorStand.remove();
	}
	
	public Location getChestLocation() {
		return this.chestLocation;
	}
	
	public EnvoyTier getTier() {
		return this.tier;
	}
	
	public Location getSpawnLocation() {
		return this.spawnLocation;
	}
	
	public boolean isFalling() {
		return this.fallingArmorStand != null;
	}
	
}

