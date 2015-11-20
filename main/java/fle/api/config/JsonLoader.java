package fle.api.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;

import org.apache.logging.log4j.core.config.JSONConfiguration;

import scala.util.parsing.json.JSON;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonStreamParser;
import com.google.gson.stream.JsonReader;

import fle.api.FleAPI;
import fle.api.util.FleLog;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.JsonToNBT;

public class JsonLoader
{
	private File file;
	
	public JsonLoader(String fileName)
	{
		file = new File(new File(FleAPI.mod.getPlatform().getMinecraftDir(), "config"), fileName);
	}

	public void process(IJsonLoader...loaders)
	{
		process(Arrays.asList(loaders));
	}
	public void process(Iterable<IJsonLoader> loaders)
	{
		try
		{
			JsonStreamParser jsp = null;
			try
			{
				jsp = read();
			}
			catch(Throwable e)
			{
				throw e;
			}
			finally
			{
				while (jsp.hasNext())
				{
					try
					{
						JsonElement element = jsp.next();
						for(IJsonLoader loader : loaders)
							loader.readJson(element);
					}
					catch(Throwable e)
					{
						FleLog.getLogger().catching(e);
					}
				}
			}
		}
		catch(Throwable e)
		{
			FleLog.getLogger().catching(e);
		}
	}
	
	private JsonStreamParser read() throws IOException
	{
		if(!file.canExecute())
		{
			file.createNewFile();
		}
		return new JsonStreamParser(new FileReader(file));
	}
}