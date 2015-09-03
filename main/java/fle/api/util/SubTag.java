package fle.api.util;

import java.util.Collection;
import java.util.HashSet;

public class SubTag implements IDataChecker<ISubTagContainer>
{
	private static final Register<SubTag> subTags = new Register();

	public static final SubTag ATOM_metal = getNewSubTag("ATOM_METAL");
	public static final SubTag ATOM_nonmetal = getNewSubTag("ATOM_NONMETAL");

	public static final SubTag ATOM_gas = getNewSubTag("ATOM_GAS");
	public static final SubTag ATOM_gas_low_temp = getNewSubTag("ATOM_GAS_LOW_TEMP");
	public static final SubTag ATOM_liquid = getNewSubTag("ATOM_LIQUID");
	
	public static final SubTag ORE_TYPE_alloy = getNewSubTag("ORE_TYPE_ALLOY");
	public static final SubTag ORE_TYPE_gem = getNewSubTag("ORE_TYPE_GEM");
	public static final SubTag ORE_TYPE_salt = getNewSubTag("ORE_TYPE_SALT");
	public static final SubTag ORE_TYPE_default = getNewSubTag("ORE_TYPE_DEFAULT");
	public static final SubTag ORE_TYPE_crush = getNewSubTag("ORE_TYPE_CRUSH");
	public static final SubTag ORE_TYPE_half = getNewSubTag("ORE_TYPE_HALF");
	
	public static final SubTag ORE_native = getNewSubTag("ORE_NATIVE");
	public static final SubTag ORE_oxide = getNewSubTag("ORE_OXIDE");
	public static final SubTag ORE_hydroxide = getNewSubTag("ORE_HYDROXIDE");
	public static final SubTag ORE_sulfide = getNewSubTag("ORE_SULFIDE");
	public static final SubTag ORE_gem = ORE_TYPE_gem;//Remove old tag of gem.
	public static final SubTag ORE_gem_only = getNewSubTag("ORE_GEM_ONLY");
	
	public static final SubTag CRAFTING_cold_wought = getNewSubTag("CRAFTING_COLD_WOUGHT");
	
	public static final SubTag TOOL_wood = getNewSubTag("TOOL_WOOD");
	public static final SubTag TOOL_stone = getNewSubTag("TOOL_STONE");
	public static final SubTag TOOL_flint = getNewSubTag("TOOL_FLINT");
	public static final SubTag TOOL_stone_real = getNewSubTag("TOOL_STONE_REAL");
	public static final SubTag TOOL_metal = getNewSubTag("TOOL_METAL");
	public static final SubTag TOOL_fireable = getNewSubTag("TOOL_FIREABLE");
	
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