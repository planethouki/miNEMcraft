package com.planethouki.plugin;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

public class DebugCommand implements CommandExecutor {
	
	private MinemcraftPlugin plugin;
	
	public DebugCommand(MinemcraftPlugin plugin) {
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
		case "balance":
		case "bal":
			String serverAddress = plugin.getConfig().getString("ServerAddress").replaceAll("-", "");
			
			url = "http://127.0.0.1:7890/account/get?address=" + serverAddress;
			sender.sendMessage(url);
			sender.sendMessage(MyHttpClient.executeGet(url));
			break;
		case "send":
			//TODO とりあえず送金するトランザクションを投げる
			String timeStamp = "83680000";
			String amount = "10000000";
			String fee = "50000";
			String recipient = plugin.getAddressConfig().getString("planet_houki");
			String deadline = "83683600";
			String signer = plugin.getConfig().getString("ServerPublicKey");
			String privateKey = plugin.getConfig().getString("ServerPrivateKey");
			url = "http://localhost:7890/transaction/prepare-announce";
//			data = "{\"transaction\":{\"timeStamp\":" + 
//					"83680000" + ",\"amount\":" + 
//					amount + ",\"fee\":50000,\"recipient\":\"" + 
//					recipient + "\",\"type\":257,\"deadline\":" + 
//					"83683600" + ",\"message\":{\"payload\":\"\",\"type\":1},\"version\":-1744830463,\"signer\":\"" +
//					signer + "\"},\"privateKey\":\"" +
//					privateKey + "\"}";
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
