package com.github.planethouki.minemcraftplugin;


import java.math.BigInteger;
import java.net.MalformedURLException;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
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
			case "send":
				int sendValue = 10;
				if (args.length == 2) {

				} else if (args.length == 3) {
					sendValue = Integer.parseInt(args[2]);
				} else {
					sender.sendMessage("argment is wrong");
					return false;
				}
				UUID recipientUUID = getPlayerUUIDByName(args[1]);
				if (recipientUUID == null) {
					sender.sendMessage("player " + args[1] + "'s uuid was not found.");
					return true;
				}
				final String recipientAddress = getPlayerAddress(recipientUUID);
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
		        	signedTransaction = signByPlayer(transferTransaction, (Player)sender);
		        } else {
		        	signedTransaction = signByServer(transferTransaction);
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
//			        	System.out.println(x.getMessage());
			        }, err -> {
			        	err.printStackTrace();
			        });
//			        listener.open().get();
//	        		listener.confirmed(Address.createFromRawAddress(recipientAddress)).subscribe(tx -> {
//	        			sender.sendMessage(tx.toString());
//	        			listener.close();
//	        		}, err -> {
//	        			err.printStackTrace();
//	        		});
			        class TransactionPredicate<Transaction> implements Predicate<Transaction> {
			        	final String transactionHash;
			        	public TransactionPredicate(String transactionHash) {
			        		this.transactionHash = transactionHash;
			        	}
						@Override
						public boolean test(Transaction t) throws Exception {
							return transactionHash.equalsIgnoreCase(
									((io.nem.sdk.model.transaction.Transaction) t).getTransactionInfo().get().getHash().get());
						}
			        }
			        TransactionPredicate<Transaction> p = new TransactionPredicate<Transaction>(signedTransaction.getHash());
			        class TransactionObserver<Transaction> extends DisposableObserver<Transaction> {
			        	final CommandSender sender;
			        	public TransactionObserver(CommandSender sender) {
			        		this.sender = sender;
			        	}
						@Override public void onStart() {
//							System.out.println("Start!");
						}
						@Override public void onNext(Transaction tx) {
							sender.sendMessage("Transaction was confirmed.");
							sender.sendMessage("Hash: " +
									((io.nem.sdk.model.transaction.Transaction) tx).getTransactionInfo().get().getHash().get());
							if (sender instanceof Player) {
								((Player)sender).playSound(
										((Player) sender).getLocation(),
										Sound.ENTITY_PLAYER_LEVELUP,
										0.75F,
										0.5F);
							}
							this.dispose();
						}
						@Override public void onError(Throwable t) {
							t.printStackTrace();
						}
						@Override public void onComplete() {
//							System.out.println("Done!");
						}
			        }
			        TransactionObserver<Transaction> d = new TransactionObserver<Transaction>(sender);

	        		listener
	        			.confirmed(Address.createFromRawAddress(recipientAddress))
	        			.filter(p)
	        			.subscribeWith(d);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				break;
			case "balance":
				String rawAddress = null;
				if (args.length == 1) {
					if (sender instanceof Player) {
						rawAddress = getPlayerAddress((Player)sender);
					} else {
						rawAddress = getServerAddress();
					}
				} else if (args.length == 2) {
					if (args[1].equalsIgnoreCase("server")) {
						rawAddress = getServerAddress();
					} else {
						UUID uuid = getPlayerUUIDByName(args[1]);
						if (uuid == null) {
							sender.sendMessage("Cannnot find player. (uuid is null)");
							return true;
						}
						rawAddress = getPlayerAddress(uuid);
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
				random.nextBytes(bytes);
				String randomName = HexEncoder.getString(bytes);
				Account account = Account.createFromPrivateKey(privateKey, network);
				Map<String, String> map = new HashMap<String, String>();
				map.put("name", randomName.substring(0, 6));
				map.put("private", account.getPrivateKey());
				map.put("public", account.getPublicKey());
				map.put("address", account.getAddress().plain());
				plugin.getAddressConfig().set(randomName.substring(6, 20), map);
				plugin.saveAddressConfig();
				break;
			case "address":
				String rawAddress1 = null;
				if (args.length == 1) {
					if (sender instanceof Player) {
						rawAddress1 = getPlayerAddress((Player)sender);

					} else {
						rawAddress1 = getServerAddress();
					}
				} else if (args.length == 2) {
					if (args[1].equalsIgnoreCase("server")) {
						rawAddress1 = getServerAddress();
					} else {
						UUID uuid = getPlayerUUIDByName(args[1]);
						if (uuid == null) {
							sender.sendMessage("Cannnot find player. (uuid is null)");
							return true;
						}
						rawAddress1 = getPlayerAddress(uuid);
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

	private SignedTransaction signByPlayer(Transaction transaction, Player player) {
		String uuid = player.getUniqueId().toString();
		if (plugin.getAddressConfig().contains(uuid)) {
			Account a = Account.createFromPrivateKey(plugin.getAddressConfig().getString(uuid + ".private"), network);
			return a.sign(transaction);
		}
		return null;
	}

	private SignedTransaction signByServer(Transaction transaction) {
		Account a = Account.createFromPrivateKey(plugin.getConfig().getString("profile.privateKey"), network);
		return a.sign(transaction);
	}


	private String getPlayerAddress(Player player) {
		return getPlayerAddress(player.getUniqueId());
	}

	private String getPlayerAddress(UUID uuid) {
		if (plugin.getAddressConfig().contains(uuid.toString())) {
			return plugin.getAddressConfig().getString(uuid + ".address");
		}
		return null;
	}

	private String getServerAddress() {
		return Account
		.createFromPrivateKey(plugin.getConfig().getString("profile.privateKey"), network)
		.getAddress()
		.plain();
	}

	private UUID getPlayerUUIDByName(String name) {
		Player recepientPlayer = plugin.getServer().getPlayerExact(name);
		if (recepientPlayer != null) {
			return recepientPlayer.getUniqueId();
		} else {
			OfflinePlayer op = Bukkit.getOfflinePlayer(name);
			if (op.hasPlayedBefore()) {
			    return op.getUniqueId();
			}
		}
		return null;
	}

}
