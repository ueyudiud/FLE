package fle.core.world.gen;

import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;

public class FleSurfaceProvider extends WorldProvider
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
		return terrainType.getChunkGenerator(worldObj, field_82913_c);
	}

	@Override
	public float getCloudHeight()
	{
		return 256.0F;
	}
}