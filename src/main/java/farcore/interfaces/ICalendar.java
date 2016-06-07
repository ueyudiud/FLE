package farcore.interfaces;

import net.minecraft.world.World;

public interface ICalendar
{
	long getDayTimeL(long tick);
	
	double getDayTimeD(long tick);

	boolean hasMonth();
	
	long getDayInMonthL(long tick);
	
	double getDayInMonthD(long tick);
	
	long getMonthInYearL(long tick);
	
	long getTotalMonthL(long tick);
	
	double getMonthInYearD(long tick);

	long getDayInYearL(long tick);
	
	long getTotalDayL(long tick);
	
	double getDayInYearD(long tick);
	
	long getYearL(long tick);
}