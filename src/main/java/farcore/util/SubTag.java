package farcore.util;

import java.util.Collection;
import java.util.HashSet;

import farcore.interfaces.ISubTagContainer;
import farcore.lib.collection.Register;

public class SubTag implements IDataChecker<ISubTagContainer>
{
	private static final Register<SubTag> subTags = new Register();

	public static final SubTag TOOL_wood = getNewSubTag("TOOL_WOOD");
	public static final SubTag TOOL_stone = getNewSubTag("TOOL_STONE");
	public static final SubTag TOOL_flint = getNewSubTag("TOOL_FLINT");
	public static final SubTag TOOL_bone = getNewSubTag("TOOL_BONE");
	public static final SubTag TOOL_stone_real = getNewSubTag("TOOL_STONE_REAL");
	public static final SubTag TOOL_metal_tier0 = getNewSubTag("TOOL_METAL_TIER0");
	public static final SubTag TOOL_metal_tier1 = getNewSubTag("TOOL_METAL_TIER1");
	public static final SubTag TOOL_metal = TOOL_metal_tier0;//Remove old tag of tool material.
	public static final SubTag TOOL_fireable = getNewSubTag("TOOL_FIREABLE");

	public static final SubTag PlantType_Plains = getNewSubTag("Plains");
	
	public static void addTagsTo(SubTag[] tags, ISubTagContainer...containers)
	{
		for(ISubTagContainer container : containers)
			if(container != null)
				container.add(tags);
	}
	
	public static SubTag getNewSubTag(String aName)
	{
		for (SubTag tSubTag : subTags)
			if (tSubTag.name.equals(aName))
				return tSubTag;
		return new SubTag(aName);
	}
	
	private final String name;
	public final Collection<ISubTagContainer> relevantTaggedItems = new HashSet(1);  
	
	private SubTag(String name) 
	{
		this.name = name;
		subTags.register(name, this);
	}
	
	public String name()
	{
		return name;
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