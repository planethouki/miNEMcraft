package com.github.planethouki.minemcraftplugin;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import com.fasterxml.jackson.databind.ObjectWriter.GeneratorSettings;

import io.nem.sdk.infrastructure.Listener;
import io.nem.sdk.model.account.Account;
import io.nem.sdk.model.blockchain.NetworkType;
import io.nem.sdk.model.transaction.SignedTransaction;
import io.nem.sdk.model.transaction.Transaction;




public class MinemcraftPlugin extends JavaPlugin {

	private FileConfiguration addressConfig;
	private File addressFile;
	private Listener blockchainListener;
	private NetworkType blockchainNetworkType;
	private String blockchainHost;

	public MinemcraftPlugin() {
		super();
		saveDefaultConfig();
		saveResource("address.yml", false);
		addressFile = new File(getDataFolder(), "address.yml");
	}

	@Override
	public void onDisable() {

		// Commands

		// Listeners
		HandlerList.unregisterAll(this);

		// Configurations
//		saveConfig();
//		saveAddressConfig();

		// Others
		blockchainListener.close();
		getLogger().info("Plugin Disabled");

		super.onDisable();
	}

	@Override
	public void onEnable() {
		// Configurations
		addressConfig = loadAddressConfig();

		// properties
		blockchainNetworkType = MinemcraftHelper.getNetwork(getConfig().getString("profile.network"));
		blockchainHost = getConfig().getString("profile.url");
		try {
			blockchainListener = new Listener(blockchainHost);
			blockchainListener.open();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		// Commands
		getCommand("mnc").setExecutor(new MinemcraftCommand(this));

		// Listeners
		new HarvestListener(this);
		new MineListener(this);
		new LoginListener(this);
		new AdvancementListener(this);

		getLogger().info("Server Address: " + getServerAddress());
		getLogger().info("Apostille Address: " + getApostilleAddress());
		getLogger().info("Network Type: " + blockchainNetworkType.name());
		getLogger().info("Node: " + blockchainHost);

		getLogger().info("Plugin Enabled");

		super.onEnable();

	}


	// address config
	void saveAddressConfig() {
		try {
			addressConfig.save(addressFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	FileConfiguration loadAddressConfig() {
		return YamlConfiguration.loadConfiguration(addressFile);
	}

	// properties setter getter
	FileConfiguration getAddressConfig() {
		return addressConfig;
	}

	Listener getBlockchainListener() {
		return blockchainListener;
	}

	NetworkType getBlockchainNetworkType() {
		return blockchainNetworkType;
	}

	String getBlockchainHost() {
		return blockchainHost;
	}



	String getServerAddress() {
		return Account
		.createFromPrivateKey(getConfig().getString("profile.privateKey"), blockchainNetworkType)
		.getAddress()
		.plain();
	}
	String getApostilleAddress() {
		return getConfig().getString("profile.apostilleaddress");
	}

	String getPlayerAddress(Player player) {
		return getPlayerAddress(player.getUniqueId());
	}

	String getPlayerAddress(UUID uuid) {
		return getAddressConfig().getString(uuid + ".address");
	}


	UUID getPlayerUUIDByName(String name) {
		Player recepientPlayer = getServer().getPlayerExact(name);
		if (recepientPlayer != null) {
			return recepientPlayer.getUniqueId();
		} else {
			OfflinePlayer op = getServer().getOfflinePlayer(name);
			if (op.hasPlayedBefore()) {
			    return op.getUniqueId();
			}
		}
		return null;
	}


	SignedTransaction signByServer(Transaction transaction) {
		Account a = Account.createFromPrivateKey(getConfig().getString("profile.privateKey"), blockchainNetworkType);
		return a.sign(transaction);
	}

	SignedTransaction signByPlayer(Transaction transaction, Player player) {
		String uuid = player.getUniqueId().toString();
		if (getAddressConfig().contains(uuid)) {
			Account a = Account.createFromPrivateKey(getAddressConfig().getString(uuid + ".private"), blockchainNetworkType);
			return a.sign(transaction);
		}
		return null;
	}


}
