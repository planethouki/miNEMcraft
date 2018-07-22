package com.github.planethouki.minemcraftplugin;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.planethouki.minemcraftplugin.listener.HarvestListener;
import com.github.planethouki.minemcraftplugin.listener.LoginListener;
import com.github.planethouki.minemcraftplugin.listener.MineListener;
import com.github.planethouki.minemcraftplugin.notification.Notification;




public class MinemcraftPlugin extends JavaPlugin {

	private FileConfiguration addressConfig;
	private Notification notification;

	public MinemcraftPlugin() {
		super();
		saveDefaultConfig();
		saveResource("address.yml", false);
	}

	@Override
	public void onDisable() {

		// Commands

		// Listeners
		HandlerList.unregisterAll(this);

		// Configurations
		saveConfig();
		saveAddressConfig();

		// Others
		getLogger().info("Plugin Disabled");

		super.onDisable();
	}

	@Override
	public void onEnable() {

		// Commands
		getCommand("mnc").setExecutor(new MinemcraftCommand(this));

		// Listeners
		new HarvestListener(this);
		new MineListener(this);
		new LoginListener(this);

		// Instance
		this.notification = new Notification();

		// Configurations
		this.saveDefaultConfig();
		addressConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "address.yml"));

		// Others
		getLogger().info("Plugin Enabled");


		super.onEnable();
	}


	// getter
	public FileConfiguration getAddressConfig() {
		return this.addressConfig;
	}
	public Notification getNotification() {
		return this.notification;
	}


	public void saveAddressConfig() {
		try {
			addressConfig.save("address.yml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
