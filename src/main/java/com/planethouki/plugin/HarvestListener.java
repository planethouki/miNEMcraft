package com.planethouki.plugin;

import java.util.Collection;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class HarvestListener implements Listener {
	
	private MinemcraftPlugin plugin;
	
	public HarvestListener(MinemcraftPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}
	
	@EventHandler
	public void	wheatBreak(BlockBreakEvent event) {
//		plugin.getLogger().info(event.getPlayer().getName());
//		plugin.getLogger().info(event.getBlock().getType().name());
		
		if (event.getBlock().getType() != Material.CROPS) {
			return;
		}

		Boolean isGrown = false;
		Collection<ItemStack> drop = event.getBlock().getDrops();
		for (ItemStack stack: drop) {
			if (stack.getType() == Material.WHEAT) {
				isGrown = true;
			}
		}
		
		if (!isGrown) {
			return;
		}
		
		Random harvestRoulette = new Random();
		if (harvestRoulette.nextInt(9) != 0) {
			return;
		}

		
		// TODO transaction
		// TODO get player's wallet
		
		// TODO message
		event.getPlayer().sendMessage("Harvest!");
	}
}
