package farcore.lib.model.item.unused;

import java.util.Map;
import java.util.function.Function;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Deprecated
@SideOnly(Side.CLIENT)
public interface IMultiTextureCollection extends Function<IResourceManager, Map<String, ResourceLocation>>
{
	@Override
	default Map<String, ResourceLocation> apply(IResourceManager manager)
	{
		return apply();
	}
	
	Map<String, ResourceLocation> apply();
}
