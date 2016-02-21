package flapi.util;

import java.util.Collection;
import java.util.HashSet;

import farcore.collection.Register;

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

	public static final SubTag MATERIAL_heatwire = getNewSubTag("MATERIAL_HEATWIRE");
	
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

	public static final SubTag PART_NUGGETSMALL = getNewSubTag("PART_NUGGETSMALL");
	public static final SubTag PART_NUGGET = getNewSubTag("PART_NUGGET");
	public static final SubTag PART_FOIL = getNewSubTag("PART_FOIL");
	public static final SubTag PART_DUSTTINY = getNewSubTag("PART_DUSTTINY");
	public static final SubTag PART_RING = getNewSubTag("PART_RING");
	public static final SubTag PART_SPRINGSMALL = getNewSubTag("PART_SPRINGSMALL");
	public static final SubTag PART_CHUNK = getNewSubTag("PART_CHUNK");
	public static final SubTag PART_DUSTSMALL = getNewSubTag("PART_DUSTSMALL");
	public static final SubTag PART_SLICE = getNewSubTag("PART_SLICE");
	public static final SubTag PART_SPRING = getNewSubTag("PART_SPRING");
	public static final SubTag PART_CUBESMALL = getNewSubTag("PART_CUBESMALL");
	public static final SubTag PART_STICK = getNewSubTag("PART_STICK");
	public static final SubTag PART_INGOT = getNewSubTag("PART_INGOT");
	public static final SubTag PART_INGOTDOUBLE = getNewSubTag("PART_INGOTDOUBLE");
	public static final SubTag PART_INGOTTRIPLE = getNewSubTag("PART_INGOTTRIPLE");
	public static final SubTag PART_INGOTQUADRUPLE = getNewSubTag("PART_INGOTQUADRUPLE");
	public static final SubTag PART_INGOTQUINTUPLE = getNewSubTag("PART_INGOTQUINTUPLE");
	public static final SubTag PART_INGOTDENSE = getNewSubTag("PART_INGOTDENSE");
	public static final SubTag PART_PLATE = getNewSubTag("PART_PLATE");
	public static final SubTag PART_PLATEDOUBLE = getNewSubTag("PART_PLATEDOUBLE");
	public static final SubTag PART_PLATETRIPLE = getNewSubTag("PART_PLATETRIPLE");
	public static final SubTag PART_PLATEQUADRUPLE = getNewSubTag("PART_PLATEQUADRUPLE");
	public static final SubTag PART_PLATEQUINTUPLE = getNewSubTag("PART_PLATEQUINTUPLE");
	public static final SubTag PART_PLATEDENSE = getNewSubTag("PART_PLATEDENSE");
	public static final SubTag PART_DUST = getNewSubTag("PART_DUST");
	public static final SubTag PART_STICKLONG = getNewSubTag("PART_STICKLONG");
	public static final SubTag PART_CUBE = getNewSubTag("PART_CUBE");
	public static final SubTag PART_PILE = getNewSubTag("PART_PILE");
	public static final SubTag PART_SPRINGLARGE = getNewSubTag("PART_SPRINGLARGE");
	public static final SubTag PART_CUBELARGE = getNewSubTag("PART_CUBELARGE");
	public static final SubTag PART_BLOCK = getNewSubTag("PART_BLOCK");

	public static final SubTag[] PART_STICKTYPE = {PART_STICK, PART_STICKLONG, PART_RING};
	public static final SubTag[] PART_INGOTTYPE = {PART_INGOT, PART_INGOTDOUBLE, PART_INGOTTRIPLE, PART_INGOTQUADRUPLE, PART_INGOTQUINTUPLE, PART_INGOTDENSE};
	public static final SubTag[] PART_PLATETYPE = {PART_PLATE, PART_PLATEDOUBLE, PART_PLATETRIPLE, PART_PLATEQUADRUPLE, PART_PLATEQUINTUPLE, PART_PLATEDENSE};
	public static final SubTag[] PART_DUSTTYPE = {PART_DUST, PART_DUSTSMALL, PART_DUSTTINY};
	
	public static final SubTag ROCK_base_rock = getNewSubTag("ROCK_BASE");
	
	public static final SubTag IC2Item = getNewSubTag("IC2_ITEM");

	public static final IDataChecker<ISubTagContainer> type_tool_metal_tier1 = new IDataChecker.Or<ISubTagContainer>(SubTag.TOOL_metal_tier1);
	public static final IDataChecker<ISubTagContainer> type_tool_metal_tier0 = new IDataChecker.Or<ISubTagContainer>(SubTag.TOOL_metal_tier0, SubTag.TOOL_metal_tier1);
	
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
		return aContainer == null ? false : aContainer.contain(this);
	}
}