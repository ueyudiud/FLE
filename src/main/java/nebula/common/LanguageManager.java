/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
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
 * Language manager type.
 * @author ueyudiud
 *
 */
public class LanguageManager
{
	/** The default locale of manager. */
	public static final String ENGLISH = "en_US";
	/** Localization map loaded from language file. */
	private static final Map<String, Map<String, String>> MAP1 = new HashMap<>();
	/** Localization map loaded from byte code. */
	private static final Map<String, String> MAP2 = new HashMap<>();
	private static final FileFilter FILTER = file -> file.getName().endsWith(".lang");
	/** Marker of reloading translation map from file. */
	private static boolean loadFile = false;
	
	/**
	 * Register tool tips (List of localize string) to localization map.
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
	 * Register localize string to localized manager,
	 * for <tt>unlocalized=localized</tt> entry.
	 * @param unlocalized the unlocalized string.
	 * @param localized the localized string.
	 */
	public static void registerLocal(String unlocalized, String localized)
	{
		if(!MAP1.getOrDefault(ENGLISH, ImmutableMap.of()).containsKey(unlocalized))
		{
			loadFile = false;
		}
		MAP2.put(unlocalized, localized);
	}
	
	/**
	 * Localized string and format it.<p>
	 * The localize manager will find localized source given by Nebula translation map first,
	 * or find from I18n if failed finding. The <tt>en_US</tt> will be used when unlocalized
	 * string does not exist in translation map.<p>
	 * The format action will only taken when translating is succeed.<p>
	 * If exception is caught in formating. The result will be <tt>"Translation Error"</tt>.
	 * @param unlocalized the unlocalized string.
	 * @param objects the format element,
	 * use {@link java.lang.String#format(String, Object...)} to format localized string.
	 * @return the formated localized string, or unlocalized string direct if nothing found.
	 * @see #translateToLocalWithIgnoreUnmapping(String, Object...)
	 */
	public static String translateToLocal(String unlocalized, Object...objects)
	{
		String locale = Strings.locale();
		String translate;
		if (MAP1.containsKey(locale) && MAP1.get(locale).containsKey(unlocalized))
		{
			translate = MAP1.get(locale).get(unlocalized);
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
		catch(Exception exception)
		{
			Log.catchingIfDebug(exception);
			return "Translation Error";
		}
	}
	
	/**
	 * For text component translation.
	 * @param unlocalized
	 * @return
	 */
	public static String translateToLocalOfText(String unlocalized)
	{
		String locale = Strings.locale();
		String translate;
		if(MAP1.containsKey(locale) && MAP1.get(locale).containsKey(unlocalized))
		{
			translate = MAP1.get(locale).get(unlocalized);
		}
		else if(MAP2.containsKey(unlocalized))
		{
			translate = MAP2.get(unlocalized);
		}
		else return net.minecraft.util.text.translation.I18n.translateToLocal(unlocalized);
		return translate == null ? unlocalized : translate;
	}
	
	/**
	 * Localized string and format unsafely.<p>
	 * The localize manager will find localized source given by Nebula translation map first,
	 * or find from I18n if failed finding. The <tt>en_US</tt> will be used when unlocalized
	 * string does not exist in translation map.<p>
	 * The format action will only taken when translating is succeed.<p>
	 * @param unlocalized the unlocalized string.
	 * @param objects the format element,
	 * use {@link java.lang.String#format(String, Object...)} to format localized string.
	 * @return the formated localized string, or <tt>null</tt> if nothing found.
	 * @see #translateToLocal(String, Object...)
	 */
	public static @Nullable String translateToLocalWithIgnoreUnmapping(String unlocalized, Object...objects)
	{
		String locale = Strings.locale();
		String translate;
		if(MAP1.containsKey(locale) && MAP1.get(locale).containsKey(unlocalized))
		{
			translate = MAP1.get(locale).get(unlocalized);
		}
		else if(MAP2.containsKey(unlocalized))
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
		catch(Exception exception)
		{
			return null;
		}
	}
	
	//Internal part start, do not use any method.
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
	
	public void read()
	{
		if (!this.file.canRead())
			return;
		MAP1.clear();
		Log.info("Start read localized file at " + this.file.getAbsolutePath());
		for(File file : this.file.listFiles(FILTER))
		{
			String name = file.getName();
			Log.info("Loading " + name + " language file.");
			name = name.substring(0, ".lang".length());
			int keyCount = 0;
			Map<String, String> map = new HashMap<>();
			
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8")))
			{
				String line;
				while((line = reader.readLine()) != null)
				{
					if(line.length() == 0)
					{
						continue;
					}
					int idx = line.indexOf('=');
					if(idx == -1) throw new RuntimeException();
					map.put(line.substring(0, idx).trim(), line.substring(idx + 1));
					++keyCount;
				}
				MAP1.put(name, map);
				Log.info("Wrote " + keyCount + " keys to language manager.");
			}
			catch(RuntimeException exception)
			{
				Log.warn("Invalid language file " + file.getName(), exception);
			}
			catch(IOException exception)
			{
				Log.warn("Fail to load language file " + file.getName(), exception);
			}
		}
		loadFile = true;
	}
	
	public void write()
	{
		if(loadFile)
			return;
		if(!this.file.exists())
		{
			this.file.mkdirs();
		}
		if(!this.file.canWrite())
		{
			Log.info("Fail to write language file because can not write lang in.");
			return;
		}
		Log.info("Start to write localized file.");
		int keyCount = 0;
		try
		{
			File file = new File(this.file, ENGLISH + ".lang");
			if(!file.exists())
			{
				file.createNewFile();
			}
			try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8")))
			{
				Map<String, String> map = MAP1.getOrDefault(ENGLISH, ImmutableMap.of());
				ImmutableMap<String, String> sortedMap = ImmutableSortedMap.copyOf(MAP2);//Use sorted map for easier to search translated word.
				for(Entry<String, String> entry : sortedMap.entrySet())
					if(!map.containsKey(entry.getKey()))
					{
						writer.write(entry.getKey() + "=" + entry.getValue() + "\r");
						++keyCount;
					}
			}
			catch(IOException exception)
			{
				Log.warn("Fail to save language file.", exception);
			}
		}
		catch(Exception exception)
		{
			Log.warn("Fail to write language file.", exception);
		}
		Log.info("Wrote " + keyCount + " keys to file.");
	}
	//Internal part end.
}