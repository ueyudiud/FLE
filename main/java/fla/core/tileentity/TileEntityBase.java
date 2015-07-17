package fla.core.tileentity;

import fla.api.world.BlockPos;
import net.minecraft.tileentity.TileEntity;

public class TileEntityBase extends TileEntity
{
	public BlockPos getBlockPos()
	{
		return new BlockPos(worldObj, xCoord, yCoord, zCoord);
	}
}
