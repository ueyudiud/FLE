package fle.api.soild;

public class SolidTankInfo
{
	public final SolidStack solid;
	public final int capacity;
	
	public SolidTankInfo(SolidStack aStack, int aCapacity)
	{
		solid = aStack;
		capacity = aCapacity;
	}

	public double getProgress()
	{
		return (double) solid.getSize() / (double) capacity;
	}
	
	public boolean haveSolid()
	{
		return solid != null;
	}
}
