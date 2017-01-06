package farcore.lib.world;

/**
 * Calendar for world.
 * @author ueyudiud
 *
 */
public interface ICalendar
{
	long year(long tick);
	
	long day(long tick);
	
	long dayInYear(long tick);
	
	double dProgressInYear(long tick);
	
	String dateInfo(long tick);
}