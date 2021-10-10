package me.mafkees92.mafenvoys.Files;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.mafkees92.mafenvoys.Main;

public abstract class BaseFile {

	protected Main plugin;
	private final File file;
	protected FileConfiguration config;

	public BaseFile(Main plugin, String fileName) {

		this.plugin = plugin;
		this.file = new File(plugin.getDataFolder(), fileName);
		
		if(file.exists()) {
            try {
                config = new YamlConfiguration();
                config.load(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
		}
		else {
            // Create the missing file
            config = new YamlConfiguration();
            
            try {
                if (plugin.getResource(fileName) != null) {
                    plugin.getLogger().info("Using default found in jar file.");
                    plugin.saveResource(fileName, false);
                    config = new YamlConfiguration();
                    config.load(file);
                } else {
                    config.save(file);
                }
            } catch (Exception e) {
                plugin.getLogger().severe("Could not create the " + file + " file!");
            }
		}
	}
	
	public void save() {
		try {
			config.save(file);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
