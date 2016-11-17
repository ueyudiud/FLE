package farcore.lib.util;

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
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;

import farcore.util.U;
import net.minecraft.client.resources.I18n;

public class LanguageManager
{
	/** The default locale of manager. */
	public static final String ENGLISH = "en_US";
	/** Load from language file. */
	private static final Map<String, Map<String, String>> MAP1 = new HashMap();
	/** Load from code. */
	private static final Map<String, String> MAP2 = new HashMap();
	private static final FileFilter FILTER = file -> file.getName().endsWith(".lang");
	private static boolean loadFile = false;

	public static void registerLocal(String unlocalized, String localized)
	{
		if(!MAP1.getOrDefault(ENGLISH, ImmutableMap.of()).containsKey(unlocalized))
		{
			loadFile = false;
		}
		MAP2.put(unlocalized, localized);
	}

	public static String translateToLocal(String unlocalized, Object...objects)
	{
		String locale = U.Strings.locale();
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
			return U.Strings.translateByI18n(unlocalized, objects);
		try
		{
			return translate == null ? unlocalized : String.format(translate, objects);
		}
		catch(Exception exception)
		{
			return "Error Translation";
		}
	}
	
	/**
	 * For text component translation.
	 * @param unlocalized
	 * @return
	 */
	public static String translateToLocalOfText(String unlocalized)
	{
		String locale = U.Strings.locale();
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

	public static String translateToLocalWithIgnoreUnmapping(String unlocalized, Object...objects)
	{
		String locale = U.Strings.locale();
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

	private File file;

	public LanguageManager(File file)
	{
		this.file = file;
	}

	public void reset()
	{
		Log.info("Far Core reset language manager.");
		MAP1.clear();
	}

	public void read()
	{
		if (!file.canRead())
			return;
		MAP1.clear();
		BufferedReader reader = null;
		Log.info("Start read localized file.");
		try
		{
			for(File file : file.listFiles(FILTER))
			{
				String name = file.getName();
				Log.info("Loading " + name + " language file.");
				name = name.substring(0, ".lang".length());
				int keyCount = 0;
				Map<String, String> map = new HashMap();
				try
				{
					reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
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
				finally
				{
					try
					{
						if(reader != null)
						{
							reader.close();
						}
					}
					catch (Exception exception2)
					{
						exception2.printStackTrace();
					}
				}
			}
			loadFile = true;
		}
		catch(Exception exception)
		{
			Log.warn("Fail to read language file.", exception);
		}
	}

	public void write()
	{
		if(loadFile)
			return;
		if(!file.exists())
		{
			file.mkdirs();
		}
		if(!file.canWrite())
		{
			Log.info("Fail to write language file because can not write lang in.");
			return;
		}
		Log.info("Start to write localized file.");
		int keyCount = 0;
		BufferedWriter writer = null;
		BufferedReader reader = null;
		try
		{
			File file = new File(this.file, ENGLISH + ".lang");
			try
			{
				if(!file.exists())
				{
					file.createNewFile();
				}
				writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
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
			finally
			{
				if(writer != null)
				{
					try
					{
						writer.close();
					}
					catch(IOException exception2)
					{
						Log.warn("Fail to close language file.", exception2);
					}
				}
			}
		}
		catch(Exception exception)
		{
			Log.warn("Fail to write language file.", exception);
		}
		Log.info("Wrote " + keyCount + " keys to file.");
	}
}