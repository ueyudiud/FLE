package farcore.util;

import java.util.HashMap;
import java.util.Map;

import farcore.interfaces.ICalendar;
import net.minecraft.world.World;

public class CalendarHandler
{
	private static final ICalendar instance = new ICalendar()
	{
		public boolean hasMonth() {return false;}
		public long getYearL(long tick) {return 1000;}
		public long getTotalMonthL(long tick) {return 1;}
		public long getTotalDayL(long tick) {return 1;}
		public long getMonthInYearL(long tick) {return 1;}
		public double getMonthInYearD(long tick) {return 1;}
		public long getDayTimeL(long tick) {return 1;}
		public double getDayTimeD(long tick) {return 1;}
		public long getDayInYearL(long tick) {return 1;}
		public double getDayInYearD(long tick) {return 1;}
		public long getDayInMonthL(long tick) {return 1;}
		public double getDayInMonthD(long tick) {return 1;}
	};
	private static final Map<Integer, ICalendar> register = new HashMap();
	
	public static void init()
	{
		addCalendar(0, "earth", new ICalendar()
		{
			public boolean hasMonth() {return true;}
			public long getYearL(long tick) {return Values.earth_year_start + (tick + Values.earth_tick_start) / Values.tick_per_year;}
			public long getTotalMonthL(long tick) {return (tick + Values.earth_tick_start) / Values.tick_per_month;}
			public long getTotalDayL(long tick) {return (tick + Values.earth_tick_start) / Values.tick_per_day;}
			public long getMonthInYearL(long tick) {return getTotalMonthL(tick) % Values.month_per_year;}
			public double getMonthInYearD(long tick) {return (double) getMonthInYearL(tick) / (double) Values.month_per_year;}
			public long getDayTimeL(long tick) {return (tick + Values.earth_tick_start) % Values.tick_per_day;}
			public double getDayTimeD(long tick) {return (double) getDayTimeL(tick) / (double) Values.tick_per_day;}
			public long getDayInYearL(long tick) {return getTotalDayL(tick) % Values.day_per_year;}
			public double getDayInYearD(long tick) {return (double) getDayInYearL(tick) / (double) Values.day_per_year;}
			public long getDayInMonthL(long tick) {return getTotalDayL(tick) % Values.day_per_month;}
			public double getDayInMonthD(long tick) {return (double) getDayInMonthL(tick) / (double) Values.day_per_month;}
		});
		addCalendar(-1, "nether", new ICalendar()
		{
			public boolean hasMonth() {return false;}
			public long getYearL(long tick) {return 1000;}
			public long getTotalMonthL(long tick) {return 1;}
			public long getTotalDayL(long tick) {return 1;}
			public long getMonthInYearL(long tick) {return 1;}
			public double getMonthInYearD(long tick) {return 1;}
			public long getDayTimeL(long tick) {return 1;}
			public double getDayTimeD(long tick) {return 1;}
			public long getDayInYearL(long tick) {return 1;}
			public double getDayInYearD(long tick) {return 1;}
			public long getDayInMonthL(long tick) {return 1;}
			public double getDayInMonthD(long tick) {return 1;}
		});
		addCalendar(1, "end", new ICalendar()
		{
			public boolean hasMonth() {return false;}
			public long getYearL(long tick) {return 1000;}
			public long getTotalMonthL(long tick) {return 1;}
			public long getTotalDayL(long tick) {return 1;}
			public long getMonthInYearL(long tick) {return 1;}
			public double getMonthInYearD(long tick) {return 1;}
			public long getDayTimeL(long tick) {return 1;}
			public double getDayTimeD(long tick) {return 1;}
			public long getDayInYearL(long tick) {return 1;}
			public double getDayInYearD(long tick) {return 1;}
			public long getDayInMonthL(long tick) {return 1;}
			public double getDayInMonthD(long tick) {return 1;}
		});
	}
	
	public static void addCalendar(int dimID, String calendarName, ICalendar calendar)
	{
		register.put(Integer.valueOf(dimID), calendar);
	}
	
	public static ICalendar getCalendar(World world)
	{
		return register.getOrDefault(Integer.valueOf(world.provider.dimensionId), instance);
	}
	
	public static ICalendar getCalendar(int dim)
	{
		return register.getOrDefault(Integer.valueOf(dim), instance);
	}
	
	public static void removeCalendar(int dim)
	{
		register.remove(Integer.valueOf(dim));
	}
}