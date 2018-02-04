/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.event;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Far land era energy event. Used in each energy net tile entities.<br>
 * This event is handled in
 * {@link farcore.handler.FarCoreEnergyHandler#BUS}.<br>
 * It is suggested add tile to energy net when tile is initialized, and remove
 * when chunk remove or tile is invalidated.<br>
 * 
 * @author ueyudiud
 * @see farcore.handler.FarCoreEnergyHandler
 */
public abstract class EnergyEvent extends Event
{
	private World	world;
	public Object	tile;
	
	public EnergyEvent(TileEntity tile)
	{
		this(tile.getWorld(), tile);
	}
	
	public EnergyEvent(World world, Object tile)
	{
		this.world = world;
		this.tile = tile;
	}
	
	public World world()
	{
		return this.world;
	}
	
	/**
	 * Add tile into energy net, you need do it when tile is loaded (first tick
	 * of update).
	 * 
	 * @author ueyudiud
	 *
	 */
	public static class Add extends EnergyEvent
	{
		public Add(TileEntity tile)
		{
			super(tile);
		}
		
		public Add(World world, Object tile)
		{
			super(world, tile);
		}
	}
	
	/**
	 * Remove tile in energy net, you need do it when tile is unloaded or
	 * removed (invalidated).
	 * 
	 * @author ueyudiud
	 *
	 */
	public static class Remove extends EnergyEvent
	{
		public Remove(TileEntity tile)
		{
			super(tile);
		}
		
		public Remove(World world, Object tile)
		{
			super(world, tile);
		}
	}
	
	/**
	 * Rejoin tile into energy net.
	 * 
	 * @author ueyudiud
	 *
	 */
	public static class Reload extends EnergyEvent
	{
		public Reload(TileEntity tile)
		{
			super(tile);
		}
		
		public Reload(World world, Object tile)
		{
			super(world, tile);
		}
	}
	
	/**
	 * Mark tile with some config changed.
	 * 
	 * @author ueyudiud
	 *
	 */
	public static class Mark extends EnergyEvent
	{
		public Mark(TileEntity tile)
		{
			super(tile);
		}
		
		public Mark(World world, Object tile)
		{
			super(world, tile);
		}
	}
}
