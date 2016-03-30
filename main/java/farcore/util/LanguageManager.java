package farcore.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import fle.core.FLE;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

public class LanguageManager
{
	public static final String ENGLISH = "en_US";
	public static final String CHINESE = "zh_CN";
		
	private static final String defaultLocale = ENGLISH;
	
	private File lang;
	
	public LanguageManager(File file)
	{
		lang = file;
	}
	
	private static Map<String, Map<String, String>> langRegister = new HashMap();
	
	public void load()
	{
		if (!lang.canExecute())
			return;
		langRegister.clear();
		JsonReader reader = null;
		FleLog.coreLogger.info("Far Core start load language file.");
		try
		{
			reader = new JsonReader(new BufferedReader(
					new InputStreamReader(new FileInputStream(lang), "UTF-8"),
					1024));
			try
			{
				int count;
				reader.beginObject();
				while (reader.hasNext())
				{
					String locale = reader.nextName();
					Map<String, String> map;
					langRegister.put(locale, map = new HashMap());
					try
					{
						count = 0;
						reader.beginObject();
						while (reader.hasNext())
						{
							try
							{
								String n = reader.nextName();
								String l = reader.nextString();
								map.put(n, l);
								++count;
							}
							catch(IOException exception)
							{
								continue;
							}
						}
						reader.endObject();
						FleLog.coreLogger.info("Successfully registered " + count + " language with locale '" + locale + "' entries from file.");
					}
					catch(IOException exception)
					{
						FleLog.coreLogger.warn("Fail to load group of '" + locale + "', clearing all language.", exception);
						langRegister.remove(locale);
					}
				}
				reader.endObject();
			}
			catch (IOException exception)
			{
				FleLog.coreLogger.warn("Fail to load language file, clearing all language.", exception);
				langRegister.clear();
			}
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (reader != null)
			{
				try
				{
					reader.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		FleLog.coreLogger.info("Far Core loaded language file.");
	}
	
	public void save()
	{
		JsonWriter writer = null;
		FleLog.coreLogger.info("Far Core start save language file.");
		try
		{
			writer = new JsonWriter(new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(lang), "UTF-8"),
					1024));
			writer.setIndent(" ");
			writer.beginObject();
			for (Entry<String, Map<String, String>> entry : langRegister
					.entrySet())
			{
				if (entry.getValue() == null || entry.getValue().size() == 0)
					continue;
				try
				{
					writer.name(entry.getKey());
					writer.beginObject();
					Map<String, String> register = entry.getValue();
					for (Entry<String, String> line : register.entrySet())
					{
						writer.name(line.getKey());
						writer.value(line.getValue());
					}
					writer.endObject();
				}
				catch (IOException e)
				{
					FleLog.coreLogger.info("Far Core fail to save language file.");
				}
			}
			writer.endObject();
		}
		catch (IOException e)
		{
			FleLog.coreLogger.info("Far Core fail to save language file.");
		}
		finally
		{
			if (writer != null)
			{
				try
				{
					writer.flush();
					writer.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		FleLog.coreLogger.info("Far Core saved language file.");
	}
	
	public void registerLocal(String locale, String unlocalizedName, String localizedName)
	{
		if (!langRegister.containsKey(locale))
			langRegister.put(locale, new HashMap());
		Map<String, String> register = langRegister.get(locale);
		if (register.containsKey(unlocalizedName))
		{
			return;
		}
		register.put(unlocalizedName, localizedName);
	}
	
	public void registerLocal(String unlocalizedName, String localizedName)
	{
		registerLocal(defaultLocale, unlocalizedName, localizedName);
	}
	
	public String translateToLocal(String str, Object... objects)
	{
		String locale = Minecraft.getMinecraft().getLanguageManager()
				.getCurrentLanguage().getLanguageCode();
		String ret;
		Map<String, String> map;
		try
		{
			if (langRegister.containsKey(locale)
					&& (map = langRegister.get(locale)).containsKey(str))
			{
				ret = String.format(map.get(str), objects).trim();
			}
			else
			{
				ret = I18n.format(str, objects);
				registerLocal(locale, str, ret);
			}
		}
		catch (IllegalArgumentException exception)
		{
			FleLog.coreLogger.debug("Fail to format name " + str + ".");
			ret = "Fail to load name";
		}
		return ret;
	}
}