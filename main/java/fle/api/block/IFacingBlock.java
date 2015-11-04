package fle.api.block;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import fle.api.world.BlockPos;

/**
 * The facing handler of block.
 * Extended on block class or tile entity class.
 * @author ueyudiud
 *
 */
public interface IFacingBlock
{
	/**
	 * Get direction of this block.
	 * @param pos
	 * @return The direction of block.
	 */
	public ForgeDirection getDirction(BlockPos pos);
	
	public boolean canSetDirection(BlockPos pos, ItemStack tool, float xPos, float yPos, float zPos);
	
	public void setDirection(World world, BlockPos pos, ItemStack tool, float xPos, float yPos, float zPos);
}