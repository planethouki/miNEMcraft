package com.github.planethouki.minemcraftplugin;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import io.nem.sdk.infrastructure.TransactionHttp;
import io.nem.sdk.model.account.Address;
import io.nem.sdk.model.mosaic.XEM;
import io.nem.sdk.model.transaction.Deadline;
import io.nem.sdk.model.transaction.PlainMessage;
import io.nem.sdk.model.transaction.SignedTransaction;
import io.nem.sdk.model.transaction.TransferTransaction;

public class MineListener implements Listener {

	private MinemcraftPlugin plugin;
	private Map<UUID, Integer> uuidToMining;

	public MineListener(MinemcraftPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
		this.uuidToMining = new HashMap<UUID, Integer>();
	}

	@EventHandler
	public void	mineBreak(BlockBreakEvent event) {
		int score = 0;
		switch (event.getBlock().getType()) {
			case COAL_ORE:
				score = 1;
				break;
			case IRON_ORE:
				score = 2;
				break;
			case REDSTONE_ORE:
				score = 2;
				break;
			case LAPIS_ORE:
				score = 2;
				break;
			case GOLD_ORE:
				score = 4;
				break;
			case DIAMOND_ORE:
				score = 8;
				break;
			case EMERALD_ORE:
				score = 10;
				break;
			default:
				return;
		}



		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		if (uuidToMining.containsKey(uuid)) {
			Integer amount = uuidToMining.get(uuid);
			if (amount.intValue() > 100) {
				uuidToMining.put(uuid, score);
				player.sendMessage("Mining Success! You will get 1 XEM!");
				sendTransaction(player, 1);
			} else {
				uuidToMining.put(uuid, amount.intValue() + score);
			}
		} else {
			uuidToMining.put(uuid, score);
		}
	}


	private void sendTransaction(Player player, int amount) {

		String recipientAddress = plugin.getPlayerAddress(player);

		if (recipientAddress == null) {
			plugin.getLogger().warning("recipientAddress is null. MineListener cannot send tx.");
			return;
		}

        final TransferTransaction transferTransaction = TransferTransaction.create(
                Deadline.create(2, java.time.temporal.ChronoUnit.HOURS),
                Address.createFromRawAddress(recipientAddress),
                Collections.singletonList(XEM.createRelative(BigInteger.valueOf(amount))),
                PlainMessage.create("Mining!"),
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
