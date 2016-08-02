package fargen.core.worldgen.surface;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import farcore.lib.world.IBiomeRegetter;
import fargen.core.biome.BiomeBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;

/**
 * Now just for debugging.
 * @author ueyudiud
 *
 */
public class FarSurfaceBiomeProvider extends BiomeProvider implements IBiomeRegetter
{
	public FarSurfaceBiomeProvider()
	{
		allowedBiomes.add(BiomeBase.DEBUG);
	}

	@Override
	public Biome getBiomeGenerator(BlockPos pos, Biome biomeGenBaseIn)
	{
		return BiomeBase.DEBUG;
	}
	
	/**
	 * Returns an array of biomes for the location input.
	 */
	@Override
	public Biome[] getBiomesForGeneration(Biome[] biomes, int x, int z, int width, int height)
	{
		if (biomes == null || biomes.length < width * height)
		{
			biomes = new Biome[width * height];
		}
		Arrays.fill(biomes, BiomeBase.DEBUG);
		return biomes;
	}
	
	/**
	 * Gets a list of biomes for the specified blocks.
	 */
	@Override
	public Biome[] getBiomeGenAt(@Nullable Biome[] listToReuse, int x, int z, int width, int length, boolean cacheFlag)
	{
		if (listToReuse == null || listToReuse.length < width * length)
		{
			listToReuse = new Biome[width * length];
		}
		Arrays.fill(listToReuse, BiomeBase.DEBUG);
		return listToReuse;
	}

	/**
	 * checks given Chunk's Biomes against List of allowed ones
	 */
	@Override
	public boolean areBiomesViable(int x, int z, int radius, List<Biome> allowed)
	{
		return true;
	}
	
	@Override
	@Nullable
	public BlockPos findBiomePosition(int x, int z, int range, List<Biome> biomes, Random random)
	{
		return null;
	}
	
	@Override
	public Biome getBiome(Biome oldBiome, BlockPos pos)
	{
		return BiomeBase.DEBUG;
	}
}