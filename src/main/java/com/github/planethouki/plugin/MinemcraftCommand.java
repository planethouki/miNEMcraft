package com.github.planethouki.plugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MinemcraftCommand implements CommandExecutor {

	private MinemcraftPlugin plugin;

	public MinemcraftCommand(MinemcraftPlugin plugin) {
		super();
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// TODO Auto-generated method stub

		if (args.length == 0) {
			sender.sendMessage("引数が足りません！");
			return false;
		}

		String url = "";
		String data = "";
		String recipient = "";
		JsonStringBuilder jdBuilder;


		switch (args[0]) {
		case "heartbeat":
			url = "http://127.0.0.1:7890/heartbeat";
			sender.sendMessage(MyHttpClient.executeGet(url));
			break;
		case "status":
			url = "http://127.0.0.1:7890/status";
			sender.sendMessage(MyHttpClient.executeGet(url));
			break;
		case "serverbalance":
			String serverAddress = plugin.getConfig().getString("ServerAddress").replaceAll("-", "");
			url = "http://127.0.0.1:7890/account/get?address=" + serverAddress;
			sender.sendMessage(MyHttpClient.executeGet(url));
			break;
		case "balance":
			String palyerAddress = plugin.getAddressConfig().getString(sender.getName());
			if (palyerAddress == null) {
				sender.sendMessage("your address is not registered yet");
				break;
			}
			url = "http://127.0.0.1:7890/account/get?address=" + palyerAddress.replaceAll("-", "");
			MyHttpClient.executeGet(url);
			break;
		}


		return false;
	}

}
