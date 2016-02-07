package farcore.world;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

/**
 * Use for object in world instance.<br>
 * For block, this use in tile entity. For entity, this implemented of that
 * type.
 * 
 * @author ueyudiud
 * 		
 */
public interface IObjectInWorld
{
	/**
	 * Get world.
	 * 
	 * @return
	 */
	World getWorld();
	
	BlockPos getBlockPos();
}