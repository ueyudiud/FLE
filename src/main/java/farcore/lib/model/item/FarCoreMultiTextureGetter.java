package farcore.lib.model.item;

import java.util.Map;

import net.minecraft.util.ResourceLocation;

public class FarCoreMultiTextureGetter implements IMultiTextureCollection
{
	private Map<String, ResourceLocation> map;
	
	public FarCoreMultiTextureGetter(Map<String, ResourceLocation> map)
	{
		this.map = map;
	}

	@Override
	public Map<String, ResourceLocation> apply()
	{
		return map;
	}
}