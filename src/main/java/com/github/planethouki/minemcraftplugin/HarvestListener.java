package com.github.planethouki.minemcraftplugin;

import java.util.Calendar;
import java.util.Collection;
import java.util.Random;
import java.util.TimeZone;

import org.bukkit.Material;
import org.bukkit.entity.Player;
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

		// TODO POTATO
		// TODO CARROT
		// TODO BEETROOT
		// TODO WATERMELON
		// TODO PUMPKIN
		// TODO COCOA
		// TODO SUGAR CANE
		// TODO NETHER WART
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

		double mosaicAmount = 0.01;

		//plugin.getCrypto().harvest(event.getPlayer(), mosaicAmount);

		event.getPlayer().sendMessage("You Got Harvest! " + Double.toString(mosaicAmount));
	}
}
