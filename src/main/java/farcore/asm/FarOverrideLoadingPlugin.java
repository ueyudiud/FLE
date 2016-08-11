package farcore.asm;

import java.io.File;
import java.util.Map;

import farcore.asm.instance._Chunk;
import farcore.asm.instance._EntityRenderer;
import farcore.asm.instance._World;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.TransformerExclusions({"farcore.override.asm"})
public class FarOverrideLoadingPlugin implements IFMLLoadingPlugin
{
	public static boolean runtimeDeobf;
	public static File location;

	@Override
	public String[] getASMTransformerClass()
	{
		return new String[]{
				_Chunk.class.getName(),
				_World.class.getName(),
				_EntityRenderer.class.getName()};
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