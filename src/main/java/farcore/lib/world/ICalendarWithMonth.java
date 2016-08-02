package farcore.lib.world;

public interface ICalendarWithMonth extends ICalendar
{
	long month(long tick);
	
	long monthInYear(long tick);
	
	long dayInMonth(long tick);
}