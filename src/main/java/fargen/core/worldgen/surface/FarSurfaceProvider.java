package fargen.core.worldgen.surface;

import fargen.core.FarGen;
import fargen.core.biome.BiomeBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.Biome;

public class FarSurfaceProvider extends WorldProvider
{
	@Override
	public DimensionType getDimensionType()
	{
		return FarGen.FAR_OVERWORLD;
	}

	@Override
	protected void createBiomeProvider()
	{
		biomeProvider = new FarSurfaceBiomeProvider();
	}
	
	/**
	 * Called to determine if the chunk at the given chunk coordinates within the provider's world can be dropped. Used
	 * in WorldProviderSurface to prevent spawn chunks from being unloaded.
	 */
	@Override
	public boolean canDropChunk(int x, int z)
	{
		return !worldObj.isSpawnChunk(x, z) || !worldObj.provider.getDimensionType().shouldLoadSpawn();
	}

	@Override
	public boolean canSnowAt(BlockPos pos, boolean checkLight)
	{
		return super.canSnowAt(pos, checkLight);
	}

	@Override
	public Biome getBiomeForCoords(BlockPos pos)
	{
		return BiomeBase.DEBUG;
	}
}