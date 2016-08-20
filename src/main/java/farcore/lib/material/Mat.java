package farcore.lib.material;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableList;

import farcore.lib.block.instance.BlockLeaves;
import farcore.lib.block.instance.BlockLeavesCore;
import farcore.lib.block.instance.BlockLogArtificial;
import farcore.lib.block.instance.BlockLogNatural;
import farcore.lib.block.instance.BlockRock;
import farcore.lib.collection.Register;
import farcore.lib.crop.ICrop;
import farcore.lib.material.ore.IOreProperty;
import farcore.lib.plant.IPlant;
import farcore.lib.tree.ITree;
import farcore.lib.util.IDataChecker;
import farcore.lib.util.IRegisteredNameable;
import farcore.lib.util.ISubTagContainer;
import farcore.lib.util.LanguageManager;
import farcore.lib.util.SubTag;
import farcore.util.U;
import farcore.util.U.OreDict;
import net.minecraft.block.Block;

public class Mat implements ISubTagContainer, IRegisteredNameable, Comparable<Mat>
{
	public static final Register<Mat> register = new Register(32768);
	
	private static final Map<IDataChecker<ISubTagContainer>, List<Mat>> materials = new HashMap();

	public static List<Mat> filt(IDataChecker<ISubTagContainer> filter)
	{
		return filt(filter, false);
	}
	public static List<Mat> filt(IDataChecker<ISubTagContainer> filter, boolean alwaysInit)
	{
		if(!materials.containsKey(filter) || alwaysInit)
		{
			ImmutableList.Builder<Mat> list = ImmutableList.builder();
			for(Mat material : register)
			{
				if(filter.isTrue(material))
				{
					list.add(material);
				}
			}
			List<Mat> ret = list.build();
			if(!alwaysInit)
			{
				materials.put(filter, ret);
			}
			return ret;
		}
		else
			return materials.get(filter);
	}

	public final String modid;
	public final String name;
	public final String oreDictName;
	public final String localName;
	public final int id;
	public String chemicalFormula = "?";
	public short[] RGBa = {255, 255, 255, 255};
	public int RGB = 0xFFFFFF;
	public float heatCap;
	public float thermalConduct;
	public float maxSpeed;
	public float maxTorque;
	public float dielectricConstant;
	public float electrialResistance;
	public float redstoneResistance;
	//Tool configuration.
	public boolean canMakeTool = false;
	public int toolMaxUse = 1;
	public int toolHarvestLevel;
	public float toolHardness = 1.0F;
	public float toolDamageToEntity;
	public int toolEnchantability;
	public float toolBrittleness;
	//Block configuration.
	public boolean isRock = false;
	public boolean isSand = false;
	public boolean isDirt = false;
	public String blockHarvestTool;
	public int blockHarvestLevel;
	public float blockExplosionResistance;
	public float blockHardness;
	//Wood matter configuration.
	public boolean isWood = false;
	public float woodHardness;//Useless prop
	public float ashcontent;
	public float woodBurnHeat;
	public boolean hasTree = false;
	public ITree tree;
	/** This two block is for tree generation. */
	public Block log;
	/** The log which is already cut off from tree. */
	public Block logArt;
	public Block leaves;
	//Rock extra configuration.
	public Block rock;
	public float minDetHeatForExplosion;
	//Ore configuration.
	public IOreProperty oreProperty = IOreProperty.property;
	//Plant configuration
	/** The plant grown in wild. */
	public IPlant plant;
	public Block wildPlantBlock;
	//Crop configuration.
	public boolean isCrop = false;
	public ICrop crop;
	//Multi item configuration.
	public IItemMatProp itemProp;
	
	private Set<SubTag> subTags = new HashSet();
	
	public Mat(int id, String name, String oreDict, String localized)
	{
		this(id, U.Mod.getActiveModID(), name, oreDict, localized);
	}
	public Mat(int id, String modid, String name, String oreDict, String localized)
	{
		this(id, true, modid, name, oreDict, localized);
	}
	public Mat(int id, boolean register, String modid, String name, String oreDict, String localized)
	{
		this.id = id;
		this.modid = modid;
		this.name = name;
		oreDictName = oreDict;
		localName = localized;
		LanguageManager.registerLocal("material." + name + ".name", localized);
		if(register)
		{
			Mat.register.register(id, name, this);
		}
	}
	
	@Override
	public final String getRegisteredName()
	{
		return name;
	}
	
	public String getLocalName()
	{
		return LanguageManager.translateToLocal("material." + name + ".name");
	}
	
	public Mat setRGBa(int colorIndex)
	{
		RGBa[0] = (short) ((colorIndex >> 24)       );
		RGBa[1] = (short) ((colorIndex >> 16) & 0xFF);
		RGBa[2] = (short) ((colorIndex >> 8 ) & 0xFF);
		RGBa[3] = (short) ((colorIndex      ) & 0xFF);
		RGB = colorIndex >> 8;
		return this;
	}
	
