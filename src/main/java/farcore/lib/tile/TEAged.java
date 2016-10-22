package farcore.lib.tile;

import net.minecraft.nbt.NBTTagCompound;

public class TEAged extends TESynchronization
{
	protected long nextUpdateTick = Long.MIN_VALUE;
	
	public TEAged()
	{
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		nextUpdateTick = nbt.getLong("tickUpdate");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setLong("tickUpdate", nextUpdateTick);
		return nbt;
	}
	
	@Override
	protected void initServer()
	{
		if(nextUpdateTick == Long.MIN_VALUE)
		{
			nextUpdateTick = worldObj.getWorldTime();
		}
		super.initServer();
	}
	
	@Override
	protected void updateServer()
	{
		if(worldObj != null)
			if(worldObj.getWorldTime() >= nextUpdateTick)
			{
				nextUpdateTick = getNextUpdateTick(nextUpdateTick);
				updateServer1();
			}
	}
	
	protected long getNextUpdateTick(long thisTick)
	{
		return thisTick + 1000L;
	}
	
	protected void updateServer1()
	{
		
	}
}