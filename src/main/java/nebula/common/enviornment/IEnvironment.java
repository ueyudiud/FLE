package nebula.common.enviornment;

import nebula.common.world.ICoord;

public interface IEnvironment
{
	long worldTime();
	
	ICoord coord();
	
	float biomeTemperature();
}