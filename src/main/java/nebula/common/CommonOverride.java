/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common;

import nebula.common.world.IBiomeExtended;
import nebula.common.world.IBiomeRegetter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;

/**
 * @author ueyudiud
 */
public class CommonOverride
{
	/**
	 * Used by ASM.
	 * 
	 * @param world
	 * @param pos
	 * @return
	 */
	public static boolean isRainingAtBiome(Biome biome, World world, BlockPos pos)
	{
		if (biome instanceof IBiomeExtended) return ((IBiomeExtended) biome).canRainingAt(world, pos);
		return biome.canRain();
	}
	
	/**
	 * Used by ASM.
	 * 
	 * @param oldBiome
	 * @param pos
	 * @param provider
	 * @return
	 */
	public static Biome regetBiome(int saveID, BlockPos pos, BiomeProvider provider)
	{
		if (provider instanceof IBiomeRegetter) return ((IBiomeRegetter) provider).getBiome(saveID, pos);
		return Biome.getBiome(saveID);
	}
}
