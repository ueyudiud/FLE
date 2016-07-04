package farcore.alpha.tile;

import net.minecraft.nbt.NBTTagCompound;

public class TEAged extends TESynchronization
{
	protected long nextUpdateTick;
	
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
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setLong("tickUpdate", nextUpdateTick);
	}
	
	@Override
	protected void initServer()
	{
		nextUpdateTick = worldObj.getWorldTime();
		super.initServer();
	}
	
	@Override
	protected void updateServer()
	{
		if(worldObj != null)
		{
			if(worldObj.getWorldTime() >= nextUpdateTick)
			{
				nextUpdateTick = getNextUpdateTick(nextUpdateTick);
				updateServer1();
			}
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