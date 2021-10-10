package me.mafkees92.mafenvoys;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;

import me.mafkees92.mafenvoys.Utils.Utils;

public class Holograms {
	
	//make a way to update a specific textline
	public static List<TextLine> AddHologram(Location location, ItemStack item, String... lines) {
		List<TextLine> textLines = new ArrayList<>();
		Hologram holo = HologramsAPI.createHologram(Main.getInstance(), location);
		for (String string : lines) {
			textLines.add(holo.appendTextLine(Utils.colorize(string)));
		}
		holo.appendItemLine(item);
		return textLines;
	}
	
	public static List<TextLine> AddHologram(Location location,  String... lines) {
		List<TextLine> textLines = new ArrayList<>();
		Hologram holo = HologramsAPI.createHologram(Main.getInstance(), location);
		for (String string : lines) {
			textLines.add(holo.appendTextLine(Utils.colorize(string)));
		}
		return textLines;
	}
	
	public static List<TextLine> AddHologram(Location location, ItemStack item, List<String> lore) {
		List<TextLine> textLines = new ArrayList<>();
	
		Hologram holo = HologramsAPI.createHologram(Main.getInstance(), location);
		Iterator<String> it = lore.iterator();
		while(it.hasNext()) {
			String loreLine = it.next();
			textLines.add(holo.appendTextLine(Utils.colorize(loreLine)));
		}
		holo.appendItemLine(item);
		return textLines;
	}
	
	public static List<TextLine> AddHologram(Location location, List<String> lore) {
		List<TextLine> textLines = new ArrayList<>();
	
		Hologram holo = HologramsAPI.createHologram(Main.getInstance(), location);
		Iterator<String> it = lore.iterator();
		while(it.hasNext()) {
			String loreLine = it.next();
			textLines.add(holo.appendTextLine(Utils.colorize(loreLine)));
		}
		return textLines;
	}

	public static void RemoveHologram(Location location) {
		HologramsAPI.getHolograms(Main.getInstance()).stream()
				.filter(x -> x.getLocation().equals(location)).findFirst().ifPresent(Hologram::delete);
	}
	
	public static void RemoveAllHolograms() {
		HologramsAPI.getHolograms(Main.getInstance()).forEach(Hologram::delete);
	}
	
	public static Hologram GetHologram(Location location) {
		return HologramsAPI.getHolograms(Main.getInstance()).stream()
		.filter(x -> x.getLocation().equals(location)).findFirst().orElse(null);
	}
	
}
