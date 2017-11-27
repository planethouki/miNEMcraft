package com.planethouki.plugin;

import org.bukkit.entity.Player;

public class PlayerWallet {
	
	MinemcraftPlugin plugin;
	
	public PlayerWallet(MinemcraftPlugin plugin) {
		this.plugin = plugin;
	}
	
	public String getAddress(Player player) {
		// TODO find player name from configuration file
		
		// TODO if can't find, generate wallet & save configuration
		
		return "TCNO3AYQ2CNEZ66G5TL57356J2HWJFG3IP646H4N"; // test
	}
	
}
