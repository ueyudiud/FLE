/*
 * copyright© 2016-2018 ueyudiud
 */
package fargen.core.worldgen.surface;

import farcore.lib.world.EnumWorldGeneratePhase;
import farcore.lib.world.IWorldGenerateReplacer;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public class FarSurfaceReplacer implements IWorldGenerateReplacer
{
	@Override
	public void replaceWorld(EnumWorldGeneratePhase phase, World world, int x, int z, ChunkPrimer primer)
	{
		switch (phase)
		{
		case ORE_GENERATE:
			replaceOre(world, x, z, primer);
			break;
		default:
			break;
		}
	}
	
	private void replaceOre(World world, int x, int z, ChunkPrimer primer)
	{
		
	}
}
