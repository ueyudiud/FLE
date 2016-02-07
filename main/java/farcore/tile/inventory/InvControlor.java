package farcore.tile.inventory;

import java.util.Arrays;

public class InvControlor
{
	final String name;
	final int size;
	final InvLocate[] locates;
	InvLocate[] locatePosition;

	public InvControlor(String name, int size)
	{
		this(name, new InvLocate('d', size));
	}
	public InvControlor(String name, int width, int height)
	{
		this(name, new InvLocate('d', width, height));
	}
	public InvControlor(String name, InvLocate...locates)
	{
		this.name = name;
		this.locates = locates;
		int s = 0;
		for(int i = 0; i < locates.length; ++i)
		{
			locates[i].locateID = i;
			int s1 = locates[i].size();
			locates[i].startID = s;
			s += s1;
			locates[i].endID = s;
		}
		this.size = s;
		locatePosition = new InvLocate[size];
		for(InvLocate locate : locates)
		{
			Arrays.fill(locatePosition, locate.startID, locate.endID, locate);
		}
	}

	public InvLocate getLocate(char chr)
	{
		for(InvLocate locate : locates)
		{
			if(locate.invChar == chr)
			{
				return locate;
			}
		}
		return null;
	}
	
	public InvLocate getLocate(int slotID)
	{
		return locatePosition[slotID];
	}
}