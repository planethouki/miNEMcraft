package com.planethouki.plugin;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class MinemcraftPlugin extends JavaPlugin {
	
	private PlayerWallet wallet;

	@Override
	public void onDisable() {
		
		// Commands
		
		// Listeners
		HandlerList.unregisterAll(this);

		// Configurations
		this.saveDefaultConfig();
		
		// Others
		getLogger().info("Plugin Disabled");
		
		super.onDisable();
	}

	@Override
	public void onEnable() {
		
		// Commands
		getCommand("nemc").setExecutor(new TestCommand());
		
		// Listeners
		new LoginListener(this);
		new HarvestListener(this);
		
		// Configurations
		this.getConfig();
		
		// Others
		wallet = new PlayerWallet(this);
		getLogger().info("Plugin Enabled");
		
		super.onEnable();
	}
	
	public PlayerWallet getPlayerWalletInstance() {
		return wallet;
	}

}
