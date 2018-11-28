package com.github.planethouki.minemcraftplugin;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.bukkit.entity.Player;

import io.nem.core.utils.HexEncoder;
import io.nem.sdk.infrastructure.AccountHttp;
import io.nem.sdk.infrastructure.Listener;
import io.nem.sdk.infrastructure.MosaicHttp;
import io.nem.sdk.infrastructure.TransactionHttp;
import io.nem.sdk.model.account.Account;
import io.nem.sdk.model.account.Address;
import io.nem.sdk.model.blockchain.NetworkType;
import io.nem.sdk.model.mosaic.Mosaic;
import io.nem.sdk.model.mosaic.MosaicId;
import io.nem.sdk.model.transaction.SignedTransaction;
import io.nem.sdk.model.transaction.Transaction;
import io.nem.sdk.model.transaction.TransactionAnnounceResponse;
import io.reactivex.Observable;

public class MinemcraftHelper {

	private MinemcraftPlugin plugin;
	private NetworkType network;
	private String host;
	private Listener listener;

	private TransactionHttp txHttp;
	private AccountHttp acHttp;
	private MosaicHttp moHttp;

	private Account srvAccount;

	MinemcraftHelper(MinemcraftPlugin plugin) {
		this.plugin = plugin;
		this.network = string2NetworkType(plugin.getConfig().getString("profile.network"));
		this.host = plugin.getConfig().getString("profile.host");
		try {
			this.listener = new Listener(this.host);
			this.txHttp = new TransactionHttp(this.host);
			this.acHttp = new AccountHttp(this.host);
			this.moHttp = new MosaicHttp(this.host);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		srvAccount = Account.createFromPrivateKey(plugin.getConfig().getString("profile.privateKey"), network);
	}

	NetworkType string2NetworkType(String network) {
		if (network.equalsIgnoreCase("MIJIN_TEST")) {
			return NetworkType.MIJIN_TEST;
		} else if (network.equalsIgnoreCase("MIJIN")) {
			return NetworkType.MIJIN;
		} else if (network.equalsIgnoreCase("TEST_NET")) {
			return NetworkType.TEST_NET;
		} else if (network.equalsIgnoreCase("MAIN_NET")) {
			return NetworkType.MAIN_NET;
		}
		throw new IllegalArgumentException("Unexpected Network. use MIJIN_TEST, MIJIN, TEST_NET, MAIN_NET");
	}

	public static String formatXEMrelative(String amountInt) {
		if (amountInt.length() > 6) {
			return
					amountInt.substring(0, amountInt.length() - 6) +
					"." +
					amountInt.substring(amountInt.length() - 6);
		} else {
			return
					"0." +
					"000000".substring(amountInt.length()) +
					amountInt
					;
		}
	}

	CompletableFuture<Void> listenerOpen() {
		return listener.open();
	}

	void listenerClose() {
		listener.close();
	}

	String getServerAddress() {
		return Account
		.createFromPrivateKey(plugin.getConfig().getString("profile.privateKey"), network)
		.getAddress()
		.plain();
	}

	String getApostilleAddress() {
		return plugin.getConfig().getString("profile.apostilleaddress");
	}

	String getPlayerAddress(Player player) {
		return getPlayerAddress(player.getUniqueId());
	}

	String getPlayerAddress(UUID uuid) {
		return plugin.getAddressConfig().getString(uuid + ".address");
	}

	SignedTransaction signByServer(Transaction transaction) {
		return srvAccount.sign(transaction);
	}

	SignedTransaction signByPlayer(Transaction transaction, Player player) {
		String uuid = player.getUniqueId().toString();
		if (plugin.getAddressConfig().contains(uuid)) {
			Account a = Account.createFromPrivateKey(plugin.getAddressConfig().getString(uuid + ".private"), network);
			return a.sign(transaction);
		}
		return null;
	}

	public Observable<TransactionAnnounceResponse> announce(SignedTransaction signedTx) {
		return this.txHttp.announce(signedTx);
	}

	NetworkType getNetwork() {
		return network;
	}

	Mosaic getLoginMosaic(int amount) {
		String name = plugin.getConfig().getString("mosaics.login");
		return new Mosaic(new MosaicId(name), BigInteger.valueOf(amount));
	}

	Mosaic getLogoutMosaic(int amount) {
		String name = plugin.getConfig().getString("mosaics.logout");
		return new Mosaic(new MosaicId(name), BigInteger.valueOf(amount));
	}

	public Address getPlayerAddress(String uuid) {
		byte[] playerSeed = uuid.replaceAll("-", "").getBytes();
		byte[] privKeySeed = io.nem.core.crypto.Hashes.sha3_256(playerSeed);
		return Account.createFromPrivateKey(HexEncoder.getString(privKeySeed), this.getNetwork()).getAddress();
	}
}
