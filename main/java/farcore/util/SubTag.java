package farcore.util;

import java.util.Collection;
import java.util.HashSet;

import farcore.collection.Register;

public class SubTag implements IDataChecker<ISubTagContainer>
{
	private static final Register<SubTag> subTags = new Register();
	public static final SubTag MATERIAL_ROCK = getNewSubTag("MATERIAL_ROCK");
	public static final SubTag MATERIAL_MINERAL = getNewSubTag("MATERIAL_MINERAL");
	public static final SubTag MATERIAL_SAND = getNewSubTag("MATERIAL_SAND");
	public static final SubTag MATERIAL_DIRT = getNewSubTag("MATERIAL_DIRT");

	public static final SubTag PLANTABLE_DESERT = getNewSubTag("PLANTABLE_DESERT");
	public static final SubTag PLANTABLE_BEACH = getNewSubTag("PLANTABLE_BEACH");
	public static final SubTag PLANTABLE_WATERABLE = getNewSubTag("PLANTABLE_WATERABLE");
	public static final SubTag PLANTABLE_NETHER = getNewSubTag("PLANTABLE_NETHER");
	public static final SubTag PLANTABLE_LAVABLE = getNewSubTag("PLANTABLE_LAVABLE");
	public static final SubTag PLANTABLE_CROP = getNewSubTag("PLANTABLE_CROP");
	public static final SubTag PLANTABLE_CAVE = getNewSubTag("PLANTABLE_CAVE");
	public static final SubTag PLANTABLE_PLAINS = getNewSubTag("PLANTABLE_PLAINS");
	
	public static final SubTag NOT_REGISTER = getNewSubTag("NOT_REGISTER");
	
	public static void addTagsTo(SubTag[] tags, ISubTagContainer...containers)
	{
		for(ISubTagContainer container : containers)
			if(container != null)
				container.add(tags);
	}
	
	public static SubTag getNewSubTag(String aName)
	{
		for(SubTag tag : subTags)
		{
			if (tag.name.equals(aName))
				return tag;
		}
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