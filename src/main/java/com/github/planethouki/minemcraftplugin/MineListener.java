package com.github.planethouki.minemcraftplugin;

import java.util.Random;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class MineListener implements Listener {

	private MinemcraftPlugin plugin;

	public MineListener(MinemcraftPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}

	@EventHandler
	public void	mineBreak(BlockBreakEvent event) {

		switch (event.getBlock().getType()) {
			case COAL_ORE:
			case IRON_ORE:
			case GOLD_ORE:
			case LAPIS_ORE:
			case REDSTONE_ORE:
			case EMERALD_ORE:
			case DIAMOND_ORE:
				break;
			default:
				return;
		}


		Random miningRoulette = new Random();
		if (miningRoulette.nextInt(10) >= 8) {
			return;
		}

		double mosaicAmount = 0.01;

//		plugin.getCrypto().mining(event.getPlayer(), mosaicAmount);
		event.getPlayer().sendMessage("You Got Mining! " + Double.toString(mosaicAmount));
	}
}
