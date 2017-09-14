/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.world;

import java.util.HashMap;
import java.util.Map;

import farcore.lib.world.instance.CalendarEmpty;
import farcore.lib.world.instance.CalendarSurface;
import net.minecraft.world.World;

/**
 * The calendar handler.
 * @author ueyudiud
 */
public class CalendarHandler
{
	private static final Map<Integer, ICalendar> CALENDARS = new HashMap();
	private static final ICalendar EMPTY = new CalendarEmpty();
	public static final ICalendarWithMonth OVERWORLD;
	
	static
	{
		registerCalendar(0, OVERWORLD = new CalendarSurface());
	}
	
	public static void registerCalendar(int dim, ICalendar calendar)
	{
		CALENDARS.put(dim, calendar);
	}
	
	public static <C extends ICalendar> C getCalendar(World world)
	{
		return (C) CALENDARS.getOrDefault(world.provider.getDimension(), EMPTY);
	}
	
	public static String getDateInfo(World world)
	{
		return getCalendar(world).dateInfo(world.getWorldTime());
	}
}