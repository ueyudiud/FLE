package farcore.lib.model.item;

import java.util.Map;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
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