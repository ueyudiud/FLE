package fle.core.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import net.minecraft.client.resources.I18n;
import flapi.FleAPI;
import flapi.collection.Register;
import flapi.util.FleLog;
import flapi.util.ILanguageManager;
import fle.FLE;

public class LanguageManager implements ILanguageManager
{
	private static final String MOD = FLE.MODID.toLowerCase();
	private static final char A = 'A';
	private static final char Z = 'Z';
	
	public static String regWithRecommendedUnlocalizedName(String aTypeName, String enLocalizedName)
	{
		String str = MOD;
		str += ".";
		String str1 = aTypeName + "." + enLocalizedName;
		boolean flag = true;
		for(int i = 0; i < str1.length(); ++i)
		{
			char chr = str1.charAt(i);
			if(chr == '_' || chr == ' ' || chr == ':')
			{
				if(flag)
				{
					flag = false;
					continue;
				}
				flag = true;
				str += ".";
				continue;
			}
			if((int) chr <= (int) Z && (int) chr >= (int) A)
			{
				if(flag)
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
		FleAPI.langManager.registerLocal(str, enLocalizedName);
		return FleAPI.langManager.translateToLocal(str, new Object[0]);
	}
	
	public static File lang = null;
	
	private static Register langRegister = new Register();
	
	public static void load()
	{
		langRegister.clear();
		BufferedReader fr = null;
		try
		{
			fr = new BufferedReader(new InputStreamReader(new FileInputStream(lang), "UTF-8"), 1024);
			fr.readLine();
			String line;
			try
			{
				while((line = fr.readLine()) != null)
				{
					try
					{
						String[] strs = line.split(",");
						langRegister.register(Integer.valueOf(strs[0]), strs[1], strs[2]);
					}
					catch(Throwable e)
					{
						FleLog.addExceptionToCache(e);
					}
				}
			}
			catch(Throwable e)
			{
				FleLog.addExceptionToCache(e);
			}
			finally
			{
				FleLog.resetAndCatchException("FLE : Find the exception while loading lang file.");
			}
		}
		catch(Throwable e)
		{
			e.printStackTrace();
		}
	}
	
	public static void save()
	{
		BufferedWriter fw = null;
		try
		{
			fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
			        lang), "UTF-8"), 1024);
			fw.write("TAG, NAME\r\n");
			String[] langs = langRegister.keySet();
			for(int i = 0; i < langs.length; ++i)
			{
				String name = langs[i];
				if(name == null) continue;
				fw.write(String.format("%s,%s,%s\r\n", i, name, langRegister.get(name)));
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
	}
	
	@Override
	public void registerLocal(String unlocalizedName, String localizedName)
	{
		if(langRegister.contain(unlocalizedName))
		{
			return;
		}
		langRegister.register(localizedName, unlocalizedName);
	}
	
	@Override
	public String translateToLocal(String str, Object...objects)
	{
		String ret;
		if(langRegister.contain(str))
		{
			ret = String.format((String) langRegister.get(str), objects);
		}
		else
		{
			ret = I18n.format(str, objects);
			langRegister.register(str, ret);
		}
		return ret;
	}
}