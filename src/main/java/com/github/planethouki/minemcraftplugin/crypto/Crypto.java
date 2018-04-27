package com.github.planethouki.minemcraftplugin.crypto;

import java.util.concurrent.ExecutionException;

import org.bukkit.entity.Player;
import org.nem.core.model.Block;

import io.nem.apps.api.ChainApi;

public class Crypto {
	public boolean hasAddress(Player player) {
		// TODO
		return true;
	}

	public String generateAddress(Player player) {
		// TODO
		return "todo";
	}

	public static String getChainHeight() throws InterruptedException, ExecutionException {
		return ChainApi.getChainHeight();
	}
}
