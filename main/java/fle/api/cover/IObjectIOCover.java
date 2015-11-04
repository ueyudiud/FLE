package fle.api.cover;

import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import fle.api.world.BlockPos;

public interface IObjectIOCover<S, O>
{
	S get();
	
	boolean canDrain(BlockPos pos, ForgeDirection dir, O target);
	
	boolean canFill(BlockPos pos, ForgeDirection dir, O target);
	
	S drain(BlockPos pos, ForgeDirection dir, S target, boolean flag);
	
	S drain(BlockPos pos, ForgeDirection dir, int maxSize, boolean flag);
	
	int fill(BlockPos pos, ForgeDirection dir, S resource, boolean flag);
}