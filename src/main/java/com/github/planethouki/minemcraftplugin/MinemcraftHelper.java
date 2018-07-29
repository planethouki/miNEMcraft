package com.github.planethouki.minemcraftplugin;

import io.nem.sdk.model.blockchain.NetworkType;

public class MinemcraftHelper {

	MinemcraftHelper() {

	}

	public static NetworkType getNetwork(String network) {
		if (network.equalsIgnoreCase("MIJIN_TEST")) {
			return NetworkType.MIJIN_TEST;
		} else if (network.equalsIgnoreCase("MIJIN")) {
			return NetworkType.MIJIN;
		} else if (network.equalsIgnoreCase("TEST_NET")) {
			return NetworkType.TEST_NET;
		} else if (network.equalsIgnoreCase("MAIN_NET")) {
			return NetworkType.MAIN_NET;
		}
		throw new IllegalArgumentException("Unexpected Network. use MIJIN_TEST, MIJIN, TEST_NET, MAIN_NET");

	}

	public static String formatXEMrelative(String amountInt) {
		if (amountInt.length() > 6) {
			return
					amountInt.substring(0, amountInt.length() - 6) +
					"." +
					amountInt.substring(amountInt.length() - 6);
		} else {
			return
					"0." +
					"000000".substring(amountInt.length()) +
					amountInt
					;
		}
	}
}
