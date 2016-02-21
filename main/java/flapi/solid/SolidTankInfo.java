package flapi.solid;

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
		return (double) solid.size() / (double) capacity;
	}
	
	public boolean haveSolid()
	{
		return solid != null;
	}
}