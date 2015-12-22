package flapi.util.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonStreamParser;
import com.google.gson.stream.JsonWriter;

import flapi.FleAPI;
import flapi.util.FleLog;

public class JsonHandler
{
	private File file;
	private List<String> list = new ArrayList();
	private final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

	public JsonHandler(String fileName)
	{
		this("config", fileName);
	}
	public JsonHandler(String location, String fileName)
	{
		file = new File(new File(FleAPI.mod.getPlatform().getMinecraftDir(), location), fileName);
	}
	
	public <T> List<T> get(Class<T> clazz)
	{
		List<T> ret = new ArrayList<T>();
		for(String string : list)
		{
			try
			{
				ret.add(gson.fromJson(string, clazz));
			}
			catch(Throwable e)
			{
				FleLog.addExceptionToCache(e);
			}
		}
		FleLog.resetAndCatchException("Fle API : Fail to get target of class type : " + clazz.toString());
		return ret;
	}
	
	public <T> void register(T...ts)
	{
		for(T t : ts)
		{
			try
			{
				list.add(gson.toJson(t));
			}
			catch(Throwable e)
			{
				FleLog.addExceptionToCache(e);
			}
		}
		FleLog.resetAndCatchException("Fle API: Catching exception during loading json from targets : " + ts.toString());
	}
	public <T> void register(Iterable<T> ts)
	{
		for(T t : ts)
		{
			try
			{
				list.add(gson.toJson(t));
			}
			catch(Throwable e)
			{
				FleLog.addExceptionToCache(e);
			}
		}
		FleLog.resetAndCatchException("Fle API: Catching exception during loading json from targets : " + ts.toString());
	}
	
	public <T> void write(IJsonLoader loader)
	{
		try
		{
			loader.writeJson(gson, list);
		}
		catch(Throwable e)
		{
			FleLog.getLogger().info("Catching an exception during write json to list.", e);
		}
		write(loader.getSaveClass());
	}
	
	public <T> void write(Class<T> clazz)
	{
		JsonWriter writer = null;
		try
		{
			FileWriter w = new FileWriter(file);
			writer = new JsonWriter(new BufferedWriter(w));
			for(String str : list)
			{
				try
				{
					gson.toJson(gson.fromJson(str, clazz), clazz, writer);
				}
				catch(Throwable e)
				{
					FleLog.addExceptionToCache(e);
				}
			}
			FleLog.resetAndCatchException("Fle API: Fail to encode the json to file.");
		}
		catch(Throwable e)
		{
			FleLog.getLogger().catching(e);
		}
		finally
		{
			try
			{
				writer.close();
			}
			catch(Throwable e)
			{
				FleLog.getLogger().catching(e);
			}
		}
	}

	public boolean read()
	{
		try
		{
			JsonStreamParser jsp = null;
			try
			{
				if(!file.canExecute())
				{
					file.createNewFile();
					return false;
				}
				jsp = new JsonStreamParser(new FileReader(file));
			}
			catch(Throwable e)
			{
				throw e;
			}
			finally
			{
				if(jsp != null)
				while (jsp.hasNext())
				{
					JsonElement jsonElement = (JsonElement) jsp.next();			
					try
					{
						list.add(jsonElement.toString());
					}
					catch(Throwable e)
					{
						FleLog.addExceptionToCache(e);
					}
				}
			}
		}
		catch(Throwable e)
		{
			FleLog.getLogger().catching(e);
		}
		FleLog.resetAndCatchException("Fle API: catching during loading json file.");
		return true;
	}

	public boolean read(IJsonLoader loader)
	{
		if(read())
		{
			try
			{
				loader.readJson(gson, list);
			}
			catch(Throwable e)
			{
				FleLog.getLogger().info("Fle API: catching an exceptio during reading json list.", e);
			}
			return true;
		}
		return false;
	}
}