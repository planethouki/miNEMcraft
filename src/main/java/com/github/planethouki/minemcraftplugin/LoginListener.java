package com.github.planethouki.minemcraftplugin;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import io.nem.core.utils.HexEncoder;
import io.nem.sdk.model.account.Account;
import io.nem.sdk.model.blockchain.NetworkType;

import io.reactivex.*;

public class LoginListener implements Listener {

	private MinemcraftPlugin plugin;

	public LoginListener(MinemcraftPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}

	@EventHandler
	public void login(PlayerLoginEvent event) {
		String playerUUID = event.getPlayer().getUniqueId().toString();
		String playerName = event.getPlayer().getDisplayName();
		if (plugin.getAddressConfig().contains(playerUUID)) {

		} else {
			SecureRandom random = new SecureRandom();
			byte bytes[] = new byte[32];
			random.nextBytes(bytes);
			String privateKey = HexEncoder.getString(bytes);
			Account account = Account.createFromPrivateKey(privateKey, NetworkType.MIJIN_TEST);
			Map<String, String> map = new HashMap<String, String>();
			map.put("name", playerName);
			map.put("private", account.getPrivateKey());
			map.put("public", account.getPublicKey());
			map.put("address", account.getAddress().plain());
			plugin.getAddressConfig().createSection(playerUUID, map);
			plugin.saveAddressConfig();
		}
	}

}
