/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula;

import java.io.File;
import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.TransformerExclusions({"nebula", "nebula.asm", "nebula.asm.cm", "nebula.asm.keyword"})
public class NebulaLoadingPlugin implements IFMLLoadingPlugin
{
	public static boolean loadedData;
	public static boolean runtimeDeobf;
	public static File location;
	
	@Override
	public String[] getASMTransformerClass()
	{
		return new String[] {"nebula.asm.ClassTransformer"};
	}
	
	@Override
	public String getModContainerClass()
	{
		return "nebula.Nebula";
	}
	
	@Override
	public String getSetupClass()
	{
		return "nebula.asm.NebulaSetup";
	}
	
	@Override
	public void injectData(Map<String, Object> data)
	{
		runtimeDeobf = ((Boolean) data.get("runtimeDeobfuscationEnabled")).booleanValue();
		location = (File) data.get("mcLocation");
		loadedData = true;
	}
	
	@Override
	public String getAccessTransformerClass()
	{
		return null;
	}
}