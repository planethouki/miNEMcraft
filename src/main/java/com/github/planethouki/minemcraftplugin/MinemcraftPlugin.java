package com.github.planethouki.minemcraftplugin;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import io.nem.sdk.infrastructure.Listener;
import io.nem.sdk.model.blockchain.NetworkType;




public class MinemcraftPlugin extends JavaPlugin {

	private FileConfiguration addressConfig;
	private File addressFile;
	private Listener blockchainListener;
	private NetworkType blockchainNetworkType;
	private String blockchainHost;

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
		blockchainListener.close();
		getLogger().info("Plugin Disabled");

		super.onDisable();
	}

	@Override
	public void onEnable() {
		// Configurations
		addressConfig = loadAddressConfig();

		// properties
		blockchainNetworkType = MinemcraftHelper.getNetwork(getConfig().getString("profile.network"));
		blockchainHost = getConfig().getString("profile.url");
		try {
			blockchainListener = new Listener(blockchainHost);
			blockchainListener.open();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		// Commands
		getCommand("mnc").setExecutor(new MinemcraftCommand(this));

		// Listeners
		new HarvestListener(this);
		new MineListener(this);
		new LoginListener(this);


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

	Listener getBlockchainListener() {
		return blockchainListener;
	}

	NetworkType getBlockchainNetworkType() {
		return blockchainNetworkType;
	}

	String getBlockchainHost() {
		return blockchainHost;
	}



}
