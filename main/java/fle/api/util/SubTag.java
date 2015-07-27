package fle.api.util;

import java.util.Collection;
import java.util.HashSet;

public class SubTag implements IDataChecker<ISubTagContainer>
{
	private static Register<SubTag> subTags = new Register();
	
	public static SubTag getNewSubTag(String aName)
	{
		for (SubTag tSubTag : subTags)
			if (tSubTag.name.equals(aName))
				return tSubTag;
		return new SubTag(aName);
	}
	
	private final String name;
	public final Collection<ISubTagContainer> relevantTaggedItems = new HashSet(1);  
	
	private SubTag(String aName) 
	{
		name = aName;
		subTags.register(this, aName);
	}
	
	public String toString()
	{
		return name;
	}

	public SubTag addContainerToList(ISubTagContainer... aContainers)
	{
		if (aContainers != null)
			for (ISubTagContainer aContainer : aContainers)
				if ((aContainer != null) && (!relevantTaggedItems.contains(aContainer)))
					relevantTaggedItems.add(aContainer);
		return this;
	}
	  
	public SubTag addTo(ISubTagContainer... aContainers)
	{
	    if (aContainers != null)
	    	for (ISubTagContainer aContainer : aContainers)
	    		if (aContainer != null)
	    			aContainer.add(new SubTag[] { this });
	    return this;
	}

	@Override
	public boolean isTrue(ISubTagContainer aContainer) 
	{
		return aContainer.contain(this);
	}
}