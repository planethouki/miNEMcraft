package com.github.planethouki.minemcraftplugin;

import java.util.Calendar;
import java.util.TimeZone;


public class JsonStringBuilder {

	private Calendar nemEpoch;
	private Calendar now;
	private long timeStamp;
//	private long amount;
	private long fee;
//	private String recipient;
	private long deadline;
	private String signer;
	private String privateKey;
	private String data;

	public JsonStringBuilder(MinemcraftPlugin plugin) {
		nemEpoch = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		nemEpoch.set(2015, 2, 29, 0, 6, 25);
		// now に入る時刻はgetInstanceした時点の時刻
		now = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		timeStamp = ((now.getTimeInMillis() - nemEpoch.getTimeInMillis())/1000);
		deadline = (((now.getTimeInMillis() - nemEpoch.getTimeInMillis())/1000)+3600);
		// TODO 手数料の計算式
		fee = 50000;
		signer = plugin.getConfig().getString("ServerPublicKey");
		privateKey = plugin.getConfig().getString("ServerPrivateKey");
	}

	public String getRequestPrepareAnnounce(String recipient, int amount) {
		// オブジェクト to JSON にしたい
		data = String.format("{\"transaction\":{\"timeStamp\":%s,\"amount\":%s,\"fee\":%s,\"recipient\":\"%s\",\"type\":257,\"deadline\":%s,\"message\":{\"payload\":\"\",\"type\":1},\"version\":-1744830463,\"signer\":\"%s\"},\"privateKey\":\"%s\"}",timeStamp, amount, fee, recipient, deadline, signer, privateKey);
		return data;
	}

}
