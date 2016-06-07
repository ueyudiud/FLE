package fle.cwg.world;

import farcore.util.U;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;

public class FCWGSurfaceProvider extends WorldProvider
{
	@Override
	public String getDimensionName()
	{
		return "FCWG Surface";
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
	
	@Override
	public boolean canSnowAt(int x, int y, int z, boolean checkLight)
	{
		return super.canSnowAt(x, y, z, checkLight);
	}
}