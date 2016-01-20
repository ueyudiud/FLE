package fle.core.render.model;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelFluid;
import net.minecraftforge.fluids.FluidRegistry;
import flapi.util.FleValue;

public class RenderLoaderFleFluid implements ICustomModelLoader
{
	@Override
	public void onResourceManagerReload(IResourceManager resourceManager)
	{
		
	}

	@Override
	public boolean accepts(ResourceLocation modelLocation)
	{
		return modelLocation.getResourceDomain().equals(FleValue.FLUID_RENDER_CONTROL);
	}

	public IModel loadModel(ResourceLocation modelLocation)
	{
		String name = modelLocation.getResourcePath();
		if(name.startsWith("models/item/"))
		{
			name = name.substring("models/item/".length());
		}
		else if(name.startsWith("models/block/"))
		{
			name = name.substring("models/block/".length());
		}
		else throw new RuntimeException("Can not indentify model location name '" + name + "', please check your mod.");
		if(!FluidRegistry.isFluidRegistered(name))
		{
			throw new RuntimeException("This fluid is not register in Forge, please check your mod.");
		}
		return new ModelFluid(FluidRegistry.getFluid(name));
	}
}