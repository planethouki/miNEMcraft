package com.github.planethouki.plugin;

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

public class MineListener implements Listener {
	
	private MinemcraftPlugin plugin;
	
	public MineListener(MinemcraftPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}
	
	@EventHandler
	public void	mineBreak(BlockBreakEvent event) {
		
		//TODO IRON
		//TODO GOLD
		//TODO LAPIS LAZULI
		//TODO REDSTONE
		//TODO EMERALD
		//TODO DIAMOND
		
		if (event.getBlock().getType() != Material.COAL_ORE) {
			return;
		}

		int coalAmount = 0;
		Collection<ItemStack> drop = event.getBlock().getDrops();
		for (ItemStack stack: drop) {
			if (stack.getType() == Material.COAL) {
				coalAmount = stack.getAmount();
			}
		}
		
		Random miningRoulette = new Random();
		if (miningRoulette.nextInt(10) >= coalAmount) {
			return;
		}

		
		Player player = event.getPlayer();
		String palyerAddress = plugin.getAddressConfig().getString(player.getName());
		if (palyerAddress == null) {
			player.sendMessage("your address is not registered yet");
			return;
		}
		
		String transactionURL = "http://localhost:7890/transaction/prepare-announce";
		String transactionData = "";
		Calendar nemEpoch = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		nemEpoch.set(2015, 2, 29, 0, 6, 25);
		Calendar now = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		String timeStamp = Long.toString((now.getTimeInMillis() - nemEpoch.getTimeInMillis())/1000);
		String amount = "50000";
		String fee = "50000";
		String recipient = palyerAddress.replaceAll("-", "");
		String deadline = Long.toString(((now.getTimeInMillis() - nemEpoch.getTimeInMillis())/1000)+3600);
		String signer = plugin.getConfig().getString("ServerPublicKey");
		String privateKey = plugin.getConfig().getString("ServerPrivateKey");
		transactionData = String.format("{\"transaction\":{\"timeStamp\":%s,\"amount\":%s,\"fee\":%s,\"recipient\":\"%s\",\"type\":257,\"deadline\":%s,\"message\":{\"payload\":\"\",\"type\":1},\"version\":-1744830463,\"signer\":\"%s\"},\"privateKey\":\"%s\"}",timeStamp, amount, fee, recipient, deadline, signer, privateKey);
		plugin.getLogger().info(player.getName() + " has harvested");
		plugin.getLogger().info(transactionURL);
		plugin.getLogger().info(transactionData);
		plugin.getLogger().info(MyHttpClient.executePost(transactionURL, transactionData));
		
		event.getPlayer().sendMessage("You Got Mining! " + (Double.parseDouble(amount)/1000) + " XEM");
	}
}
