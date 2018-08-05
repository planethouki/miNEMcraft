package com.github.planethouki.minemcraftplugin;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.nem.sdk.infrastructure.Listener;
import io.nem.sdk.model.account.Address;
import io.nem.sdk.model.transaction.Transaction;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;

public class TransactionListener {

	private MinemcraftPlugin plugin;
	private Listener listener;
	private String targetHash;
	private CommandSender sender;
	private String listeningAddress;


	public TransactionListener(MinemcraftPlugin plugin, String listeningAddress, String targetHash, CommandSender toMessage) {
		this.plugin = plugin;
		this.listener = plugin.getBlockchainListener();
		this.targetHash = targetHash;
		this.sender = toMessage;
		this.listeningAddress = listeningAddress;
	}

	public TransactionListener confirmed() {
		TransactionPredicate<Transaction> p = new TransactionPredicate<Transaction>(targetHash);
		TransactionObserver<Transaction> d = new TransactionObserver<Transaction>(sender, plugin.getLogger());

		listener
			.confirmed(Address.createFromRawAddress(listeningAddress))
			.filter(p)
			.take(60, TimeUnit.SECONDS)
			.subscribeWith(d);

		return this;
	}

	public TransactionListener status() {
		TransactionPredicate<io.nem.sdk.model.transaction.TransactionStatusError> p =
				new TransactionPredicate<io.nem.sdk.model.transaction.TransactionStatusError>(targetHash);
		TransactionObserver<io.nem.sdk.model.transaction.TransactionStatusError> d =
				new TransactionObserver<io.nem.sdk.model.transaction.TransactionStatusError>(sender, plugin.getLogger());

		listener
			.status(Address.createFromRawAddress(listeningAddress))
			.filter(p)
			.take(60, TimeUnit.SECONDS)
			.subscribeWith(d);

		return this;
	}


    class TransactionPredicate<T> implements Predicate<T> {

    	final String transactionHash;

    	public TransactionPredicate(String transactionHash) {
    		this.transactionHash = transactionHash;
    	}

		@Override
		public boolean test(T t) throws Exception {
			if (t instanceof Transaction) {
				return transactionHash.equalsIgnoreCase(
						((io.nem.sdk.model.transaction.Transaction) t).getTransactionInfo().get().getHash().get());
			} else if (t instanceof io.nem.sdk.model.transaction.TransactionStatusError) {
				return transactionHash.equalsIgnoreCase(
						((io.nem.sdk.model.transaction.TransactionStatusError) t).getHash());
			}
			return false;
		}
    }

    class TransactionObserver<T> extends DisposableObserver<T> {

    	final CommandSender sender;
    	final Logger logger;

    	public TransactionObserver(CommandSender sender, Logger logger) {
    		this.sender = sender;
    		this.logger = logger;
    	}

		@Override public void onStart() {

		}

		@Override public void onNext(T t) {
			String hash = null;
			if (t instanceof Transaction) {
				hash = ((Transaction) t).getTransactionInfo().get().getHash().get();
			} else if (t instanceof io.nem.sdk.model.transaction.TransactionStatusError) {
				hash = ((io.nem.sdk.model.transaction.TransactionStatusError) t).getHash();
			} else {

			}
			sender.sendMessage("Transaction was confirmed.");
			sender.sendMessage("Hash: " + hash);
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

		}
    }
}
