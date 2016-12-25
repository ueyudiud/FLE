package farcore.asm;

import java.io.File;
import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.TransformerExclusions({"farcore.override.asm"})
public class FarOverrideLoadingPlugin implements IFMLLoadingPlugin
{
	public static boolean loadedData;
	public static boolean runtimeDeobf;
	public static File location;
	
	@Override
	public String[] getASMTransformerClass()
	{
		return new String[] {"farcore.asm.ClassTransformerModifyMethod"};
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
		loadedData = true;
	}
	
	@Override
	public String getAccessTransformerClass()
	{
		return null;
	}
}