package com.planethouki.plugin;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class MinemcraftPlugin extends JavaPlugin {

	@Override
	public void onDisable() {
		HandlerList.unregisterAll(this);
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
		
		// Other
		getLogger().info("Plugin Enabled");
		
		super.onEnable();
	}

}
