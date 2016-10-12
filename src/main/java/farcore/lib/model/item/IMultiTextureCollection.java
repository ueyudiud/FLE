package farcore.lib.model.item;

import java.util.Map;
import java.util.function.Function;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public interface IMultiTextureCollection extends Function<IResourceManager, Map<String, ResourceLocation>>
{
	@Override
	default Map<String, ResourceLocation> apply(IResourceManager manager)
	{
		return apply();
	}

	Map<String, ResourceLocation> apply();
}
