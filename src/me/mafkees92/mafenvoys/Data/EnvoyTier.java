package me.mafkees92.mafenvoys.Data;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;

public class EnvoyTier {

	String tierName;
	ArrayList<ItemStack> items;
	double chance;
	
	public EnvoyTier(String tierName, ArrayList<ItemStack> items, double chance) {
		this.tierName = tierName;
		this.items = items;
		this.chance = chance;
	}
	
	
	public String getName() {
		return this.tierName;
	}
	
	public double getChance() {
		return this.chance;
	}
	
	public ArrayList<ItemStack> getItems() {
		return this.items;
	}
	
	public void addItem(ItemStack item) {
		this.items.add(item);
	}
	
	public void setItems(ArrayList<ItemStack> items) {
		this.items = items;
	}
}
