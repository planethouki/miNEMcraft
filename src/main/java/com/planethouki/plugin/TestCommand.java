package com.planethouki.plugin;


import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

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
		
		switch (args[0]) {
		case "sbal":
			String serverAddress = plugin.getConfig().getString("ServerAddress").replaceAll("-", "");
			url = "http://127.0.0.1:7890/account/get?address=" + serverAddress;
			sender.sendMessage(url);
			sender.sendMessage(MyHttpClient.executeGet(url));
			break;
		case "mybal":
			String playerName = sender.getName();
			if (playerName == null) {
				sender.sendMessage("your address is not registered yet");
				break;
			}
			String palyerAddress = plugin.getAddressConfig().getString(playerName).replaceAll("-", "");
			url = "http://127.0.0.1:7890/account/get?address=" + palyerAddress;
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
			//TODO とりあえず送金するトランザクションを投げる
			Calendar nemEpoch = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
			nemEpoch.set(2015, 2, 29, 0, 6, 25);
			Calendar now = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
			
			String timeStamp = Long.toString((now.getTimeInMillis() - nemEpoch.getTimeInMillis())/1000);
			String amount = "50000";
			String fee = "50000";
			String recipient = plugin.getAddressConfig().getString("planet_houki").replaceAll("-", "");
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
