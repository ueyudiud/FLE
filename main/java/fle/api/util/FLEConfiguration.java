package fle.api.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;

import fle.core.world.layer.FLELayer;

public class FLEConfiguration
{
	private File file;
	private HashMap<String, ConfigInfomation> cfgMap = new HashMap<String, ConfigInfomation>();
	
	public FLEConfiguration(File aFile)
	{
		file = aFile;
	}
	
	public void init()
	{
		cfgMap.clear();
		BufferedReader fr = null;
		try
		{
			if(!file.exists())
			{
				file.createNewFile();
				return;
			}
			fr = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"), 1024);
			fr.readLine();
			String line;
			try
			{
				while((line = fr.readLine()) != null)
				{
					String[] infos = line.split(",");
					String[] arg = new String[infos.length - 1];
					System.arraycopy(infos, 1, arg, 0, infos.length - 1);
                    cfgMap.put(infos[0], new ConfigInfomation(arg));
				}
			}
			finally
			{
				
			}
		}
		catch(Throwable e)
		{
			e.printStackTrace();
		}
	}
	
	public void save()
	{
		BufferedWriter fw = null;
		try
		{
			fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
			        file), "UTF-8"), 1024);
			for(String str : cfgMap.keySet())
			{
				String code = cfgMap.get(str).toString();
				if(code.replaceAll(" ", "").isEmpty())
					fw.write(String.format("%s\r\n", str));
				else
					fw.write(String.format("%s, %s\r\n", str, code));
			}
			fw.flush();
			fw.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(fw != null)
			{
				try
				{
					fw.close();
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
		cfgMap = new HashMap(1);
	}
	
	public ConfigInfomation getConfigInfomation(String str)
	{
		if(!cfgMap.containsKey(str))
			cfgMap.put(str, ConfigInfomation.instance());
		return cfgMap.get(str);
	}
}