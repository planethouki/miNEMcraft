package com.github.planethouki.minemcraftplugin;


import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.nem.core.utils.HexEncoder;
import io.nem.sdk.infrastructure.AccountHttp;
import io.nem.sdk.infrastructure.Listener;
import io.nem.sdk.model.account.Account;
import io.nem.sdk.model.account.Address;
import io.nem.sdk.model.blockchain.NetworkType;
import io.nem.sdk.model.mosaic.MosaicId;
import io.nem.sdk.model.mosaic.XEM;
import io.nem.sdk.model.transaction.UInt64;
import io.vertx.core.json.JsonArray;

public class MinemcraftCommand implements CommandExecutor {

	private MinemcraftPlugin plugin;
	private NetworkType network;
	private String host;
	private Listener listener;

	public MinemcraftCommand(MinemcraftPlugin plugin) {
		this.plugin = plugin;
		network = plugin.getNemNetworkType();
		host = plugin.getNemHost();
		listener = plugin.getNemListener();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (args.length > 0) {

			switch(args[0]) {
			case "debug":
				sender.sendMessage("============================================");
				sender.sendMessage(plugin.getConfig().getString("profile.network"));
				sender.sendMessage(plugin.getConfig().getString("profile.url"));
				sender.sendMessage(plugin.getConfig().getString("profile.privateKey"));
				sender.sendMessage("--------------------------------------------");
				plugin.getAddressConfig().getKeys(false).forEach(uuid -> {
					sender.sendMessage("name: " + plugin.getAddressConfig().getString(uuid + ".name"));
					sender.sendMessage("private: " + plugin.getAddressConfig().getString(uuid + ".private"));
					sender.sendMessage("public: " + plugin.getAddressConfig().getString(uuid + ".public"));
					sender.sendMessage("address: " + plugin.getAddressConfig().getString(uuid + ".address"));
				});
				sender.sendMessage("============================================");
				sender.sendMessage("network: " + network.toString());
				sender.sendMessage("host: " + host);
				sender.sendMessage("listener: " + listener.getUID());
				sender.sendMessage("============================================");
				break;
			case "generate":
				SecureRandom random = new SecureRandom();
				byte bytes[] = new byte[32];
				random.nextBytes(bytes);
				String privateKey = HexEncoder.getString(bytes);
				String randomName = UUID.randomUUID().toString();
				Account account = Account.createFromPrivateKey(privateKey, network);
				Map<String, String> map = new HashMap<String, String>();
				map.put("name", "random_generated");
				map.put("private", account.getPrivateKey());
				map.put("public", account.getPublicKey());
				map.put("address", account.getAddress().plain());
				plugin.getAddressConfig().set(randomName, map);
				plugin.saveAddressConfig();
				break;
			case "address":
				String rawAddress1 = null;
				if (args.length == 1) {
					if (sender instanceof Player) {
						rawAddress1 = plugin.getPlayerAddress((Player)sender);

					} else {
						rawAddress1 = plugin.getServerAddress();
					}
				} else if (args.length == 2) {
					if (args[1].equalsIgnoreCase("server")) {
						rawAddress1 = plugin.getServerAddress();
					} else {
						UUID uuid = plugin.getPlayerUUIDByName(args[1]);
						if (uuid == null) {
							sender.sendMessage("Cannnot find player. (uuid is null)");
							return true;
						}
						rawAddress1 = plugin.getPlayerAddress(uuid);
					}
				} else {
					return false;
				}
				if (rawAddress1 == null) {
					sender.sendMessage("Cannot find player. (rawAddress is null)");
					return true;
				}
				sender.sendMessage(rawAddress1);
				break;
			case "reload":
				plugin.reloadConfig();
				plugin.loadAddressConfig();
				break;
			}
		}
		return true;
	}

}
