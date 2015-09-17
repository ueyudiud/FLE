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
}
