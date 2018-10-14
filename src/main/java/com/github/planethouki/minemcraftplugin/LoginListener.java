package com.github.planethouki.minemcraftplugin;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import io.nem.core.utils.HexEncoder;
import io.nem.sdk.model.account.Account;
import io.nem.sdk.model.account.Address;
import io.nem.sdk.model.blockchain.NetworkType;
import io.nem.sdk.model.mosaic.Mosaic;
import io.nem.sdk.model.mosaic.MosaicId;
import io.nem.sdk.model.mosaic.XEM;
import io.nem.sdk.model.transaction.Deadline;
import io.nem.sdk.model.transaction.PlainMessage;
import io.nem.sdk.model.transaction.TransferTransaction;
import io.reactivex.*;

public class LoginListener implements Listener {

	private MinemcraftPlugin plugin;
	private MinemcraftHelper helper;

	public LoginListener(MinemcraftPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
		this.helper = plugin.getHelper();
	}

	@EventHandler
	public void login(PlayerLoginEvent event) {
		String playerUUID = event.getPlayer().getUniqueId().toString();
		String playerName = event.getPlayer().getDisplayName();
		byte[] playerSeed = playerUUID.replaceAll("-", "").getBytes();
		byte[] privKeySeed = io.nem.core.crypto.Hashes.sha3_256(playerSeed);
		Address recipient = Account.createFromPrivateKey(HexEncoder.getString(privKeySeed), helper.getNetwork()).getAddress();

		Observable.just(
			TransferTransaction.create(
				Deadline.create(2, java.time.temporal.ChronoUnit.HOURS),
				recipient,
				Collections.singletonList(helper.getLoginMosaic(1)),
				PlainMessage.create(playerName),
				helper.getNetwork()
			)
		).map(
			tx -> helper.signByServer(tx)
		).subscribe(
			signedTx -> {
				plugin.getHelper().announce(signedTx).subscribe();
				plugin.getLogger().info(signedTx.getHash());
			}
		);


	}

}
