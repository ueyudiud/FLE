package farcore.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;

public abstract class TEUpdatable extends TEBase implements IUpdatePlayerListBox
{
	protected static final int INIT = 0;
	
	/** The last time this tile updated. */
	private long tick = Long.MIN_VALUE;
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		tick = nbt.getLong("lastTick");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setLong("lastTick", tick);
	}
	
	@Override
	public final void update()
	{
		if (!should(INIT))
		{
			init();
			if (tick != Long.MIN_VALUE)
				tick = worldObj.getTotalWorldTime();
			enable(INIT);
		}
		else
		{
			long tick1 = worldObj.getTotalWorldTime();
			long tick2 = tick1 - tick;
			if (tick2 <= 0)
				tick2 = 1;
			tick = tick1;
			update(tick, tick2);
		}
	}
	
	public abstract void init();
	
	public abstract void update(long tick, long tick1);
}