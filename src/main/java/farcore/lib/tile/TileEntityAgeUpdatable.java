package farcore.lib.tile;

import net.minecraft.nbt.NBTTagCompound;

public class TileEntityAgeUpdatable extends TileEntitySyncable
{
	private long lastTick = -1;
	private long nextTick = -1;
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		nextTick = nbt.getLong("tick");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setLong("tick", nextTick);
	}
	
	@Override
	protected boolean init()
	{
		if(nextTick == -1)
		{
			nextTick = lastTick = worldObj.getWorldTime();
		}
		else if(lastTick == -1)
		{
			lastTick = worldObj.getWorldTime();
		}
		return super.init();
	}
	
	@Override
	protected final void updateServer1()
	{
		if(lastTick > worldObj.getWorldTime())
		{
			nextTick = lastTick = worldObj.getWorldTime();
		}
		else
		{
			lastTick = worldObj.getWorldTime();
			if(nextTick < lastTick)
			{
				nextTick += tickTime();
				updateServer2();
			}
		}
	}
	
	protected long tickTime()
	{
		return 1000L;
	}
	
	protected void updateServer2()
	{
		
	}
}