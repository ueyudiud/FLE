package flapi.energy;

import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * The electrical energy need to add into electrical net.<br>
 * The event is posted in electrical net.<br>
 * @author ueyudiud
 * @see flapi.energy.IElectricalNet
 */
public abstract class ElectricalEvent extends Event
{
	public final IEleTile tile;
	
	public ElectricalEvent(IEleTile tile)
	{
		this.tile = tile;
	}
	
	public BlockPos getPos()
	{
		return tile.getBlockPos();
	}
	
	/**
	 * Called when load electrical tile into net.
	 * @author ueyudiud
	 *
	 */
	public static class ElectricalLoadEvent extends ElectricalEvent
	{
		public ElectricalLoadEvent(IEleTile tile)
		{
			super(tile);
		}
	}

	public static class ElectricalUnloadEvent extends ElectricalEvent
	{
		public ElectricalUnloadEvent(IEleTile tile)
		{
			super(tile);
		}		
	}
	
	public static class ElectricalChangedEvent extends ElectricalEvent
	{
		public final ChangeType type;
		
		public ElectricalChangedEvent(IEleTile tile, ChangeType type)
		{
			super(tile);
			this.type = type;
		}
		
		public static enum ChangeType
		{
			RESIDENCE,//For residence changed
			STATE;//For point Close/Open, can emmit energy.
		}
	}
}