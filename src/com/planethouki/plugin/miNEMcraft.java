package com.planethouki.plugin;

import org.bukkit.plugin.java.JavaPlugin;


public class miNEMcraft extends JavaPlugin {

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		super.onDisable();
	}

	@Override
	public void onEnable() {
		// TODO Auto-generated method stub
		getCommand("nemc").setExecutor(new testCommand());
		super.onEnable();
	}

	

}
