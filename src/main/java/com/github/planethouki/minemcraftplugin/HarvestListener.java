package com.github.planethouki.minemcraftplugin;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.CropState;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.material.Crops;
import org.bukkit.material.MaterialData;

import io.nem.sdk.infrastructure.TransactionHttp;
import io.nem.sdk.model.account.Account;
import io.nem.sdk.model.account.Address;
import io.nem.sdk.model.mosaic.XEM;
import io.nem.sdk.model.transaction.Deadline;
import io.nem.sdk.model.transaction.PlainMessage;
import io.nem.sdk.model.transaction.SignedTransaction;
import io.nem.sdk.model.transaction.Transaction;
import io.nem.sdk.model.transaction.TransferTransaction;

public class HarvestListener implements Listener {

	private MinemcraftPlugin plugin;
	private Map<UUID, Integer> uuidToHarvest;

	public HarvestListener(MinemcraftPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
		this.uuidToHarvest = new HashMap<UUID, Integer>();
	}

	@EventHandler
	public void	harvest(BlockBreakEvent event) {
		int score = 0;
		// 1.12.2 => NETHER_WARTS, 1.13 => NETHER_WART
		// 1.12.2 => SUGAR_CANE_BLOCK, 1.13 => SUGAR_CANE
		// 1.13 => if (event.getBlock().getBlockData().getAsString().equalsIgnoreCase(7) {
		switch(event.getBlock().getType()) {
		case CROPS:
		case POTATO:
		case CARROT:
			// 0-7
			MaterialData materialData1 = event.getBlock().getState().getData();
			score = Byte.toUnsignedInt(materialData1.getData());
			break;
		case BEETROOT_SEEDS:
		case NETHER_WARTS:
			// 0-3
			MaterialData materialData2 = event.getBlock().getState().getData();
			score = Byte.toUnsignedInt(materialData2.getData());
			break;
		case COCOA:
			// 0-2
			MaterialData materialData3 = event.getBlock().getState().getData();
			score = Byte.toUnsignedInt(materialData3.getData()) / 4;
			break;
		case MELON:
		case PUMPKIN:
		case SUGAR_CANE_BLOCK:
			score = 1;
			break;
		default:
			return;
		}

		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		if (uuidToHarvest.containsKey(uuid)) {
			Integer amount = uuidToHarvest.get(uuid);
			if (amount.intValue() > 100) {
				uuidToHarvest.put(uuid, score);
				player.sendMessage("Harvest! You will get 1 XEM!");
				sendTransaction(player, 1);
			} else {
				uuidToHarvest.put(uuid, amount.intValue() + score);
			}
		} else {
			uuidToHarvest.put(uuid, score);
		}
	}

	private void sendTransaction(Player player, int amount) {

		String recipientAddress = plugin.getPlayerAddress(player);

		if (recipientAddress == null) {
			plugin.getLogger().warning("recipientAddress is null. HarvestListener cannot send tx.");
			return;
		}

        final TransferTransaction transferTransaction = TransferTransaction.create(
                Deadline.create(2, java.time.temporal.ChronoUnit.HOURS),
                Address.createFromRawAddress(recipientAddress),
                Collections.singletonList(XEM.createRelative(BigInteger.valueOf(amount))),
                PlainMessage.create("Harvest!"),
                plugin.getBlockchainNetworkType()
            );
        SignedTransaction signedTransaction = plugin.signByServer(transferTransaction);

        try {
	        final TransactionHttp transactionHttp = new TransactionHttp(plugin.getBlockchainHost());
	        transactionHttp.announce(signedTransaction).subscribe(x -> {

	        }, err -> {
	        	err.printStackTrace();
	        });

	        new TransactionListener(
	        		plugin,
	        		recipientAddress,
	        		signedTransaction.getHash(),
	        		player
	        		)
	        	.confirmed()
	        	.status();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

}
