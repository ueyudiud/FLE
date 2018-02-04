/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.common.world;

import net.minecraft.world.World;

/**
 * @author ueyudiud
 */
public class WorldTask
{
	public final World world;
	protected long settled;
	protected long delay;
	
	public WorldTask(World world, long delay)
	{
		this.world = world;
		this.delay = delay;
		this.settled = world.getTotalWorldTime();
	}
	
	public boolean handleTask()
	{
		return this.world.getTotalWorldTime() >= this.settled + this.delay;
	}
}
