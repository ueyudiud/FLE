/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;

import nebula.Log;
import nebula.common.util.Strings;
import net.minecraft.client.resources.I18n;
import scala.actors.threadpool.Arrays;

/**
 * Language manager use to localized string to display.
 * 
 * @author ueyudiud
 */
public class LanguageManager
{
	/** The default locale of manager. */
	public static final String					ENGLISH	= "en_US";
	/** Localization map loaded from language file. */
	private static final Map<String, String>	MAP1	= new HashMap<>();
	/** Localization map loaded from byte code. */
	private static final Map<String, String>	MAP2	= new HashMap<>();
	
	/**
	 * Register tool tips (List of localize string) to localization map.
	 * 
	 * @param unlocalized the unlocalized string.
	 * @param localized the localized strings.
	 */
	public static void registerTooltip(String unlocalized, String...localized)
	{
		if (localized.length == 1)
		{
			registerLocal(unlocalized, localized[0]);
		}
		else
		{
			for (int i = 0; i < localized.length; ++i)
			{
				registerLocal(unlocalized + "." + (i + 1), localized[i]);
			}
		}
	}
	
	/**
	 * Register localize string with string format.
	 * 
	 * @param unlocalized the unlocalized string.
	 * @param localized the localized string.
	 * @param formats the formats to string.
	 * @see String#format(String, Object...) String.format
	 * @see #registerLocal(String, String)
	 */
	public static void registerLocal(String unlocalized, String localized, Object...formats)
	{
		try
		{
			String format = String.format(localized, formats);
			registerLocal(unlocalized, format);
		}
		catch (IllegalFormatException exception)
		{
			Log.error("Invalid format {}, {}", localized, Arrays.toString(formats));
		}
	}
	
	/**
	 * Register localize string to localized manager, for
	 * <tt>unlocalized=localized</tt> entry.
	 * 
	 * @param unlocalized the unlocalized string.
	 * @param localized the localized string.
	 */
	public static void registerLocal(String unlocalized, String localized)
	{
		MAP2.put(unlocalized, localized);
	}
	
	/**
	 * Localized string and format it.
	 * <p>
	 * The localize manager will find localized source given by Nebula
	 * translation map first, or find from I18n if failed finding. The
	 * <tt>en_US</tt> will be used when unlocalized string does not exist in
	 * translation map.
	 * <p>
	 * The format action will only taken when translating is succeed.
	 * <p>
	 * If exception is caught in formating. The result will be
	 * <tt>"Translation Error"</tt>.
	 * 
	 * @param unlocalized the unlocalized string.
	 * @param objects the format element, use
	 *            {@link java.lang.String#format(String, Object...) String.format} to format
	 *            localized string.
	 * @return the formated localized string, or unlocalized string direct if
	 *         nothing found.
	 * @see #translateToLocalWithIgnoreUnmapping(String, Object...)
	 */
	public static String translateToLocal(String unlocalized, Object...objects)
	{
		String translate;
		if (MAP1.containsKey(unlocalized))
		{
			translate = MAP1.get(unlocalized);
		}
		else if (MAP2.containsKey(unlocalized))
		{
			translate = MAP2.get(unlocalized);
		}
		else
			return Strings.translateByI18n(unlocalized, objects);
		try
		{
			return translate == null ? unlocalized : String.format(translate, objects);
		}
		catch (Exception exception)
		{
			return translate;
		}
	}
	
	/**
	 * For text component translation.
	 * 
	 * @param unlocalized
	 * @return
	 */
	public static String translateToLocalOfText(String unlocalized)
	{
		String translate;
		if (MAP1.containsKey(unlocalized))
		{
			translate = MAP1.get(unlocalized);
		}
		else if (MAP2.containsKey(unlocalized))
		{
			translate = MAP2.get(unlocalized);
		}
		else
			return net.minecraft.util.text.translation.I18n.translateToLocal(unlocalized);
		return translate == null ? unlocalized : translate;
	}
	
