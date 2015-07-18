package fla.core.tileentity;

import fla.core.Fla;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityFirewood extends TileEntityBase
{
	private boolean isBurning1;
	private boolean isBurning2;
	private int carbonLevel;
	private int carbonContain = 1000;
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		isBurning1 = nbt.getBoolean("IsSmolder");
		isBurning2 = nbt.getBoolean("IsBurning");
		carbonLevel = nbt.getShort("CarbonLevel");
		carbonContain = nbt.getShort("CarbonContain");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setBoolean("IsSmolder", isBurning1);
		nbt.setBoolean("IsBurning", isBurning2);
		nbt.setShort("CarbonLevel", (short) carbonLevel);
		nbt.setShort("CarbonContain", (short) carbonContain);
	}
	
	@Override
	public void updateEntity() 
	{
		boolean flag = false;
		if(getBlockPos().toPos(ForgeDirection.UP).getBlock() == Blocks.fire && rand.nextInt(8) == 0)
		{
			isBurning1 = true;
			flag = true;
		}
		if(!canABurning()) isBurning1 = false;
		if(canBBurning() && rand.nextInt(32) == 0) isBurning2 = true;
		if(!canBBurning()) isBurning2 = false;
		if(isBurning1)
		{
			if(carbonLevel < 1000)
			{
				++carbonLevel;
			}
			else if(rand.nextInt(16) == 0)
			{
				isBurning1 = false;
				flag = true;
			}
		}
		if(isBurning2)
		{
			--carbonContain;
			for(int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; ++i)
			{
				ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[i];
				Fla.fla.hm.emmitHeat(worldObj, getBlockPos(), dir, carbonLevel / 10);
			}
			if(carbonContain == 0)
			{
				worldObj.setBlockToAir(xCoord, yCoord, zCoord);
				worldObj.removeTileEntity(xCoord, yCoord, zCoord);
			}
			if(worldObj.getBlock(xCoord, yCoord + 1, zCoord) == Blocks.air)
			{
				worldObj.setBlock(xCoord, yCoord + 1, zCoord, Blocks.fire);
			}
			else
			{
				if(worldObj.getBlock(xCoord, yCoord + 1, zCoord).isFlammable(worldObj, xCoord, yCoord, zCoord, ForgeDirection.UP) && rand.nextInt(16) == 0)
				{
					if(worldObj.getTileEntity(xCoord, yCoord + 1, zCoord) != null)
					{
						worldObj.removeTileEntity(xCoord, yCoord + 1, zCoord);
					}
					worldObj.setBlock(xCoord, yCoord + 1, zCoord, Blocks.fire);
				}
			}
		}
		if(flag)
		{
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, isBurning1 ? 1 : 0, 2);
		}
	}
	
	public boolean canABurning()
	{
		int blockCount = 0;
		for(int i = -1; i < 2; ++i)
			for(int j = -1; j < 2; ++j)
				for(int k = -1; k < 2; ++k)
				{
					if(!worldObj.getBlock(xCoord + i, yCoord + j, zCoord + k).isAir(worldObj, xCoord + i, yCoord + j, zCoord + k))
					{
						++blockCount;
					}
				}
		return blockCount != 27;
	}
	
	public boolean canBBurning()
	{
		int blockCount = 0;
		for(int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; ++i)
		{
			ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[i];
			if(!getBlockPos().toPos(dir).getBlock().isAir(worldObj, xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ))
			{
				++blockCount;
			}
		}
		return blockCount != 6 && isBurning1;
	}

}