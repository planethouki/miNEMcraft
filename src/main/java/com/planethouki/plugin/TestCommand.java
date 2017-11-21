package com.planethouki.plugin;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

public class TestCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// TODO Auto-generated method stub
		sender.sendMessage("Command Accepted");
		if (args.length == 0) { return false; }
		
		String url = "";
		
		switch (args[0]) {
		case "balance":
		case "bal":
			url = "http://127.0.0.1:7890/account/get?address=TD4EC4YNUAWN2EPACF7FRP4IHU7XSMFFUCYPMZZD";
			sender.sendMessage(MyHttpClient.executeGet(url));
			try {
				String genreJson = MyHttpClient.executeGet(url);
				JSONObject genreJsonObject = (JSONObject) JSONValue.parseWithException(genreJson);
				JSONObject accountJsonObject = (JSONObject) genreJsonObject.get("account");
				System.out.println(accountJsonObject.get("balance"));
				// get the data
	//			JSONArray genreArray = (JSONArray) genreJsonObject.get("dataset");
				// get the first genre
	//			JSONObject firstGenre = (JSONObject) genreArray.get(0);
	//			System.out.println(firstGenre.get("genre_title"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			break;
		case "tip":
			url = "TCNO3A-YQ2CNE-Z66G5T-L57356-J2HWJF-G3IP64-6H4N";
			break;
		default:
			break;
		}
		return true;
	}


}
