package com.github.planethouki.minemcraftplugin.command;


import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.github.planethouki.minemcraftplugin.JsonStringBuilder;
import com.github.planethouki.minemcraftplugin.MinemcraftPlugin;
import com.github.planethouki.minemcraftplugin.MyHttpClient;
import com.github.planethouki.minemcraftplugin.crypto.Crypto;

public class TestCommand implements CommandExecutor {

	private MinemcraftPlugin plugin;

	public TestCommand(MinemcraftPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (args.length == 0) {
			sender.sendMessage("few arg");
			return false;
		}

		String url = "";
		String data = "";
		String recipient = "";
		JsonStringBuilder jdBuilder;

		switch (args[0]) {
		case "height":
			String height = "";
			try {
				height = Crypto.getChainHeight();
				plugin.getNotification().sayChainHeight(sender, height);
			} catch (InterruptedException e) {
				plugin.getNotification().sayError(sender);
				e.printStackTrace();
			} catch (ExecutionException e) {
				plugin.getNotification().sayError(sender);
				e.printStackTrace();
			}
			break;
		case "heartbeat":
		case "hb":
			url = "http://127.0.0.1:7890/heartbeat";
			sender.sendMessage(url);
			sender.sendMessage(MyHttpClient.executeGet(url));
			break;
		case "status":
			url = "http://127.0.0.1:7890/status";
			sender.sendMessage(url);
			sender.sendMessage(MyHttpClient.executeGet(url));
			break;
		case "serverbalance":
		case "sbal":
			String serverAddress = plugin.getConfig().getString("ServerAddress").replaceAll("-", "");
			url = "http://127.0.0.1:7890/account/get?address=" + serverAddress;
			sender.sendMessage(url);
			sender.sendMessage(MyHttpClient.executeGet(url));
			break;
		case "balance":
		case "mybal":
			String palyerAddress = plugin.getAddressConfig().getString(sender.getName());
			if (palyerAddress == null) {
				sender.sendMessage("your address is not registered yet");
				break;
			}
			url = "http://127.0.0.1:7890/account/get?address=" + palyerAddress.replaceAll("-", "");
			sender.sendMessage(url);
			sender.sendMessage(MyHttpClient.executeGet(url));
			break;
		case "register":
			if (args.length == 1) {
				sender.sendMessage("need your address");
			} else if (args.length >= 3) {
				sender.sendMessage("too many args");
			} else {
				plugin.getAddressConfig().set(sender.getName(), args[1]);
				plugin.saveAddressConfig();
			}
			break;
		case "generate":
			sender.sendMessage(MyHttpClient.executeGet("http://127.0.0.1:7890/account/generate"));
			break;
		case "send":
			recipient = plugin.getAddressConfig().getString(sender.getName()).replaceAll("-", "");
			jdBuilder = new JsonStringBuilder(plugin);
			url = "http://localhost:7890/transaction/prepare-announce";
			data = jdBuilder.getRequestPrepareAnnounce(recipient, 5000);
			sender.sendMessage(url);
			sender.sendMessage(data);
			sender.sendMessage(MyHttpClient.executePost(url, data));
			break;
		case "sendp":
			recipient = "TCNO3AYQ2CNEZ66G5TL57356J2HWJFG3IP646H4N";
			jdBuilder = new JsonStringBuilder(plugin);
			url = "http://localhost:7890/transaction/prepare-announce";
			data = jdBuilder.getRequestPrepareAnnounce(recipient, 5000);
			sender.sendMessage(url);
			sender.sendMessage(data);
			sender.sendMessage(MyHttpClient.executePost(url, data));
			break;
		case "sendp2":
			Calendar nemEpoch = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
			nemEpoch.set(2015, 2, 29, 0, 6, 25);
			Calendar now = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

			String timeStamp = Long.toString((now.getTimeInMillis() - nemEpoch.getTimeInMillis())/1000);
			String amount = "50000";
			String fee = "50000";
			recipient = plugin.getAddressConfig().getString("planet_houki").replaceAll("-", "");
			String deadline = Long.toString(((now.getTimeInMillis() - nemEpoch.getTimeInMillis())/1000)+3600);
			String signer = plugin.getConfig().getString("ServerPublicKey");
			String privateKey = plugin.getConfig().getString("ServerPrivateKey");
			url = "http://localhost:7890/transaction/prepare-announce";
			data = String.format("{\"transaction\":{\"timeStamp\":%s,\"amount\":%s,\"fee\":%s,\"recipient\":\"%s\",\"type\":257,\"deadline\":%s,\"message\":{\"payload\":\"\",\"type\":1},\"version\":-1744830463,\"signer\":\"%s\"},\"privateKey\":\"%s\"}",timeStamp, amount, fee, recipient, deadline, signer, privateKey);
			sender.sendMessage(url);
			sender.sendMessage(data);
			sender.sendMessage(MyHttpClient.executePost(url, data));
			break;
		case "conf":
			String confMsg = plugin.getConfig().getString("ServerPublicKey");
			sender.sendMessage(confMsg);
			break;
		default:
			sender.sendMessage("unknown arg");
			break;
		}
		return true;
	}


}
