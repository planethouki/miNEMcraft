package com.planethouki.plugin;

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
		return false;
	}

}
