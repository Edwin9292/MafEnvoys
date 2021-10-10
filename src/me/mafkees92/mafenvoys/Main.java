package me.mafkees92.mafenvoys;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.mafkees92.mafenvoys.Commands.EnvoysCommand;
import me.mafkees92.mafenvoys.Files.Config;
import me.mafkees92.mafenvoys.Files.Messages;
import me.mafkees92.mafenvoys.GUI.GuiManager;
import me.mafkees92.mafenvoys.listeners.ChestClickListener;

public class Main extends JavaPlugin{

	private static Main instance;
	private EnvoyManager crateManager;
	
	
	public void onEnable() {
		
		if(Main.instance == null) {
			Main.instance = this;
		}
		
		//load files
		new Config(this);
		new Messages(this, "Messages.yml");
		crateManager = new EnvoyManager(this);
		
		Bukkit.getPluginManager().registerEvents(new ChestClickListener(this), this);
		Bukkit.getPluginManager().registerEvents(new GuiManager(), this);
		getCommand("envoys").setExecutor(new EnvoysCommand(this));
	}
	
	public void onDisable() {
		crateManager.removeAllCrates();
		Holograms.RemoveAllHolograms();
	}
	
	public static Main getInstance() {
		return Main.instance;		
	}
	
	
}
