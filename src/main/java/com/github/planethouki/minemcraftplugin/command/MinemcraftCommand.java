package com.github.planethouki.minemcraftplugin.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.github.planethouki.minemcraftplugin.JsonStringBuilder;
import com.github.planethouki.minemcraftplugin.MinemcraftPlugin;
import com.github.planethouki.minemcraftplugin.MyHttpClient;
import com.github.planethouki.minemcraftplugin.crypto.Crypto;

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

		switch (args[0]) {
		case "address":

			break;
		case "balance":

			break;
		case "send":

			break;
		}


		return false;
	}

}
