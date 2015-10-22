package fle.api.util;

import java.util.Collection;
import java.util.HashSet;

public class SubTag implements IDataChecker<ISubTagContainer>
{
	private static final Register<SubTag> subTags = new Register();

	public static final SubTag ATOM_metal = getNewSubTag("ATOM_METAL");
	public static final SubTag ATOM_nonmetal = getNewSubTag("ATOM_NONMETAL");

	public static final SubTag ATOM_soild = getNewSubTag("ATOM_SOILD");
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
	public static final SubTag TOOL_bone = getNewSubTag("TOOL_BONE");
	public static final SubTag TOOL_stone_real = getNewSubTag("TOOL_STONE_REAL");
	public static final SubTag TOOL_metal_tier0 = getNewSubTag("TOOL_METAL_TIER0");
	public static final SubTag TOOL_metal_tier1 = getNewSubTag("TOOL_METAL_TIER1");
	public static final SubTag TOOL_metal = TOOL_metal_tier0;//Remove old tag of tool material.
	public static final SubTag TOOL_fireable = getNewSubTag("TOOL_FIREABLE");

	public static final SubTag BIOME_wet = getNewSubTag("BIOME_WET");
	public static final SubTag BIOME_w_mid = getNewSubTag("BIOME_W_MID");
	public static final SubTag BIOME_dry = getNewSubTag("BIOME_DRY");
	public static final SubTag BIOME_hot = getNewSubTag("BIOME_HOT");
	public static final SubTag BIOME_warm = getNewSubTag("BIOME_WARM");
	public static final SubTag BIOME_temperate = getNewSubTag("BIOME_TEMPERATE");
	public static final SubTag BIOME_cold = getNewSubTag("BIOME_COLD");
	public static final SubTag BIOME_freeze = getNewSubTag("BIOME_FREEZE");
	public static final SubTag BIOME_ocean = getNewSubTag("BIOME_OCEAN");
	public static final SubTag BIOME_island = getNewSubTag("BIOME_ISLAND");
	public static final SubTag BIOME_river = getNewSubTag("BIOME_RIVER");
	public static final SubTag BIOME_nether = getNewSubTag("BIOME_NETHER");
	public static final SubTag BIOME_sky = getNewSubTag("BIOME_SKY");

	public static final SubTag ROCK_base_rock = getNewSubTag("ROCK_BASE");
	
	public static final SubTag IC2Item = getNewSubTag("IC2_ITEM");

	public static final IDataChecker<ISubTagContainer> type_tool_metal_tier1 = new IDataChecker.Or<ISubTagContainer>(SubTag.TOOL_metal_tier1);
	public static final IDataChecker<ISubTagContainer> type_tool_metal_tier0 = new IDataChecker.Or<ISubTagContainer>(SubTag.TOOL_metal_tier0, SubTag.TOOL_metal_tier1);
	
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