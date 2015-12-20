package flapi.util.io;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import com.google.gson.JsonElement;
import com.google.gson.JsonStreamParser;

import flapi.FleAPI;
import flapi.util.FleLog;

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