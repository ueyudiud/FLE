package fargen.core.biome.layer;

import java.util.Random;

import farcore.lib.block.instance.BlockRock;
import fargen.core.biome.BiomeBase;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public abstract class BiomeLayerGenerator
{
	public abstract void genTerrainBlocks(World world, Random rand, ChunkPrimer primer, int x, int z, double noiseVal, BiomeBase biome, int submeta);

	protected boolean isRock(Block block)
	{
		return block instanceof BlockRock;
	}
}