	/**
	 * Localized string and format unsafely.
	 * <p>
	 * The localize manager will find localized source given by Nebula
	 * translation map first, or find from I18n if failed finding. The
	 * <tt>en_US</tt> will be used when unlocalized string does not exist in
	 * translation map.
	 * <p>
	 * The format action will only taken when translating is succeed.
	 * <p>
	 * 
	 * @param unlocalized the unlocalized string.
	 * @param objects the format element, use
	 *            {@link java.lang.String#format(String, Object...) String.format} to format
	 *            localized string.
	 * @return the formated localized string, or <tt>null</tt> if nothing found.
	 * @see #translateToLocal(String, Object...)
	 */
	public static @Nullable String translateToLocalWithIgnoreUnmapping(String unlocalized, Object...objects)
	{
		String translate;
		if (MAP1.containsKey(unlocalized))
		{
			translate = MAP1.get(unlocalized);
		}
		else if (MAP2.containsKey(unlocalized))
		{
			translate = MAP2.get(unlocalized);
		}
		else
		{
			translate = I18n.format(unlocalized, objects);
			return unlocalized.equals(translate) ? null : translate;
		}
		try
		{
			return translate == null ? null : String.format(translate, objects);
		}
		catch (Exception exception)
		{
			return null;
		}
	}
	
	// Internal part start, do not use these method.
	private File file;
	
	public LanguageManager(File file)
	{
		this.file = file;
	}
	
	public void reset()
	{
		Log.info("Reset language manager.");
		MAP1.clear();
	}
	
	private Map<String, String> read(String locale)
	{
		if (!this.file.canRead()) return ImmutableMap.of();
		File file = new File(this.file, locale + ".lang");
		if (!file.exists()) return ImmutableMap.of();
		String name = file.getName();
		Log.info("Loading " + name + " language file.");
		int keyCount = 0;
		Map<String, String> map = new HashMap<>();
		
		int l = 0;
		try (
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8")))
		{
			String line;
			while ((line = reader.readLine()) != null)
			{
				if (line.length() != 0 && line.charAt(0) != '#')
				{
					int idx = line.indexOf('=');
					if (idx == -1) throw new RuntimeException();
					map.put(line.substring(0, idx).trim(), line.substring(idx + 1));
					++keyCount;
				}
				++l;
			}
			Log.info("Wrote " + keyCount + " keys to language manager.");
		}
		catch (RuntimeException exception)
		{
			Log.warn("Invalid language file " + file.getName() + ". line: " + l, exception);
		}
		catch (IOException exception)
		{
			Log.warn("Fail to load language file " + file.getName(), exception);
		}
		return map;
	}
	
	public void read()
	{
		MAP1.clear();
		Log.info("Start read localized file at " + this.file.getAbsolutePath());
		MAP1.putAll(read(Strings.locale()));
	}
	
	public void write()
	{
		if (!this.file.exists())
		{
			this.file.mkdirs();
		}
		if (!this.file.canWrite())
		{
			Log.info("Fail to write language file because can not write lang in.");
			return;
		}
		Log.info("Start to write localized file.");
		int keyCount = 0;
		try
		{
			File file = new File(this.file, ENGLISH + ".lang");
			if (!file.exists()) file.createNewFile();
			Map<String, String> map = read(ENGLISH);
			try (
					BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8")))
			{
				ImmutableMap<String, String> sortedMap = ImmutableSortedMap.copyOf(MAP2);// Use
				// sorted
				// map
				// for
				// easier
				// to
				// search
				// translated
				// word.
				for (Entry<String, String> entry : sortedMap.entrySet())
					if (!map.containsKey(entry.getKey()))
					{
						writer.write(entry.getKey() + "=" + entry.getValue() + "\r");
						++keyCount;
					}
			}
			catch (IOException exception)
			{
				Log.warn("Fail to save language file.", exception);
			}
		}
		catch (Exception exception)
		{
			Log.warn("Fail to write language file.", exception);
		}
		Log.info("Wrote " + keyCount + " keys to file.");
	}
	// Internal part end.
}
