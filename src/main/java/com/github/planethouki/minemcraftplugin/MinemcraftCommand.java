package com.github.planethouki.minemcraftplugin;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.util.Collections;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.github.planethouki.minemcraftplugin.MinemcraftPlugin;

import io.nem.sdk.infrastructure.AccountHttp;
import io.nem.sdk.infrastructure.TransactionHttp;
import io.nem.sdk.model.account.Account;
import io.nem.sdk.model.account.Address;
import io.nem.sdk.model.blockchain.NetworkType;
import io.nem.sdk.model.mosaic.XEM;
import io.nem.sdk.model.transaction.Deadline;
import io.nem.sdk.model.transaction.PlainMessage;
import io.nem.sdk.model.transaction.SignedTransaction;
import io.nem.sdk.model.transaction.TransferTransaction;

public class MinemcraftCommand implements CommandExecutor {

	private MinemcraftPlugin plugin;

	public MinemcraftCommand(MinemcraftPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		sender.sendMessage("============================================");
		sender.sendMessage(plugin.getConfig().getString("profile.network"));
		sender.sendMessage(plugin.getConfig().getString("profile.url"));
		sender.sendMessage(plugin.getConfig().getString("profile.privateKey"));
		sender.sendMessage("--------------------------------------------");
		sender.sendMessage(plugin.getAddressConfig().getString("planet_houki.private"));
		sender.sendMessage(plugin.getAddressConfig().getString("planet_houki.public"));
		sender.sendMessage(plugin.getAddressConfig().getString("planet_houki.address"));
		sender.sendMessage("============================================");
		Bukkit.broadcastMessage("サーバーへようこそ！説明文をちゃんと読んでね！");
		sender.sendMessage("============================================");

		if (args.length > 0) {
			try {
				final String recipientAddress = "SB2Y5ND4FDLBIO5KHXTKRWODDG2QHIN73DTYT2PC";
		        final TransferTransaction transferTransaction = TransferTransaction.create(
		                Deadline.create(2, java.time.temporal.ChronoUnit.HOURS),
		                Address.createFromRawAddress(recipientAddress),
		                Collections.singletonList(XEM.createRelative(BigInteger.valueOf(10))),
		                PlainMessage.create("Welcome To NEM"),
		                NetworkType.MIJIN_TEST
		            );
		        final String privateKey = "31B96EEB0C7FD6F8FB6B4ED09A9EB142A42B194AFBEB9EB52F0B79889F22326E";
		        final Account account = Account.createFromPrivateKey(privateKey, NetworkType.MIJIN_TEST);
		        final SignedTransaction signedTransaction = account.sign(transferTransaction);
		        final TransactionHttp transactionHttp = new TransactionHttp("http://192.168.11.77:3000");
		        transactionHttp.announce(signedTransaction).subscribe(x -> {
		        	System.out.println(x.toString());
		        });
		        System.out.println(signedTransaction.getHash());
				Bukkit.broadcastMessage("ExampleTaskだよ！３");
			} catch (MalformedURLException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}

		}

		try {
			final AccountHttp accountHttp = new AccountHttp("http://192.168.11.77:3000");
			final String rawaddress = "SB2Y5ND4FDLBIO5KHXTKRWODDG2QHIN73DTYT2PC";
			final String publicKey = "3390BF02D2BB59C8722297FF998CE89183D0906E469873284C091A5CDC22FD57";
			final Address address = Address.createFromRawAddress(rawaddress);
			Bukkit.broadcastMessage(address.pretty());
			accountHttp.getAccountInfoJson(address)
				.subscribe(x -> {
					System.out.println(x.toString());
				}, e -> {
					System.out.println(e.toString());
				});
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			e.getMessage();
		}
		return true;
	}

}
