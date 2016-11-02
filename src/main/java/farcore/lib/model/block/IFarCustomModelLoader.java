package farcore.lib.model.block;

import farcore.FarCore;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;

public interface IFarCustomModelLoader extends ICustomModelLoader
{
	@Override
	default void onResourceManagerReload(IResourceManager resourceManager)
	{
		//Nothing to do.
	}
	
	@Override
	default boolean accepts(ResourceLocation modelLocation)
	{
		return FarCore.INNER_RENDER.equals(modelLocation.getResourceDomain()) &&
				(modelLocation.getResourcePath().startsWith(getLoaderPrefix()) ||
						modelLocation.getResourcePath().startsWith("models/block/" + getLoaderPrefix()) ||
						modelLocation.getResourcePath().startsWith("models/item/" + getLoaderPrefix()));
	}

	String getLoaderPrefix();
}