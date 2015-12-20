package fle.core.util;

import java.io.File;

import net.minecraft.client.Minecraft;
import cpw.mods.fml.relauncher.FMLInjectionData;
import flapi.material.MaterialAbstract;

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

	private final String fileName1 = "fle_lang.csv";
	private final String fileName2 = "fle_material.csv";
	
	private void setupLangurgeFile()
	{
		File destination = mcDir;
		LanguageManager.lang = new File(destination, fileName1);
		MaterialAbstract.property = new File(destination, fileName2);
		try
		{
			if(!LanguageManager.lang.exists())
			{
				LanguageManager.lang.createNewFile();
			}
			if(!MaterialAbstract.property.exists())
			{
				MaterialAbstract.property.createNewFile();
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException("create new cfg failed.", e);
		}
	}
}