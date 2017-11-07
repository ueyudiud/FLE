package fargen.core.biome.decorator;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;

public abstract class BiomeDecorator
{
	public abstract void decorate(World world, Random rand, BlockPos pos, IChunkGenerator generator);
}
