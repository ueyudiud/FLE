package fle.core.util;

import java.io.File;

import cpw.mods.fml.relauncher.FMLInjectionData;
import net.minecraft.client.Minecraft;

public class FleSetup
{
	private File mcDir;
	public String mcVeision;

	public void setup()
	{
		mcDir = Minecraft.getMinecraft().mcDataDir;
		mcVeision = (String) FMLInjectionData.data()[4];
		setupLangurgeFile();
	}

	private final String fileName1 = "fle_lang.json";
	
	private void setupLangurgeFile()
	{
		File destination = mcDir;
		LanguageManager.lang = new File(destination, fileName1);
		try
		{
			if(!LanguageManager.lang.exists())
			{
				LanguageManager.lang.createNewFile();
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException("create new cfg failed.", e);
		}
	}
}