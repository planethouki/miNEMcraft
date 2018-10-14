package com.github.planethouki.minemcraftplugin;


import java.math.BigInteger;
import java.util.Collections;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import io.nem.core.utils.HexEncoder;
import io.nem.sdk.model.account.Account;
import io.nem.sdk.model.mosaic.Mosaic;
import io.nem.sdk.model.mosaic.MosaicId;
import io.nem.sdk.model.mosaic.XEM;
import io.nem.sdk.model.transaction.Deadline;
import io.nem.sdk.model.transaction.PlainMessage;
import io.nem.sdk.model.transaction.TransferTransaction;
import io.reactivex.Observable;

public class MinemcraftCommand implements CommandExecutor {

	private MinemcraftPlugin plugin;
	private MinemcraftHelper helper;

	public MinemcraftCommand(MinemcraftPlugin plugin) {
		this.plugin = plugin;
		this.helper = plugin.getHelper();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (args.length > 0) {

			switch(args[0]) {
			case "debug":
				sender.sendMessage("============================================");
//				sender.sendMessage(plugin.getConfig().getString("profile.network"));
//				sender.sendMessage(plugin.getConfig().getString("profile.host"));
//				sender.sendMessage(plugin.getConfig().getString("profile.privateKey"));
//				sender.sendMessage(plugin.getConfig().getString("profile.apostilleaddress"));
//				sender.sendMessage("--------------------------------------------");
//				plugin.getAddressConfig().getKeys(false).forEach(uuid -> {
//					sender.sendMessage("name: " + plugin.getAddressConfig().getString(uuid + ".name"));
//					sender.sendMessage("private: " + plugin.getAddressConfig().getString(uuid + ".private"));
//					sender.sendMessage("public: " + plugin.getAddressConfig().getString(uuid + ".public"));
//					sender.sendMessage("address: " + plugin.getAddressConfig().getString(uuid + ".address"));
//				});
				sender.sendMessage("============================================");
				break;
			case "randuuid":
				sender.sendMessage(UUID.randomUUID().toString());
				break;
			case "reload":
				plugin.reloadConfig();
				plugin.loadAddressConfig();
				break;
			}
		}
		return true;
	}

}
