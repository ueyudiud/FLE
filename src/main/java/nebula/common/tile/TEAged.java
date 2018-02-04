/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.common.tile;

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
		this.nextUpdateTick = nbt.getLong("tickUpdate");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setLong("tickUpdate", this.nextUpdateTick);
		return nbt;
	}
	
	@Override
	protected void initServer()
	{
		if (this.nextUpdateTick == Long.MIN_VALUE)
		{
			this.nextUpdateTick = this.world.getWorldTime();
		}
		super.initServer();
	}
	
	@Override
	protected void updateServer()
	{
		if (this.world != null) if (this.world.getWorldTime() >= this.nextUpdateTick)
		{
			this.nextUpdateTick = getNextUpdateTick(this.nextUpdateTick);
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
