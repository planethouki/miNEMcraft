package com.github.planethouki.minemcraftplugin.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import com.github.planethouki.minemcraftplugin.MinemcraftPlugin;

public class LoginListener implements Listener {

	private MinemcraftPlugin plugin;

	public LoginListener(MinemcraftPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}

	@EventHandler
	public void login(PlayerLoginEvent event) {
//		boolean hasAddress = plugin.getCrypto().hasAddress(event.getPlayer());
//		if ( !hasAddress ) {
//			String address = plugin.getCrypto().generateAddress(event.getPlayer());
//			plugin.getNotification().sayGeneratedAddress(event.getPlayer(), address);
//			plugin.getLogger().info(event.getPlayer().getDisplayName() + "'s address generated: " + address);
//		}
	}

}
