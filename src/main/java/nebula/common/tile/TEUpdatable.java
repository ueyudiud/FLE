/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.tile;

import nebula.Log;
import nebula.Nebula;
import net.minecraft.util.ITickable;

/**
 * The tile entity can be update.
 * <p>
 * The Minecraft remove <code>updateEntity()</code> after 1.7.10, and use
 * <code>ITickable</code> interface instead. Let your TileEntity extends this
 * class if the TileEntity is <tt>updatable</tt>.
 * 
 * @author ueyudiud
 */
public class TEUpdatable extends TECustomName implements ITickable
{
	/**
	 * Use call for delaying task.
	 * 
	 * @author ueyudiud
	 *
	 */
	protected class TimeMarker
	{
		public final int		duration;
		private final Runnable	runnable;
		
		public TimeMarker(int duration, Runnable runnable)
		{
			this.duration = duration;
			this.runnable = runnable;
		}
		
		private int delay;
		
		public int getDelay()
		{
			return this.delay;
		}
		
		public void resetDelay()
		{
			this.delay = 0;
		}
		
		public void onUpdate()
		{
			if (this.delay++ >= this.duration)
			{
				resetDelay();
				this.runnable.run();
			}
		}
	}
	
	@Override
	public void update()
	{
		try
		{
			this.isUpdating = false;
			updateEntity1();
			this.isUpdating = true;
		}
		catch (Exception exception)
		{
			if (this.world != null)
				if (!this.world.isRemote)
				{
					Log.error("Tile entity throws an exception during ticking in the world, " + "if your enable the option of remove errored tile, this tile will " + "be removed soon. Please report this bug to modder.", exception);
					if (Nebula.debug)
						removeBlock();
					else
						throw exception;
				}
				else
					Log.warn("The tile might disconnect from server caused an exception, " + "if not, please report this bug to modder. If you are playing " + "client world, this exception might cause this world can not " + "load next time, if you can not load the world second time, "
							+ "you can try to remove errored block.", exception);
			else
				Log.warn("Tile entity throws an exception when not ticking in the world. " + "Something might update out of world!", exception);
		}
	}
	
	protected void updateEntity1()
	{
		
	}
}
