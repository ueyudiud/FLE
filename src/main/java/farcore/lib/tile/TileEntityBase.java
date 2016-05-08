package farcore.lib.tile;

import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityBase extends TileEntity
{
	/**
	 * To know is this tile is initialized (For client in common). 
	 */
	private boolean init = false;
	
	protected int light = 0;
	/**
	 * Tile random.
	 */
	protected Random rand = new Random();
	
	protected void markReinit()
	{
		init = false;
	}
	
	protected void markInit()
	{
		init = true;
	}

	public void onBlockBreak(int meta)
	{
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
	}
	
	@Override
	public final void updateEntity()
	{
		if(!init)
		{
			if(worldObj.isRemote)
			{
				initClient();
				if(!shouldClientNeedSyncable())
				{
					init = true;
				}
			}
			else
			{
				if(init())
				{
					init = true;
				}
			}
		}
		else
		{
			updateGeneral();
			if(worldObj.isRemote)
			{
				updateClient();
			}
			else
			{
				updateServer();
			}
		}
	}
	
	protected boolean init()
	{
		return true;
	}
	
	protected void initClient()
	{
		
	}
	
	protected boolean shouldClientNeedSyncable()
	{
		return false;
	}
	
	protected void updateGeneral()
	{
		
	}
	
	protected void updateServer()
	{
		
	}
	
	protected void updateClient()
	{
		
	}

	public void markUpdate()
	{
		
	}
	
	public int getLightValue()
	{
		return light;
	}
	
	//Block control start.
	public void removeBlock(int xOffset, int yOffset, int zOffset)
	{
		worldObj.removeTileEntity(xCoord + xOffset, yCoord + yOffset, zCoord + zOffset);
		worldObj.setBlockToAir(xCoord + xOffset, yCoord + yOffset, zCoord + zOffset);
	}
	
	public void setLightValue(int value)
	{
		if(light != value)
		{
			light = value;
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
	}
}