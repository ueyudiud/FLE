/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

/**
 * Minecraft only give 256 index to save biome id.<br>
 * So you can create a new biome list only use in one dimension. So there are
 * some method should be override.<br>
 * This method is to re-get the biome at coord in world, please implements this
 * interface at World Provider.
 * 
 * @author ueyudiud
 *
 */
public interface IBiomeRegetter
{
	/**
	 * Get new biome from block pos.<br>
	 * DO NOT USE {@link net.minecraft.world.World#getBiome} TO GET BIOME
	 * BECAUSE THIS METHOD IS OVERRIDE FROM THERE.
	 * 
	 * @param oldBiome
	 * @param pos
	 * @return
	 */
	Biome getBiome(int saveID, BlockPos pos);
}
