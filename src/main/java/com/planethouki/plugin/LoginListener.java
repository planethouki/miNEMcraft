package com.planethouki.plugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class LoginListener implements Listener {
	
	private MinemcraftPlugin plugin;
	
	public LoginListener(MinemcraftPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		plugin.getLogger().info("LoginListener Set");
		this.plugin = plugin;
	}
	
	@EventHandler
	public void normalLogin(PlayerLoginEvent event) {
		this.plugin.getLogger().info(event.getPlayer().getName());
	}
}
