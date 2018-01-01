/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.world;

import net.minecraft.world.World;

/**
 * Calendar for world.
 * 
 * @author ueyudiud
 *
 */
public interface ICalendar
{
	default long year(World world)
	{
		return year(world.getWorldTime());
	}
	
	long year(long tick);
	
	default long day(World world)
	{
		return day(world.getWorldTime());
	}
	
	long day(long tick);
	
	default long dayInYear(World world)
	{
		return dayInYear(world.getWorldTime());
	}
	
	long dayInYear(long tick);
	
	double dProgressInYear(long tick);
	
	String dateInfo(long tick);
}
