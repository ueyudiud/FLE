package fle.override;

import java.io.File;
import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import fle.override.asm.O_WorldServer;

@IFMLLoadingPlugin.TransformerExclusions({"fle.override.asm"})
public class FarOverrideLoadingPlugin implements IFMLLoadingPlugin
{
	public static boolean runtimeDeobf;
	public static File location;

	@Override
	public String[] getASMTransformerClass()
	{
		return new String[]
				{
				O_WorldServer.class.getName()
				};
	}

	@Override
	public String getModContainerClass()
	{
		return FarOverride.class.getName();
	}

	@Override
	public String getSetupClass()
	{
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data)
	{
	    runtimeDeobf = ((Boolean) data.get("runtimeDeobfuscationEnabled")).booleanValue();
	    location = (File) data.get("coremodLocation");
	}

	@Override
	public String getAccessTransformerClass()
	{
		return null;
	}
}