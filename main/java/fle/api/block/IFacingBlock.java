package fle.api.block;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import fle.api.world.BlockPos;

public interface IFacingBlock
{
	public ForgeDirection getDirction(BlockPos pos);
	
	public boolean canSetDirection(BlockPos pos, ItemStack tool, float xPos, float yPos, float zPos);
	
	public void setDirection(World world, BlockPos pos, ItemStack tool, float xPos, float yPos, float zPos);
}