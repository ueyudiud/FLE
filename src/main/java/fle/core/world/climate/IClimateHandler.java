package fle.core.world.climate;

public interface IClimateHandler
{
	Climate[] getClimateAt(Climate[] climates, int x, int z, int w, int h, boolean ignoreCache);
	
	Climate getClimateAt(int x, int z);
}