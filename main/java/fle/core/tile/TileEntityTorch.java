package fle.core.tile;

import farcore.enums.Direction;
import farcore.lib.substance.SubstanceWood;
import farcore.lib.tile.TileEntityAgeUpdatable;
import farcore.util.Values;
import fle.core.block.BlockTorch;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityTorch extends TileEntityAgeUpdatable
{
	public static int getWoodBurnTime(SubstanceWood substance)
	{
		return substance.burnEnergyPerUnit * Values.half_mol;
	}
	
	public SubstanceWood substance;
	private boolean isBurning;
	private boolean postBurning;
	public boolean isWet = false;
	public int currentBurnTick = -1;
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		isBurning = nbt.getBoolean("burning");
		currentBurnTick = nbt.getInteger("tick");
		substance = SubstanceWood.getSubstance(nbt.getString("material"));
		isWet = nbt.getBoolean("wet");
	}
	
	@Override
	protected void readDescriptionsFromNBT1(NBTTagCompound nbt)
	{
		super.readDescriptionsFromNBT1(nbt);
		currentBurnTick = nbt.getInteger("t");
		substance = SubstanceWood.getSubstance(nbt.getShort("s"));
		isBurning = nbt.getBoolean("b");
		if(isBurning)
		{
			setLightValue(12);
		}
		else
		{
			setLightValue(0);
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setString("material", substance.getName());
		nbt.setInteger("tick", currentBurnTick);
		nbt.setBoolean("burning", isBurning);
		nbt.setBoolean("wet", isWet);
	}
	
	@Override
	protected void writeDescriptionsToNBT1(NBTTagCompound nbt)
	{
		super.writeDescriptionsToNBT1(nbt);
		nbt.setInteger("t", currentBurnTick);
		nbt.setString("s", substance.getName());
		nbt.setBoolean("b", isBurning);
	}
	
	@Override
	protected boolean init()
	{
		if(super.init())
		{
			if(currentBurnTick == -1)
			{
				currentBurnTick = substance.burnEnergyPerUnit;
			}
			return true;
		}
		return false;
	}
	
	public boolean isBurning()
	{
		return isBurning;
	}
	
	public int getBurnLength(int length)
	{
		return substance == null ? length : 
			(int) ((float) currentBurnTick * length / (float) getWoodBurnTime(substance));
	}
	
	@Override
	protected void updateClient1()
	{
		if(postBurning != isBurning)
		{
			postBurning = isBurning;
		}
	}
	
	@Override
	protected void updateServer2()
	{
		if(worldObj.isRaining() && 
				worldObj.canBlockSeeTheSky(xCoord, yCoord, zCoord))
		{
			isWet = true;
		}
		if(postBurning != isBurning)
		{
			markUpdate();
			postBurning = isBurning;
		}
		if(isBurning)
		{
			if(!checkCanBurning())
			{
				isBurning = false;
				syncToNearby();
				return;
			}
			int length = getBurnLength(12);
			if(currentBurnTick > 0)
			{
				--currentBurnTick;
				if(currentBurnTick == 0)
				{
					removeBlock(0, 0, 0);
					return;
				}
			}
			if(length != getBurnLength(12))
			{
				syncToNearby();
			}
		}
	}
	
	protected boolean checkCanBurning()
	{
		if(isWet)
		{
			return false;
		}
		for(Direction direction : Direction.directions)
		{
			if(worldObj.getBlock(xCoord + direction.x, yCoord + direction.y, zCoord + direction.z)
					.isAir(worldObj, xCoord + direction.x, yCoord + direction.y, zCoord + direction.z))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	protected long tickTime()
	{
		return 200L;
	}

	public void setBurnState(boolean state)
	{
		if(state != isBurning)
		{
			if(isWet && state) return;
			this.isBurning = state;
			syncToNearby();
		}
	}

	
	public ItemStack getDrop(Block block)
	{
		ItemStack ret = new ItemStack(block);
		BlockTorch.setItemStack(ret, substance, currentBurnTick, isWet);
		return ret;
	}
}