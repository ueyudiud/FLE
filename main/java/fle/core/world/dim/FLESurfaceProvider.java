package fle.core.world.dim;

import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;

public class FLESurfaceProvider extends WorldProvider
{	
	@Override
	public String getDimensionName()
	{
		return "FLE Surface";
	}
	
	@Override
	public String getWelcomeMessage()
	{
		return "Entering ths Surface";
	}
	
	@Override
	public String getDepartMessage()
	{
		return "Leaving this Surface";
	}
	
	@Override
	public IChunkProvider createChunkGenerator()
	{
		return new FLESurfaceChunkProvider(worldObj, getSeed(), worldObj.getWorldInfo().isMapFeaturesEnabled());
	}

	@Override
	public float getCloudHeight()
	{
		return 256.0F;
	}
}