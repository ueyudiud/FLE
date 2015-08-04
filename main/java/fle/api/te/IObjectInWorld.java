package fle.api.te;

import net.minecraft.world.World;
import fle.api.world.BlockPos;

public interface IObjectInWorld 
{
	public World getWorldObj();
	
	public BlockPos getBlockPos();
}