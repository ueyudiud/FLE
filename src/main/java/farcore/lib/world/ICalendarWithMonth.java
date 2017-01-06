package farcore.lib.world;

/**
 * A calendar with month, such as Overworld (Moon provided the month).
 * @author ueyudiud
 *
 */
public interface ICalendarWithMonth extends ICalendar
{
	long month(long tick);
	
	long monthInYear(long tick);
	
	long dayInMonth(long tick);
}