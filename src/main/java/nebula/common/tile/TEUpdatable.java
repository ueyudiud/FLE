package nebula.common.tile;

import nebula.Log;
import nebula.Nebula;
import net.minecraft.util.ITickable;

public class TEUpdatable extends TECustomName implements ITickable
{
	/**
	 * Use call for delaying task.
	 * @author ueyudiud
	 *
	 */
	protected class TimeMarker
	{
		public final int duration;
		private final Runnable runnable;
		
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
		
		public void onUpdate()
		{
			if (this.delay++ >= this.duration)
			{
				this.delay = 0;
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
		catch(Exception exception)
		{
			if(this.world != null)
				if(!this.world.isRemote)
				{
					Log.error("Tile entity throws an exception during ticking in the world, "
							+ "if your enable the option of remove errored tile, this tile will "
							+ "be removed soon. Please report this bug to modder.", exception);
					if(Nebula.debug)
						removeBlock();
					else
						throw exception;
				}
				else
					Log.warn("The tile might disconnect from server caused an exception, "
							+ "if not, please report this bug to modder. If you are playing "
							+ "client world, this exception might cause this world can not "
							+ "load next time, if you can not load the world second time, "
							+ "you can try to remove errored block.", exception);
			else
				Log.warn("Tile entity throws an exception when not ticking in the world. "
						+ "Something might update out of world!", exception);
		}
	}
	
	protected void updateEntity1()
	{
		
	}
}