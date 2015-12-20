package flapi.event;

import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;
import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import flapi.te.TEInventory;
import flapi.world.BlockPos;

@Cancelable
public class FLEInventoryUpdateEvent extends Event
{
	private TEInventory tile;
	public IInventory inv;
	
	public FLEInventoryUpdateEvent(TEInventory aTile, IInventory aInv)
	{
		this.tile = aTile;
		this.inv = aInv;
	}
	
	public BlockPos getBlockPos()
	{
		return tile.getBlockPos();
	}
	
	public World getWorldObj()
	{
		return tile.getWorldObj();
	}
}