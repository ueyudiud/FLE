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

import farcore.util.U;
import net.minecraft.client.resources.I18n;

public class LanguageManager
{
	public static final String ENGLISH = "en_US";
	/** Load from language file. */
	private static final Map<String, Map<String, String>> map1 = new HashMap();
	/** Load from code. */
	private static final Map<String, String> map2 = new HashMap();
	private static final FileFilter filter = (File file) ->
	{
		return file.getName().endsWith(".lang");
	};

	public static void registerLocal(String unlocalized, String localized)
	{
		map2.put(unlocalized, localized);
	}

	public static String translateToLocal(String unlocalized, Object...objects)
	{
		String locale = U.Strings.locale();
		String translate;
		if(map1.containsKey(locale) && map1.get(locale).containsKey(unlocalized))
			translate = map1.get(locale).get(unlocalized);
		else if(map2.containsKey(unlocalized))
			translate = map2.get(unlocalized);
		else
			return I18n.format(unlocalized, objects);
		try
		{
			return translate == null ? unlocalized : String.format(translate, objects);
		}
		catch(Exception exception)
		{
			return "Error Translation";
		}
	}

	private boolean loadFile = false;
	private File file;

	public LanguageManager(File file)
	{
		this.file = file;
	}

	public void reset()
	{
		Log.info("Far Core reset language manager.");
		map1.clear();
	}

	public void read()
	{
		if (!file.canRead())
			return;
		map1.clear();
		BufferedReader reader = null;
		try
		{
			for(File file : file.listFiles(filter))
			{
				String name = file.getName();
				name = name.substring(0, ".lang".length());
				Map<String, String> map = new HashMap();
				try
				{
					reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
					String line;
					while((line = reader.readLine()) != null)
					{
						if(line.length() == 0) continue;
						if(line.indexOf('=') == -1) throw new RuntimeException();
						String[] strings = line.split("=");
						if(strings.length != 2) throw new RuntimeException();
						map.put(strings[0], strings[1]);
					}
					map1.put(name, map);
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
							reader.close();
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
			file.mkdirs();
		if(!file.canWrite())
			Log.info("Fail to write language file because can not write lang in.");
		BufferedWriter writer = null;
		try
		{
			File file = new File(this.file, ENGLISH + ".lang");
			if(!file.exists())
				file.createNewFile();
			try
			{
				writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
				Map<String, String> map = map1.getOrDefault(ENGLISH, ImmutableMap.of());
				for(Entry<String, String> entry : map2.entrySet())
					if(!map.containsKey(entry.getKey()))
						writer.write(entry.getKey() + "=" + entry.getValue() + "\r");
			}
			catch(IOException exception)
			{
				Log.warn("Fail to write language.", exception);
			}
			finally
			{
				if(writer != null)
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
		catch(Exception exception)
		{
			Log.warn("Fail to write language file.", exception);
		}
	}
}