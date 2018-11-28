package com.github.planethouki.minemcraftplugin;

import java.util.Collections;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import io.nem.sdk.model.account.Address;
import io.nem.sdk.model.transaction.Deadline;
import io.nem.sdk.model.transaction.PlainMessage;
import io.nem.sdk.model.transaction.TransferTransaction;
import io.reactivex.*;

public class LoginoutListener implements Listener {

	private MinemcraftPlugin plugin;
	private MinemcraftHelper helper;

	public LoginoutListener(MinemcraftPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
		this.helper = plugin.getHelper();
	}

	@EventHandler
	public void login(PlayerLoginEvent event) {
		String playerUUID = event.getPlayer().getUniqueId().toString();
		String playerName = event.getPlayer().getDisplayName();
		Address recipient = helper.getPlayerAddress(playerUUID);

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

	@EventHandler
	public void logout(PlayerQuitEvent event) {
		String playerUUID = event.getPlayer().getUniqueId().toString();
		String playerName = event.getPlayer().getDisplayName();
		Address recipient = helper.getPlayerAddress(playerUUID);

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
