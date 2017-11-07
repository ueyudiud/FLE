package farcore.lib.world;

import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public interface IWorldGenerateReplacer
{
	/**
	 * The replacer of world generator.
	 * 
	 * @param phase
	 * @param world
	 * @param x
	 * @param z
	 * @param primer
	 */
	void replaceWorld(EnumWorldGeneratePhase phase, World world, int x, int z, ChunkPrimer primer);
}
