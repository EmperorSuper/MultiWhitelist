package me.EmperorSuper.MultiWhitelist;

import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Yamls {
	private MainClass plugin = MainClass.getPlugin(MainClass.class);
	
	public FileConfiguration cfg;
	public File file;

	public void setup() {
		if (!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdir();
		}

		file = new File(plugin.getDataFolder(), "Data.yml");

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				plugin.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Could not create the Data.yml file");
			}
		}

		cfg = YamlConfiguration.loadConfiguration(file);
	}

	public FileConfiguration getData() {
		return cfg;
	}

	public void saveData() {
		try {
			cfg.save(file);

		} catch (IOException e) {
			plugin.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Could not save the Data.yml file");
		}
	}

	public void reloadData() {
		cfg = YamlConfiguration.loadConfiguration(file);
	}
	public static Yamls Data;
}
