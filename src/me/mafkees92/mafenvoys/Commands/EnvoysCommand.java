package me.mafkees92.mafenvoys.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.mafkees92.mafenvoys.EnvoyManager;
import me.mafkees92.mafenvoys.Main;
import me.mafkees92.mafenvoys.Files.TierHandler;
import me.mafkees92.mafenvoys.GUI.GuiManager;
import me.mafkees92.mafenvoys.Utils.Utils;

public class EnvoysCommand implements CommandExecutor{

	Main plugin;
	
	
	public EnvoysCommand(Main main) {
		this.plugin = main;
		
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		// TODO Auto-generated method stub
		if(!(sender instanceof Player))
			return false;
		Player player = (Player) sender;
		
		if(EnvoyManager.timer <= 60) {
			player.sendMessage(Utils.colorize("&7Next envoy will spawn in &6" + EnvoyManager.timer + " seconds&7."));
		}else if(EnvoyManager.timer <= 3600){
			player.sendMessage(Utils.colorize("&7Next envoy will spawn in &6" + (EnvoyManager.timer/60) + " minutes&7."));
		}
		else {
			player.sendMessage(Utils.colorize("&7Next envoy will spawn in &6" + (EnvoyManager.timer/3600) + " hours&7 and &6"
					+ ((EnvoyManager.timer%3600)/60) + " minutes&7."));
		}
		
		if(!player.isOp()) {
			return true;
		}

		if(args.length < 1) {
			sendHelpMenu(player);
			return true;
		}
		
		// /envoys createtier tiername chance
		if(args[0].equalsIgnoreCase("createtier")) {
			if(args.length != 3) {
				player.sendMessage(Utils.colorize("&cInvalid format. Try /mafenvoys createtier [tiername] [chance]"));
				return true;
			}
			String name = args[1];
		    double chance =  Utils.tryParseDouble(args[2]);
		    if(chance == -1) {
		    	player.sendMessage(Utils.colorize("&c" + args[2] + " is not a valid number."));
		    	return true;
		    }
			TierHandler.getInstance().createTier(name, chance);
			player.sendMessage("&7Tier &6" + name + "&7 has successfully been created.");
		}
		
		// /envoys additem [tiername]
		if(args[0].equalsIgnoreCase("additem")) {
			if(args.length != 2) {
				player.sendMessage(Utils.colorize("&cInvalid format. Try /mafenvoys additem [tiername]"));
				return true;
			}
			String tierName = args[1];	
			ItemStack item = player.getInventory().getItemInMainHand();
			if(TierHandler.getInstance().addItem(tierName, item)) {
				player.sendMessage(Utils.colorize("&aSuccesfully added the item in your hand"));
			}
			else {
				player.sendMessage(Utils.colorize("&cFailed to add the item to tier &6" + tierName + "&c. This tier does not yet exist."));
			}
		}
		if(args[0].equalsIgnoreCase("spawn")) {
			if(args.length != 1) {
				sendHelpMenu(player);
				return true;
			}
			EnvoyManager.getInstance().spawnEnvoys();
			player.sendMessage(Utils.colorize("You have spawned in the envoys!"));
			return true;
		}
		if(args[0].equalsIgnoreCase("gui")) {
			GuiManager.displayTierInventory(player);
			return true;
		}
		else {
			sendHelpMenu(player);
		}
		return true;
	}

	private void sendHelpMenu(Player player) {
		player.sendMessage(Utils.colorize("&6Maf Envoys help panel"));
		player.sendMessage(Utils.colorize("--------------------------------------------"));
		player.sendMessage(Utils.colorize(""));
		player.sendMessage(Utils.colorize("&e/envoys createtier [name] [chance] : &7Create a new tier with set chance to spawn."));
		player.sendMessage(Utils.colorize("&e/envoys additem [tiername] : &7Add the item in your hand to a tier."));
		player.sendMessage(Utils.colorize("&e/envoys spawn : &7Spawn the envoys on command."));
		player.sendMessage(Utils.colorize("&e/envoys gui : &7View all envoy tiers and add/remove items from them."));
		player.sendMessage(Utils.colorize(""));
		player.sendMessage(Utils.colorize(""));
		player.sendMessage(Utils.colorize("--------------------------------------------"));
		player.sendMessage(Utils.colorize(""));
	}
}
