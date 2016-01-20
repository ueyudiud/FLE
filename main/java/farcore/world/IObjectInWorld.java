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
	
	default double getX()
	{
		return getBlockPos().getX();
	}
	
	default double getY()
	{
		return getBlockPos().getY();
	}
	
	default double getZ()
	{
		return getBlockPos().getZ();
	}
	
	default int getT()
	{
		return getWorld().provider.getDimensionId();
	}
}