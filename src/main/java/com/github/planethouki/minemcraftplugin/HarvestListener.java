package com.github.planethouki.minemcraftplugin;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.CropState;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.material.Crops;

import io.nem.sdk.infrastructure.TransactionHttp;
import io.nem.sdk.model.account.Account;
import io.nem.sdk.model.account.Address;
import io.nem.sdk.model.mosaic.XEM;
import io.nem.sdk.model.transaction.Deadline;
import io.nem.sdk.model.transaction.PlainMessage;
import io.nem.sdk.model.transaction.SignedTransaction;
import io.nem.sdk.model.transaction.Transaction;
import io.nem.sdk.model.transaction.TransferTransaction;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;

public class HarvestListener implements Listener {

	private MinemcraftPlugin plugin;
	private Map<UUID, Double> uuidToHarvest;

	public HarvestListener(MinemcraftPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
		this.uuidToHarvest = new HashMap<UUID, Double>();
	}

	@EventHandler
	public void	wheatBreak(BlockBreakEvent event) {

		// TODO POTATO
		// TODO CARROT
		// TODO BEETROOT
		// TODO WATERMELON
		// TODO PUMPKIN
		// TODO COCOA
		// TODO SUGAR CANE
		// TODO NETHER WART
		if (event.getBlock().getType() != Material.CROPS) {
			return;
		}

		Crops crops = (Crops)event.getBlock().getState().getData();
		UUID uuid = event.getPlayer().getUniqueId();
		if (crops.getState() == CropState.RIPE) {
			if (uuidToHarvest.containsKey(uuid)) {
				Double amount = uuidToHarvest.get(uuid);
				if (amount.doubleValue() > 1F) {
					uuidToHarvest.put(uuid, new Double(0.1));
					event.getPlayer().sendMessage("Harvest! You will get 10XEM!");
					sendTransaction(event.getPlayer());
				} else {
					uuidToHarvest.put(uuid, new Double(amount.doubleValue() + 0.1));
				}
			} else {
				uuidToHarvest.put(uuid, 0.1);
			}
		}

	}

	private void sendTransaction(Player player) {
		UUID uuid = player.getUniqueId();
		String recipientAddress = getPlayerAddress(uuid);
		if (recipientAddress == null) {
			plugin.getLogger().warning("recipientAddress is null. HarvestListener cannot send tx.");
			plugin.getLogger().warning("uuid: " + uuid.toString());
			return;
		}
        final TransferTransaction transferTransaction = TransferTransaction.create(
                Deadline.create(2, java.time.temporal.ChronoUnit.HOURS),
                Address.createFromRawAddress(recipientAddress),
                Collections.singletonList(XEM.createRelative(BigInteger.valueOf(10))),
                PlainMessage.create("Harvest!"),
                plugin.getBlockchainNetworkType()
            );
        SignedTransaction signedTransaction = signByServer(transferTransaction);
        try {
	        final TransactionHttp transactionHttp = new TransactionHttp(plugin.getBlockchainHost());
	        transactionHttp.announce(signedTransaction).subscribe(x -> {
	        }, err -> {
	        	err.printStackTrace();
	        });
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
	        	final Logger logger;
	        	public TransactionObserver(CommandSender sender, Logger logger) {
	        		this.sender = sender;
	        		this.logger = logger;
	        	}
				@Override public void onStart() {
//					System.out.println("Start!");
				}
				@Override public void onNext(Transaction tx) {
					String hash = ((io.nem.sdk.model.transaction.Transaction) tx).getTransactionInfo().get().getHash().get();
					sender.sendMessage("Harvest Complete!");
					sender.sendMessage("Hash: " + hash);
					logger.info("Harvest Transaction Confirmed");
					logger.info("Hash: " + hash);
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
//					System.out.println("Done!");
				}
	        }
	        TransactionObserver<Transaction> d = new TransactionObserver<Transaction>(player, plugin.getLogger());

    		plugin.getBlockchainListener()
    			.confirmed(Address.createFromRawAddress(recipientAddress))
    			.filter(p)
    			.subscribeWith(d);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	private String getPlayerAddress(UUID uuid) {
		if (plugin.getAddressConfig().contains(uuid.toString())) {
			return plugin.getAddressConfig().getString(uuid + ".address");
		}
		return null;
	}

	private SignedTransaction signByServer(Transaction transaction) {
		Account a = Account.createFromPrivateKey(plugin.getConfig().getString("profile.privateKey"), plugin.getBlockchainNetworkType());
		return a.sign(transaction);
	}
}
