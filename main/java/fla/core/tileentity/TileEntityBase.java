package fla.core.tileentity;

import java.util.Random;

import net.minecraft.tileentity.TileEntity;
import fla.api.world.BlockPos;

public class TileEntityBase extends TileEntity
{
	protected Random rand = new Random();
	
	public BlockPos getBlockPos()
	{
		return new BlockPos(worldObj, xCoord, yCoord, zCoord);
	}
}
