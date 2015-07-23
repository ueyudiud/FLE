package fla.core.tileentity;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import fla.api.world.BlockPos;
import fla.core.Fla;
import fla.core.FlaBlocks;
import fla.core.tileentity.base.TileEntityBase;

public class TileEntityFirewood extends TileEntityBase
{
	private boolean isCharcoal;
	private boolean isBurning1;
	private boolean isBurning2;
	private int carbonLevel;
	private int carbonContain;
	
	public TileEntityFirewood(boolean isCoal)
	{
		isCharcoal = isCoal;
		carbonContain = isCoal ? 4000 : 1000;
	}
	public TileEntityFirewood() 
	{
		isCharcoal = false;
		carbonContain = 1000;
	}
	
	public int getCharcoalContain()
	{
		return (int) Math.floor(carbonContain / 1000);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		isBurning1 = nbt.getBoolean("IsSmolder");
		isBurning2 = nbt.getBoolean("IsBurning");
		isCharcoal = nbt.getBoolean("IsCharcoal");
		carbonLevel = nbt.getShort("CarbonLevel");
		carbonContain = nbt.getShort("CarbonContain");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setBoolean("IsSmolder", isBurning1);
		nbt.setBoolean("IsBurning", isBurning2);
		nbt.setBoolean("IsCharcoal", isCharcoal);
		nbt.setShort("CarbonLevel", (short) carbonLevel);
		nbt.setShort("CarbonContain", (short) carbonContain);
	}
	
	@Override
	public void updateEntity() 
	{
		boolean flag = false;
		boolean flag1 = false;
		if(!worldObj.isRemote)
		{
			if(updateFirewoodBurning())
			{
				flag = true;
			}
		}
		if(isCharcoal)
		{
			if(isBurning2)
			{
				burningFirewood();
			}
		}
		else
		{
			if(isBurning1)
			{
				if(carbonLevel < 6000)
				{
					++carbonLevel;
				}
				else if(!worldObj.isRemote)
				{
					isCharcoal = true;
					isBurning1 = false;
					isBurning2 = false;
					flag = flag1 = true;
				}
			}
			if(isBurning2)
			{
				burningFirewood();
			}
		}
		if(flag)
		{
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, isBurning1 ? 1 : 0, 2);
			if(flag1)
			{
				worldObj.setBlock(xCoord, yCoord, zCoord, FlaBlocks.charcoal, 0, 2);
				carbonContain = 4000;
				carbonLevel = -1;
			}
		}
	}
	
	private boolean updateFirewoodBurning()
	{
		if(getBlockPos().toPos(ForgeDirection.UP).getBlock() == Blocks.fire && rand.nextInt(8) == 0)
		{
			isBurning1 = true;
			return true;
		}
		else if(!isBurning1)
		{
			for(int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; ++i)
			{
				BlockPos pos = getBlockPos().toPos(ForgeDirection.VALID_DIRECTIONS[i]);
				if(pos.getBlock().isBurning(worldObj, pos.x, pos.y, pos.z))
				{
					isBurning1 = true;
					return true;
				}
			}
		}
		if(!canABurning()) 
			isBurning1 = false;
		if(canBBurning() && rand.nextInt(32) == 0) 
			isBurning2 = true;
		if(!canBBurning()) 
			isBurning2 = false;
		 return false;
	}
	
	private void burningFirewood()
	{
		--carbonContain;
		if(carbonContain <= 0)
		{
			isBurning1 = false;
			worldObj.removeTileEntity(xCoord, yCoord, zCoord);
			worldObj.setBlock(xCoord, yCoord, zCoord, FlaBlocks.plantAsh);
			return;
		}
		for(int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; ++i)
		{
			ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[i];
			Fla.fla.hm.emmitHeat(worldObj, getBlockPos(), dir, isCharcoal ? 100 : 20);
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
		if(worldObj.isRaining())
		{
			boolean flag = true;
			for(int i = yCoord + 1; i < 256; ++i)
			{
				if(!worldObj.getBlock(xCoord, i, zCoord).isSideSolid(worldObj, xCoord, i, zCoord, ForgeDirection.UP))
				{
					flag = false;
				}
			}
			if(flag) return false;
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
	
	public boolean isBurning() 
	{
		return isBurning2;
	}
}