package com.planethouki.plugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class balanceCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// TODO Auto-generated method stub
		String url = "http://127.0.0.1:7890/account/get?address=TD4EC4YNUAWN2EPACF7FRP4IHU7XSMFFUCYPMZZD";
		sender.sendMessage(MyHttpClient.executeGet(url));
		return true;
	}

}
