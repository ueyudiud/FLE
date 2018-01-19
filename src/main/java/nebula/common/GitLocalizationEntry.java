/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;

import nebula.Log;

/**
 * @author ueyudiud
 */
class GitLocalizationEntry implements INetworkLocalizationEntry
{
	/**
	 * The Gson used to parse SHA of localization files.
	 */
	static final Gson GSON = new GsonBuilder().registerTypeAdapter(String.class, SHAAdapter.INSTANCE).create();
	
	String key;
	String path;
	String branch;
	
	GitLocalizationEntry(String key, String path, String branch)
	{
		this.key = key;
		this.path = path;
		this.branch = branch;
	}
	
	private static BufferedReader openReader(URL url) throws IOException
	{
		URLConnection connection = url.openConnection();
		connection.setConnectTimeout(2000);
		connection.setReadTimeout(25000);
		return new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
	}
	
	public boolean loadLocalization(LanguageManager manager, Map<String, String> properties,
			String locale, Map<String, String> localization)
	{
		try
		{
			URL url;
			url = new URL("https://api.github.com/repos/" + this.path + "/contents/lang?ref=" + this.branch);
			SHAAdapter.INSTANCE.name = locale + ".lang";
			String key;
			try (JsonReader reader = new JsonReader(openReader(url)))
			{
				key = GSON.fromJson(reader, String.class);
			}
			if (!properties.getOrDefault(this.key, "").equals(key))
			{
				url = new URL("https://raw.githubusercontent.com/" + this.path + "/" + this.branch + "/lang/" + locale + ".lang");
				Log.info("Downloading localization file from {}, it may takes some times, please waiting...", url);
				try (BufferedReader reader = openReader(url))
				{
					manager.read(reader, localization);
				}
				properties.put(this.path, key);
				return true;
			}
			return false;
		}
		catch (IOException exception)
		{
			Log.info("Failed to connect localization file '{}' from {}", locale, this.path);
			Log.catching(exception);
			return false;
		}
		catch (JsonParseException exception)
		{
			Log.info("The localization file '{}.lang' does not exist yet.", locale);
			return false;
		}
	}
}
