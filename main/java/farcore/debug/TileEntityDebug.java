package farcore.debug;

import farcore.lib.tile.TileEntitySyncable;
import farcore.util.FleLog;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityDebug extends TileEntitySyncable
{
	@Override
	public void writeDescriptionsToNBT1(NBTTagCompound nbt)
	{
		super.writeDescriptionsToNBT1(nbt);
		double rand = Math.random();
		FleLog.coreLogger.info("Debug " + rand);
		nbt.setDouble("r", rand);
	}
	
	@Override
	protected void readDescriptionsFromNBT1(NBTTagCompound nbt)
	{
		double rand = nbt.getDouble("r");
		FleLog.coreLogger.info("Debug" + rand);
	}
}