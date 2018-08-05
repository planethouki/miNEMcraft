package com.github.planethouki.minemcraftplugin;


import java.math.BigInteger;
import java.net.MalformedURLException;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.advancement.Advancement;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.github.planethouki.minemcraftplugin.MinemcraftPlugin;

import io.nem.core.utils.HexEncoder;
import io.nem.sdk.infrastructure.AccountHttp;
import io.nem.sdk.infrastructure.Listener;
import io.nem.sdk.infrastructure.TransactionHttp;
import io.nem.sdk.model.account.Account;
import io.nem.sdk.model.account.Address;
import io.nem.sdk.model.blockchain.NetworkType;
import io.nem.sdk.model.mosaic.MosaicId;
import io.nem.sdk.model.mosaic.XEM;
import io.nem.sdk.model.transaction.Deadline;
import io.nem.sdk.model.transaction.PlainMessage;
import io.nem.sdk.model.transaction.SignedTransaction;
import io.nem.sdk.model.transaction.Transaction;
import io.nem.sdk.model.transaction.TransferTransaction;
import io.nem.sdk.model.transaction.UInt64;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.vertx.core.json.JsonArray;

public class MinemcraftCommand implements CommandExecutor {

	private MinemcraftPlugin plugin;
	private NetworkType network;
	private String host;
	private Listener listener;

	public MinemcraftCommand(MinemcraftPlugin plugin) {
		this.plugin = plugin;
		network = plugin.getBlockchainNetworkType();
		host = plugin.getBlockchainHost();
		listener = plugin.getBlockchainListener();
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
			case "advance":
				Iterator<Advancement> advancementIterator = plugin.getServer().advancementIterator();
				while (advancementIterator.hasNext()) {
					Advancement advancement = advancementIterator.next();
					sender.sendMessage(advancement.getKey().getKey());
				}
				break;
			case "send":
				int sendValue = 10;
				if (args.length == 2) {

				} else if (args.length == 3) {
					sendValue = Integer.parseInt(args[2]);
				} else {
					sender.sendMessage("argment is wrong");
					return false;
				}
				UUID recipientUUID = plugin.getPlayerUUIDByName(args[1]);
				if (recipientUUID == null) {
					sender.sendMessage("player " + args[1] + "'s uuid was not found.");
					return true;
				}
				final String recipientAddress = plugin.getPlayerAddress(recipientUUID);
				if (recipientAddress == null) {
					sender.sendMessage("player " + args[1] + "'s address was not found.");
					return true;
				}
		        final TransferTransaction transferTransaction = TransferTransaction.create(
		                Deadline.create(2, java.time.temporal.ChronoUnit.HOURS),
		                Address.createFromRawAddress(recipientAddress),
		                Collections.singletonList(XEM.createRelative(BigInteger.valueOf(sendValue))),
		                PlainMessage.create("Send " + sendValue + " to " + args[1]),
		                network
		            );
		        SignedTransaction signedTransaction = null;
		        if (sender instanceof Player) {
		        	signedTransaction = plugin.signByPlayer(transferTransaction, (Player)sender);
		        } else {
		        	signedTransaction = plugin.signByServer(transferTransaction);
		        }
		        if (signedTransaction == null) {
					sender.sendMessage("Couldn't sign transaction. Something wrong.");
					return true;
		        }
		        plugin.getServer().getLogger().info("TransferTx Sending: " + signedTransaction.getHash());
	        	sender.sendMessage("Sending Transaction...");
				try {
			        final TransactionHttp transactionHttp = new TransactionHttp(host);
			        transactionHttp.announce(signedTransaction).subscribe(x -> {
			        	System.out.println(x.getMessage());
			        }, err -> {
			        	err.printStackTrace();
			        });
			        new TransactionListener(
			        		plugin,
			        		recipientAddress,
			        		signedTransaction.getHash(),
			        		sender)
			        	.confirmed()
			        	.status();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				break;
			case "balance":
				String rawAddress = null;
				if (args.length == 1) {
					if (sender instanceof Player) {
						rawAddress = plugin.getPlayerAddress((Player)sender);
					} else {
						rawAddress = plugin.getServerAddress();
					}
				} else if (args.length == 2) {
					if (args[1].equalsIgnoreCase("server")) {
						rawAddress = plugin.getServerAddress();
					} else {
						UUID uuid = plugin.getPlayerUUIDByName(args[1]);
						if (uuid == null) {
							sender.sendMessage("Cannnot find player. (uuid is null)");
							return true;
						}
						rawAddress = plugin.getPlayerAddress(uuid);
					}
				} else {
					return false;
				}
				if (rawAddress == null) {
					sender.sendMessage("Cannot find player. (rawAddress is null)");
					return true;
				}
				Address address = Address.createFromRawAddress(rawAddress);
				try {
					final AccountHttp accountHttp = new AccountHttp(host);
					accountHttp.getAccountInfoJson(address)
						.subscribe(x -> {
							JsonArray c = x.getJsonObject("account").getJsonArray("mosaics");
							for (int i = 0; i < c.size(); i++) {
//								System.out.println(c.getJsonObject(i).toString());
								JsonArray mosaicIdJsonArray = c.getJsonObject(i).getJsonArray("id");
								int mosaicIdIntArray[] = new int[2];
								mosaicIdIntArray[0] = mosaicIdJsonArray.getInteger(0);
								mosaicIdIntArray[1] = mosaicIdJsonArray.getInteger(1);
								if (XEM.MOSAICID.equals(new MosaicId(UInt64.fromIntArray(mosaicIdIntArray)))) {
									JsonArray amountJsonArray = c.getJsonObject(i).getJsonArray("amount");
									int amountIntArray[] = new int[2];
									amountIntArray[0] = amountJsonArray.getInteger(0);
									amountIntArray[1] = amountJsonArray.getInteger(1);
//									System.out.println(MinemcraftHelper.formatXEMrelative(UInt64.fromIntArray(amountIntArray).toString()));
									sender.sendMessage(MinemcraftHelper.formatXEMrelative(UInt64.fromIntArray(amountIntArray).toString()));
								};
							}
						}, err -> {
							err.printStackTrace();
						});
				} catch (Exception e) {
					e.printStackTrace();
					e.getMessage();
				}
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
