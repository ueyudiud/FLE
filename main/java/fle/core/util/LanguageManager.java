package fle.core.util;

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

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

import farcore.util.FleLog;
import farcore.util.ILanguageManager;
import farcore.util.Langs;
import fle.FLE;

public class LanguageManager implements ILanguageManager
{
	private static final String defaultLocale = Langs.english;
	private static final String MOD = FLE.MODID.toLowerCase();
	private static final char A = 'A';
	private static final char Z = 'Z';
	
	@Override
	public String registerRecommended(String locale, String type,
			String localized)
	{
		String str = MOD;
		str += ".";
		String str1 = type + "." + localized;
		boolean flag = true;
		for (int i = 0; i < str1.length(); ++i)
		{
			char chr = str1.charAt(i);
			if (chr == '_' || chr == ' ' || chr == ':')
			{
				if (flag)
				{
					flag = false;
					continue;
				}
				flag = true;
				str += ".";
				continue;
			}
			if ((int) chr <= (int) Z && (int) chr >= (int) A)
			{
				if (flag)
				{
					flag = false;
					str += Character.toLowerCase(chr);
					continue;
				}
				flag = true;
				str += "." + Character.toLowerCase(chr);
				continue;
			}
			str += chr;
		}
		registerLocal(locale, str, localized);
		return str;
	}
	
	@Override
	public String registerRecommended(String type, String localized)
	{
		return registerRecommended(defaultLocale, type, localized);
	}
	
	public static File lang = null;
	
	private static Map<String, Map<String, String>> langRegister = new HashMap();
	
	public static void load()
	{
		if (!lang.canExecute())
			return;
		langRegister.clear();
		@SuppressWarnings("resource")
		JsonReader reader = null;
		try
		{
			reader = new JsonReader(new BufferedReader(
					new InputStreamReader(new FileInputStream(lang), "UTF-8"),
					1024));
			try
			{
				reader.beginObject();
				while (reader.hasNext())
				{
					String locale = reader.nextName();
					Map<String, String> map;
					langRegister.put(locale, map = new HashMap());
					reader.beginObject();
					while (reader.hasNext())
					{
						String n = reader.nextName();
						String l = reader.nextString();
						map.put(n, l);
					}
					reader.endObject();
				}
				reader.endObject();
			}
			catch (IOException e)
			{
				FleLog.addExceptionToCache(e);
			}
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
		finally
		{
			FleLog.resetAndCatchException(
					"FLE : Find the exception while loading lang file.");
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
	}
	
	public static void save()
	{
		JsonWriter writer = null;
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
					FleLog.addExceptionToCache(e);
				}
			}
			writer.endObject();
		}
		catch (IOException e)
		{
			FleLog.addExceptionToCache(e);
		}
		finally
		{
			FleLog.resetAndCatchException(
					"Catching these exception when saving lang.");
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
	}
	
	@Override
	public void registerLocal(String locale, String unlocalizedName,
			String localizedName)
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
	
	@Override
	public void registerLocal(String unlocalizedName, String localizedName)
	{
		registerLocal(defaultLocale, unlocalizedName, localizedName);
	}
	
	@Override
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
			FleLog.debug("Fail to format name " + str + ".");
			ret = "Fail to load name";
		}
		return ret;
	}
}