package farcore.lib.world;

public interface ICalendar
{
	long year(long tick);
	
	long month(long tick);
	
	long monthInYear(long tick);
	
	long day(long tick);
	
	long dayInYear(long tick);
	
	long dayInMonth(long tick);
	
	String dateInfo(long tick);
}