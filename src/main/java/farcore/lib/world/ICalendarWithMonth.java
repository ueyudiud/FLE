/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.world;

import net.minecraft.world.World;

/**
 * A calendar with month, such as Overworld (Moon provided the month).
 * 
 * @author ueyudiud
 *
 */
public interface ICalendarWithMonth extends ICalendar
{
	default long month(World world)
	{
		return month(world.getWorldTime());
	}
	
	long month(long tick);
	
	default long monthInYear(World world)
	{
		return monthInYear(world.getWorldTime());
	}
	
	long monthInYear(long tick);
	
	default long dayInMonth(World world)
	{
		return dayInMonth(world.getWorldTime());
	}
	
	long dayInMonth(long tick);
}
