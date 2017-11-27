package com.planethouki.plugin;

import java.util.Random;

import org.bukkit.Material;
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
		
		if (event.getBlock().getType() != Material.CROPS) {
			return;
		}
		
		// TODO Grown CROPS Only Harvest Chance
		
		
		int harverstRoulette = 0;
		harverstRoulette += event.getPlayer().getLocation().getX() % 0.1 * 100;
		harverstRoulette += event.getPlayer().getLocation().getZ() % 0.1 * 100;
		harverstRoulette %= 10;

		this.plugin.getLogger().info(Integer.toString(Math.abs(harverstRoulette)));
		
		if ( Math.abs((int)(harverstRoulette % 0.1 * 100)) < 9) {
			return;
		}
		
		// TODO transaction
		// TODO get player's wallet
		
		// TODO message
		event.getPlayer().sendMessage("Harvest!");
	}
}
