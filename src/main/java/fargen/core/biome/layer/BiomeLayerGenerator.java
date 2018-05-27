/*
 * copyright 2016-2018 ueyudiud
 */
package fargen.core.biome.layer;

import java.util.Random;

import farcore.lib.world.IWorldPropProvider;
import fargen.core.biome.BiomeBase;
import nebula.base.collection.IPropertyMap;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public interface BiomeLayerGenerator
{
	void genTerrainBlocks(World world, Random rand, ChunkPrimer primer, int x, int z, BiomeBase biome, IWorldPropProvider provider, IPropertyMap map);
}
