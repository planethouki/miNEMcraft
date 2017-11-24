package com.planethouki.plugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class HarvestListener implements Listener {
	
	private MinemcraftPlugin plugin;
	
	public HarvestListener(MinemcraftPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}
	
	@EventHandler
	public void	wheatBreak(BlockBreakEvent event) {
		this.plugin.getLogger().info(event.getPlayer().getName());
		this.plugin.getLogger().info(event.getBlock().getType().name());
	}
}
