package com.github.planethouki.minemcraftplugin;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.util.Collections;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

import io.nem.sdk.infrastructure.TransactionHttp;
import io.nem.sdk.model.account.Address;
import io.nem.sdk.model.mosaic.XEM;
import io.nem.sdk.model.transaction.Deadline;
import io.nem.sdk.model.transaction.PlainMessage;
import io.nem.sdk.model.transaction.SignedTransaction;
import io.nem.sdk.model.transaction.TransferTransaction;

public class AdvancementListener implements Listener {

	private MinemcraftPlugin plugin;

	public AdvancementListener(MinemcraftPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}

	@EventHandler
	public void advancement(PlayerAdvancementDoneEvent event) {


		String playerId = event.getPlayer().getUniqueId().toString();
		String playerName = event.getPlayer().getName();
		String advancementKey = event.getAdvancement().getKey().getKey();


        final TransferTransaction transferTransaction = TransferTransaction.create(
                Deadline.create(2, java.time.temporal.ChronoUnit.HOURS),
                Address.createFromRawAddress(plugin.getApostilleAddress()),
                Collections.singletonList(XEM.createRelative(BigInteger.valueOf(0))),
                PlainMessage.create(playerId + " " + advancementKey),
                plugin.getBlockchainNetworkType()
            );
        SignedTransaction signedTransaction = plugin.signByServer(transferTransaction);

		plugin.getLogger().info("Creating Apostille: " + playerName + "(" + playerId + ") " + advancementKey);
		plugin.getLogger().info("Hash: " + signedTransaction.getHash());

        TransactionHttp transactionHttp;
		try {
			transactionHttp = new TransactionHttp(plugin.getBlockchainHost());
	        transactionHttp.announce(signedTransaction).subscribe(x -> {

	        }, err -> {
	        	err.printStackTrace();
	        });
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

}
