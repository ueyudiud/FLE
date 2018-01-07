/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
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
	
	public boolean loadLocalization(LanguageManager manager, Map<String, String> properties,
			String locale, Map<String, String> localization)
	{
		try
		{
			URL url;
			url = new URL("https://api.github.com/repos/" + this.path + "/contents/lang?ref=" + this.branch);
			SHAAdapter.INSTANCE.name = locale + ".lang";
			String key;
			try (JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))))
			{
				key = GSON.fromJson(reader, String.class);
			}
			if (!properties.getOrDefault(this.key, "").equals(key))
			{
				url = new URL("https://raw.githubusercontent.com/" + this.path + "/" + this.branch + "/lang/" + locale + ".lang");
				Log.info("Downloading localization file from {}, it may takes some times, please waiting...", url);
				try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8")))
				{
					manager.read(reader, localization);
				}
				properties.put(locale, key);
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
