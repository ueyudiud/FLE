/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.world.instance;

import farcore.lib.world.ICalendar;

public class CalendarEmpty implements ICalendar
{
	@Override
	public long year(long tick)
	{
		return 1000;
	}
	
	@Override
	public long day(long tick)
	{
		return 0;
	}
	
	@Override
	public long dayInYear(long tick)
	{
		return 0;
	}
	
	@Override
	public double dProgressInYear(long tick)
	{
		return 0;
	}
	
	@Override
	public String dateInfo(long tick)
	{
		return "No date";
	}
}
