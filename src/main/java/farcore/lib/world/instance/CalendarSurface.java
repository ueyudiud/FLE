package farcore.lib.world.instance;

import farcore.lib.world.ICalendarWithMonth;
import farcore.util.U;
import farcore.util.U.Maths;

public class CalendarSurface implements ICalendarWithMonth
{
	private static final String[] monthName = {
			"January",
			"February",
			"March",
			"April",
			"May",
			"June",
			"July",
			"August",
			"September",
			"October",
			"November",
			"December"
	};
	private static final long dayT = 24000L;
	private static final long monthT = 192000L;
	private static final long yearT = 2304000L;
	private static final long offsetD = dayT - 18000L;
	private static final long offsetM = monthT - 114000L;
	private static final long offsetY = 3 * monthT - 114000L;

	@Override
	public long year(long tick)
	{
		return 1000L + (tick + offsetY) / yearT;
	}
	
	@Override
	public long month(long tick)
	{
		return (tick + offsetM) / monthT;
	}
	
	@Override
	public long monthInYear(long tick)
	{
		return Maths.mod(tick + offsetY, yearT) / monthT;
	}
	
	@Override
	public long day(long tick)
	{
		return (tick + offsetD) / dayT;
	}
	
	@Override
	public long dayInYear(long tick)
	{
		return Maths.mod(tick + offsetY, yearT) / dayT;
	}
	
	@Override
	public long dayInMonth(long tick)
	{
		return Maths.mod(tick + offsetM, monthT) / dayT;
	}
	
	@Override
	public double dProgressInYear(long tick)
	{
		return (double) (tick % yearT) / yearT;
	}
	
	@Override
	public String dateInfo(long tick)
	{
		return monthName[(int) monthInYear(tick)] + " " + U.Strings.toOrdinalNumber(dayInMonth(tick) + 1) + ", " + year(tick);
	}
}