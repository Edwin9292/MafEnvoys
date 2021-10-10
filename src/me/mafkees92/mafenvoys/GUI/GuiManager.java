package me.mafkees92.mafenvoys.GUI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.mafkees92.mafenvoys.Data.EnvoyTier;
import me.mafkees92.mafenvoys.Files.TierHandler;
import me.mafkees92.mafenvoys.Utils.Utils;

public class GuiManager implements Listener{

	
	
	public static void displayTierInventory(Player player) {
		
		Inventory inv = Bukkit.createInventory(player, 45, Utils.colorize("&eMafEnvoys tierMenu"));
		
		List<EnvoyTier> envoyTiers = TierHandler.getInstance().getAllTiers();
		
		for(EnvoyTier tier : envoyTiers) {
			ItemStack item = new ItemStack(Material.PAPER);
			
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(Utils.colorize("Tier: " + tier.getName()));
			
			List<String> lore = new ArrayList<>();
			lore.add(Utils.colorize("&aName: &6" + tier.getName()));
			lore.add(Utils.colorize("&aChance: &6" + tier.getChance()));
			meta.setLore(lore);
			
			item.setItemMeta(meta);
			item = Utils.setNBTTag(item, "mafEnvoyTiers", tier.getName());
			inv.addItem(item);
		}
		
		player.openInventory(inv);
	}
	
	private void displayTierItemsInventory(Player player, String tierName) {
		ArrayList<ItemStack> items = TierHandler.getInstance().getTier(tierName).getItems();
		Inventory inv = Bukkit.createInventory(player, 45, Utils.colorize("&eMafEnvoys tierItems Tier: &6" + tierName ));
		
		inv.setContents(items.toArray(new ItemStack[0]));
		player.openInventory(inv);
	}
	
	
	
	
	@EventHandler
	public void inventoryClickEvent(InventoryClickEvent e) {
		if(e.getInventory().getName().equals(Utils.colorize("&eMafEnvoys tierMenu"))) {
			String tag = Utils.getNBTTag(e.getCurrentItem(), "mafEnvoyTiers");
			if((tag != null)) {
				e.setCancelled(true);
				e.getWhoClicked().closeInventory();
				displayTierItemsInventory((Player)e.getWhoClicked(), tag);
			}
		}	
	}
	
	
	@EventHandler
	public void inventoryDragHandler(InventoryDragEvent e) {
		if(e.getInventory().getName().equals(Utils.colorize("&eMafEnvoys tierMenu"))) {
			e.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void inventoryCloseEvent(InventoryCloseEvent e) {
		if(e.getInventory().getName().equals(Utils.colorize("&eMafEnvoys tierMenu"))) {
			//nothing for now
		}
		if(e.getInventory().getName().contains(Utils.colorize("&eMafEnvoys tierItems Tier: &6"))){
			String tierName = e.getInventory().getName().replace(Utils.colorize("&eMafEnvoys tierItems Tier: &6"), "");
			ArrayList<ItemStack> items = new ArrayList<>();
			for(ItemStack item : e.getInventory().getContents()) {
				if(item != null) items.add(item);
			}
			TierHandler.getInstance().setItems(tierName, items);
		}
		
	}
	
}
















