package com.planethouki.plugin;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class MinemcraftPlugin extends JavaPlugin {
	
	private PlayerWallet wallet;
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
		try {
			addressConfig.save("address.yml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
		new LoginListener(this);
		new HarvestListener(this);
		
		// Configurations
		addressConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "address.yml"));
		
		
		// Others
		wallet = new PlayerWallet(this);
		getLogger().info("Plugin Enabled");
		
		super.onEnable();
	}
	
	public PlayerWallet getPlayerWalletInstance() {
		return wallet;
	}
	
	public FileConfiguration getAddressConfig() {
		return this.addressConfig;
	}

}
