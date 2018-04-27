package com.github.planethouki.minemcraftplugin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class MyHttpClient {
    public static String executeGet(String argurl) {
    	String rawResponse = "";
    	String line;
    	try {
			URL url = new URL(argurl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.connect();
	    	StringBuilder builder = new StringBuilder();
			if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
				reader.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(con.getErrorStream()));
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
				reader.close();
			}
			con.disconnect();
			rawResponse = builder.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rawResponse;
    }

    public static String executePost(String argurl, String argpost) {
    	String rawResponse = "";

    	try {
	    	StringBuilder builder = new StringBuilder();
	    	String line;
	    	URL url;
	    	HttpURLConnection con;
			url = new URL(argurl);
			con = (HttpURLConnection) url.openConnection();
			con.setDoOutput(true);
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
			writer.write(argpost);
			writer.flush();
			writer.close();
			con.connect();
			if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
				reader.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(con.getErrorStream()));
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
				reader.close();
			}
			con.disconnect();
    		rawResponse = builder.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	
		return rawResponse;

    }
}
