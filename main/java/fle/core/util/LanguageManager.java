package fle.core.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.xml.internal.fastinfoset.util.CharArray;

import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import fle.FLE;
import fle.api.FleAPI;
import fle.api.util.ILanguageManager;

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
		boolean flag = false;
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
					continue;
				}
				flag = true;
				str += "." + (char) (chr & 0xFFDF);
				continue;
			}
			str += chr;
		}
		FleAPI.lm.registerLocal(str, enLocalizedName);
		return FleAPI.lm.translateToLocal(str, new Object[0]);
	}
	
	public static File lang = null;
	
	private static Map<String, String> langMap = new HashMap();
	
	public static void load()
	{
		langMap.clear();
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
                    langMap.put(line.split(",")[0], line.split(",")[1].replaceFirst(" ", ""));
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
	
	public static void save()
	{
		BufferedWriter fw = null;
		try
		{
			fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
			        lang), "UTF-8"), 1024);
			fw.write("TAG, NAME\r\n");
			for(String str : langMap.keySet())
			{
				fw.write(String.format("%s, %s\r\n", str, langMap.get(str)));
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
		if(langMap.containsKey(unlocalizedName))
		{
			return;
		}
		langMap.put(unlocalizedName, localizedName);
	}
	
	@Override
	public String translateToLocal(String str, Object...objects)
	{
		String ret;
		if(langMap.containsKey(str))
		{
			ret = String.format(langMap.get(str), objects);
		}
		else
		{
			ret = I18n.format(str, objects);
			langMap.put(str, ret);
		}
		return ret;
	}
}