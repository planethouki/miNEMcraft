package com.github.planethouki.minemcraftplugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;




public class MinemcraftPlugin extends JavaPlugin {

	private FileConfiguration addressConfig;
	private File addressFile;

	private String nemNetworkType;
	private String nemHost;

	private MinemcraftHelper helper;

	public MinemcraftPlugin() {
		super();
		saveDefaultConfig();
		saveResource("address.yml", false);
		addressFile = new File(getDataFolder(), "address.yml");
	}

	@Override
	public void onDisable() {

		// Commands

		// Listeners
		HandlerList.unregisterAll(this);

		// Configurations
//		saveConfig();
//		saveAddressConfig();

		// Others
		helper.listenerClose();
		getLogger().info("Plugin Disabled");

		super.onDisable();
	}

	@Override
	public void onEnable() {
		// Configurations
		addressConfig = loadAddressConfig();

		// properties
		nemNetworkType = getConfig().getString("profile.network");
		nemHost = getConfig().getString("profile.host");

		// Helpers
		this.helper = new MinemcraftHelper(this);
		this.helper.listenerOpen();

		// Commands
		getCommand("mnc").setExecutor(new MinemcraftCommand(this));

		// Listeners
		new LoginListener(this);


		getLogger().info("Server Address: " + helper.getServerAddress());
		getLogger().info("Apostille Address: " + helper.getApostilleAddress());
		getLogger().info("Network Type: " + nemNetworkType);
		getLogger().info("Node: " + nemHost);

		getLogger().info("Plugin Enabled");

		super.onEnable();

	}


	// address config
	void saveAddressConfig() {
		try {
			addressConfig.save(addressFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	FileConfiguration loadAddressConfig() {
		return YamlConfiguration.loadConfiguration(addressFile);
	}

	// properties setter getter
	FileConfiguration getAddressConfig() {
		return addressConfig;
	}

	UUID getPlayerUUIDByName(String name) {
		Player recepientPlayer = getServer().getPlayerExact(name);
		if (recepientPlayer != null) {
			return recepientPlayer.getUniqueId();
		} else {
			OfflinePlayer op = getServer().getOfflinePlayer(name);
			if (op.hasPlayedBefore()) {
			    return op.getUniqueId();
			}
		}
		return null;
	}

	MinemcraftHelper getHelper() {
		return helper;
	}
}
