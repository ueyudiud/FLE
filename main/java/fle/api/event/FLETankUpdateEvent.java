package fle.api.event;

import net.minecraft.world.World;
import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import fle.api.te.IFluidTanks;
import fle.api.te.TEBase;
import fle.api.world.BlockPos;

@Cancelable
public class FLETankUpdateEvent extends Event
{
	private TEBase tile;
	public IFluidTanks tank;

	public FLETankUpdateEvent(TEBase aTile, IFluidTanks aTank)
	{
		tile = aTile;
		tank = aTank;
	}
	
	public World getWorldObj()
	{
		return tile.getWorldObj();
	}
	
	public BlockPos getBlockPos()
	{
		return tile.getBlockPos();
	}
}