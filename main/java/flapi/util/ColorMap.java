package flapi.util;

public interface ColorMap
{
	public int getColorFromCrood(int x, int y);
	
	public int getGrayLevelFromCrood(int x, int y);
	
	public int getRedLevelFromCrood(int x, int y);
	
	public int getGreenLevelFromCrood(int x, int y);
	
	public int getBlueLevelFromCrood(int x, int y);
}