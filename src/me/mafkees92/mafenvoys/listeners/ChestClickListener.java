package me.mafkees92.mafenvoys.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.mafkees92.mafenvoys.EnvoyManager;
import me.mafkees92.mafenvoys.Main;

public class ChestClickListener implements Listener{

	Main plugin;
	
	public ChestClickListener(Main main) {
		this.plugin = main;
	}
	
	@EventHandler
	public void chestOpenEvent(PlayerInteractEvent event) {
		if(event.getAction().equals(Action.LEFT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if(event.getClickedBlock().getType().equals(Material.CHEST)){
				if(EnvoyManager.getInstance().isEnvoyCrate(event.getClickedBlock().getLocation())){
					event.setCancelled(true);
					EnvoyManager.getInstance().claimEnvoy(event.getClickedBlock().getLocation(), event.getPlayer());
				}
			}
		}
	}
}
