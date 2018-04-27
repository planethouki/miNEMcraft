package com.github.planethouki.plugin;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import io.nem.apps.builders.ConfigurationBuilder;

public class MinemcraftPlugin extends JavaPlugin {

	private FileConfiguration addressConfig;

	public MinemcraftPlugin() {
		super();
		saveDefaultConfig();
		saveResource("address.yml", false);
	}

	@Override
	public void onDisable() {

		// Commands

		// Listeners
		HandlerList.unregisterAll(this);

		// Configurations
		saveConfig();
		saveAddressConfig();

		// Others
		getLogger().info("Plugin Disabled");

		super.onDisable();
	}

	@Override
	public void onEnable() {

		// Commands
		getCommand("mncx").setExecutor(new TestCommand(this));
		getCommand("mnc").setExecutor(new MinemcraftCommand(this));

		// Listeners
		new HarvestListener(this);
		new MineListener(this);

		// Configurations
		addressConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "address.yml"));

		// nem-apps-lib
		ConfigurationBuilder.nodeNetworkName("defaultTestnet")
		    .nodeNetworkProtocol("http")
		    .nodeNetworkUri("bigalice2.nem.ninja")
		    .nodeNetworkPort("7890")
		    .setup();

		// Others
		getLogger().info("Plugin Enabled");

		super.onEnable();
	}


	public FileConfiguration getAddressConfig() {
		return this.addressConfig;
	}

	public void saveAddressConfig() {
		try {
			addressConfig.save("address.yml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
