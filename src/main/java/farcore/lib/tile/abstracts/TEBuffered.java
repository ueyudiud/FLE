package farcore.lib.tile.abstracts;

import net.minecraft.nbt.NBTTagCompound;

public class TEBuffered extends TEUpdatable
{
	public long timer = Long.MIN_VALUE;
	public long lastActived = Long.MIN_VALUE;

	public TEBuffered()
	{

	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		timer = nbt.getLong("timer");
		lastActived = nbt.getLong("lt");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setLong("timer", timer);
		nbt.setLong("lt", lastActived);
		return nbt;
	}

	@Override
	protected final void updateEntity1()
	{
		if(timer == Long.MIN_VALUE)
			timer = worldObj.getTotalWorldTime();
		updateEntity2();
		lastActived = worldObj.getTotalWorldTime();
	}

	protected void updateEntity2()
	{

	}
}