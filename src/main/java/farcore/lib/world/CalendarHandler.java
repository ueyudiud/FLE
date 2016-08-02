package farcore.lib.world;

import java.util.HashMap;
import java.util.Map;

import farcore.lib.world.instance.CalendarEmpty;
import farcore.lib.world.instance.CalendarSurface;
import net.minecraft.world.World;

public class CalendarHandler
{
	private static final Map<Integer, ICalendar> calendars = new HashMap();
	private static final ICalendar empty = new CalendarEmpty();
	public static final ICalendarWithMonth overworld;
	
	static
	{
		registerCalendar(0, overworld = new CalendarSurface());
	}
	
	public static void registerCalendar(int dim, ICalendar calendar)
	{
		calendars.put(dim, calendar);
	}
	
	public static ICalendar getCalendar(World world)
	{
		return calendars.getOrDefault(world.provider.getDimension(), empty);
	}
	
	public static String getDateInfo(World world)
	{
		return getCalendar(world).dateInfo(world.getWorldTime());
	}
}