	public Mat setRGBa(short[] colorIndex)
	{
		RGBa = colorIndex;
		RGB = colorIndex[0] << 16 | colorIndex[1] << 8 | colorIndex[2];
		return this;
	}

	public Mat setChemicalFormula(String chemicalFormula)
	{
		this.chemicalFormula = chemicalFormula;
		return this;
	}
	
	public Mat setGeneralProp(float heatCap, float thermalConduct, float maxSpeed, float maxTorque, float dielectricConstant, float electrialResistance, float redstoneResistance)
	{
		this.heatCap = heatCap;
		this.thermalConduct = thermalConduct;
		this.dielectricConstant = dielectricConstant;
		this.maxSpeed = maxSpeed;
		this.maxTorque = maxTorque;
		this.electrialResistance = electrialResistance;
		this.redstoneResistance = redstoneResistance;
		return this;
	}
	
	public Mat setToolable(int harvestLevel, int maxUse, float hardness, float brittleness, float dVE, int enchantability)
	{
		canMakeTool = true;
		toolHarvestLevel = harvestLevel;
		toolMaxUse = maxUse;
		toolHardness = hardness;
		toolBrittleness = brittleness;
		toolDamageToEntity = dVE;
		toolEnchantability = enchantability;
		add(SubTag.TOOL);
		return this;
	}

	public Mat setOreProperty(int harvestLevel, float hardness, float resistance)
	{
		return setOreProperty(harvestLevel, hardness, resistance, IOreProperty.property);
	}
	
	public Mat setOreProperty(int harvestLevel, float hardness, float resistance, IOreProperty oreProperty)
	{
		this.oreProperty = oreProperty;
		blockHarvestLevel = harvestLevel;
		blockHardness = hardness;
		blockExplosionResistance = resistance;
		add(SubTag.ORE);
		return this;
	}
	
	public Mat setWood(float woodHardness, float ashcontent, float woodBurnHeat)
	{
		isWood = true;
		blockHardness = 1.5F + woodHardness / 4F;
		blockExplosionResistance = 0.4F + woodHardness / 8F;
		blockHarvestLevel = 1;
		blockHarvestTool = "axe";
		this.woodHardness = woodHardness;
		this.ashcontent = ashcontent;
		this.woodBurnHeat = woodBurnHeat;
		add(SubTag.WOOD);
		return this;
	}

	public Mat setTree(ITree tree)
	{
		return setTree(tree, true);
	}
	
	/**
	 * Set tree information of material.
	 * @param tree The tree information.
	 * @param createBlock False to prevent add log and leaves block, you
	 * may have other block to added, this option is only input false
	 * in VOID material in FarCore.
	 * @return
	 */
	public Mat setTree(ITree tree, boolean createBlock)
	{
		hasTree = true;
		this.tree = tree;
		if(createBlock)
		{
			BlockLogNatural logNatural = BlockLogNatural.create(this);
			BlockLogArtificial logArtificial = BlockLogArtificial.create(this);
			BlockLeaves leaves = BlockLeaves.create(this);
			BlockLeavesCore coreLeaves = BlockLeavesCore.create(leaves, this);
			this.leaves = leaves;
			log = logNatural;
			logArt = logArtificial;
			OreDict.registerValid("logWood", logArtificial);
			OreDict.registerValid("leaves", leaves);
			tree.initInfo(logNatural, logArtificial, leaves, coreLeaves);
		}
		return this;
	}
	
	public Mat setRock(int harvestLevel, float hardness, float resistance, int minDetTemp)
	{
		isRock = true;
		blockHarvestLevel = harvestLevel;
		blockHardness = hardness;
		blockExplosionResistance = resistance;
		minDetHeatForExplosion = minDetTemp;
		BlockRock rock = new BlockRock("rock." + name, this, localName);
		this.rock = rock;
		add(SubTag.ROCK);
		return this;
	}
	
	public Mat setRock(int harvestLevel, float hardness, float resistance, int minDetTemp, Block rock)
	{
		isRock = true;
		blockHarvestLevel = harvestLevel;
		blockHardness = hardness;
		blockExplosionResistance = resistance;
		minDetHeatForExplosion = minDetTemp;
		this.rock = rock;//Use custom rock block might have other use, will not auto-register into ore dictionary.
		add(SubTag.ROCK);
		return this;
	}
	
	public Mat setCrop(ICrop crop)
	{
		isCrop = true;
		this.crop = crop;
		add(SubTag.CROP);
		return this;
	}
	
	public Mat setTag(SubTag...tags)
	{
		add(tags);
		return this;
	}
	
	@Override
	public void add(SubTag... tags)
	{
		subTags.addAll(Arrays.asList(tags));
	}
	
	@Override
	public boolean contain(SubTag tag)
	{
		return subTags.contains(tag);
	}
	
	@Override
	public int compareTo(Mat o)
	{
		return name.compareTo(o.name);
	}

	@Override
	public String toString()
	{
		return name;
	}
}