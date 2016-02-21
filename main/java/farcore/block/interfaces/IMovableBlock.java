package farcore.block.interfaces;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import flapi.world.BlockPos;

/**
 * The interface of movable block.
 * @author ueyudiud
 *
 */
public interface IMovableBlock
{
	public void onBlockStartMove(World aWorld, BlockPos aPos);
	
	public boolean canBlockFallOn(World aWorld, BlockPos aPos, boolean groundCheck);
	
	public void onBlockEndMove(World aWorld, BlockPos aPos, int metadata);
	
	public void onBlockHitEntity(World aWorld, EntityLivingBase aEntity);
}