package fle.core.render.model;

import java.io.IOException;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;

public class ModelLoader implements ICustomModelLoader
{
	@Override
	public void onResourceManagerReload(IResourceManager resourceManager)
	{
		
	}

	@Override
	public boolean accepts(ResourceLocation modelLocation)
	{
		return false;
	}

	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws IOException
	{
		return null;
	}
}