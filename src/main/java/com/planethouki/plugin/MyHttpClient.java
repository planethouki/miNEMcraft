package com.planethouki.plugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MyHttpClient {
    public static String executeGet(String argurl) {
    	// TODO Coding
    	URL url;
    	HttpURLConnection con;
    	StringBuilder builder = new StringBuilder();;
    	try {
			url = new URL(argurl);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.connect();
	    	String line;
			BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			reader.close();
			con.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	//String rawResponse = "{\"code\":6,\"type\":4,\"message\":\"status\"}";
		return builder.toString();
    }

    public static String executePost(String argurl, String argpost) {
    	// TODO Coding
    	String rawResponse = "{\"timeStamp\":84375275,\"error\":\"Not Found\",\"message\":null,\"status\":404}";
		return rawResponse;

    }
}
