/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.world;

import java.util.List;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

public interface IWorldSubContainer
{
	World world();
	
	List<ChunkPos> chunks();
	
	boolean containPosition(BlockPos pos);
}
