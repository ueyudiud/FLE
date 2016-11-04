package farcore.lib.material;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableList;

import farcore.data.Config;
import farcore.data.M;
import farcore.lib.block.instance.BlockLeaves;
import farcore.lib.block.instance.BlockLeavesCore;
import farcore.lib.block.instance.BlockLogArtificial;
import farcore.lib.block.instance.BlockLogNatural;
import farcore.lib.block.instance.BlockPlank;
import farcore.lib.block.instance.BlockRock;
import farcore.lib.block.instance.BlockSoil;
import farcore.lib.collection.HashPropertyMap;
import farcore.lib.collection.IPropertyMap;
import farcore.lib.collection.IPropertyMap.IProperty;
import farcore.lib.collection.IntegerMap;
import farcore.lib.collection.Register;
import farcore.lib.crop.ICrop;
import farcore.lib.material.ore.IOreProperty;
import farcore.lib.material.prop.PropertyBasic;
import farcore.lib.material.prop.PropertyBlockable;
import farcore.lib.material.prop.PropertyOre;
import farcore.lib.material.prop.PropertyRock;
import farcore.lib.material.prop.PropertyTool;
import farcore.lib.material.prop.PropertyTree;
import farcore.lib.material.prop.PropertyWood;
import farcore.lib.plant.IPlant;
import farcore.lib.tree.ITree;
import farcore.lib.util.IDataChecker;
import farcore.lib.util.IRegisteredNameable;
import farcore.lib.util.ISubTagContainer;
import farcore.lib.util.LanguageManager;
import farcore.lib.util.SubTag;
import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class Mat implements ISubTagContainer, IRegisteredNameable, Comparable<Mat>
{
	private static final Register<Mat> register = new Register(32768);

	private static final Map<IDataChecker<ISubTagContainer>, List<Mat>> materials = new HashMap();
	
	public static Register<Mat> materials()
	{
		return register;
	}
	public static Mat material(int id)
	{
		return register.get(id);
	}
	public static Mat material(String name)
	{
		return register.get(name, M.VOID);
	}
	public static Mat material(String name, Mat def)
	{
		return register.get(name, def);
	}
	public static boolean contain(String name)
	{
		return register.contain(name);
	}
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
	/**
	 * Some material is variant of other material,
	 * this field is the source material target.
	 */
	public Mat unificationMaterial = this;
	public String chemicalFormula;
	public String customDisplayInformation;
	public short[] RGBa = {255, 255, 255, 255};
	public int RGB = 0xFFFFFF;
	//For general property.
	public PropertyBasic basic;
	//All these property will be remove, use property map instead.
	//Tool configuration.
	@Deprecated
	public boolean canMakeTool = false;
	@Deprecated
	public int toolMaxUse = 1;
	@Deprecated
	public int toolHarvestLevel;
	@Deprecated
	public float toolHardness = 1.0F;
	@Deprecated
	public float toolDamageToEntity;
	@Deprecated
	public int toolEnchantability;
	@Deprecated
	public float toolBrittleness;
	@Deprecated
	public float toolAttackSpeed;
	@Deprecated
	public float handleToughness = 1.0F;
	//Block configuration.
	@Deprecated
	public boolean isRock = false;
	@Deprecated
	public boolean isSand = false;
	@Deprecated
	public boolean isDirt = false;
	@Deprecated
	public String blockHarvestTool;
	@Deprecated
	public int blockHarvestLevel;
	@Deprecated
	public float blockExplosionResistance;
	@Deprecated
	public float blockHardness;
	//Wood matter configuration.
	@Deprecated
	public boolean isWood = false;
	@Deprecated
	public float woodHardness;//Useless prop
	@Deprecated
	public float ashcontent;
	@Deprecated
	public float woodBurnHeat;
	@Deprecated
	public boolean hasTree = false;
	@Deprecated
	public ITree tree;
	/** This two block is for tree generation. */
	@Deprecated
	public Block log;
	/** The log which is already cut off from tree. */
	@Deprecated
	public Block logArt;
	@Deprecated
	public Block leaves;
	@Deprecated
	public Block plank;
	//Rock extra configuration.
	@Deprecated
	public Block rock;
	@Deprecated
	public float minTemperatureForExplosion;
	//Soil extra configuration
	@Deprecated
	public Block soil;
	//Ore configuration.
	@Deprecated
	public IOreProperty oreProperty = IOreProperty.property;
	//Plant configuration
	/** The plant grown in wild. */
	@Deprecated
	public IPlant plant;
	@Deprecated
	public Block wildPlantBlock;
	//Crop configuration.
	@Deprecated
	public boolean isCrop = false;
	@Deprecated
	public ICrop crop;
	//Multi item configuration.
	public IItemMatProp itemProp;

	private IPropertyMap propertyMap = new HashPropertyMap();
	@Deprecated
	private IntegerMap<String> properties = new IntegerMap();
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
	
	public Mat setUnificationMaterial(Mat material)
	{
		unificationMaterial = material;
		return this;
	}
	
	public Mat setChemicalFormula(String name)
	{
		chemicalFormula = name;
		return this;
	}

	public Mat setCustomInformation(String info)
	{
		customDisplayInformation = info;
		return this;
	}

	public <V> Mat addProperty(IProperty<V> property, V value)
	{
		if(property == M.property_basic)
		{
			basic = (PropertyBasic) value;
		}
		else
		{
			propertyMap.put(property, value);
		}
		return this;
	}
	
	public <V> V getProperty(IProperty<V> property)
	{
		return property == M.property_basic ? (basic != null ? (V) basic : property.defValue()) : getProperty(property, property.defValue());
	}
	
	public <V> V getProperty(IProperty<V> property, V def)
	{
		V value = propertyMap.get(property);
		return value == null ? def : value;
	}

	public Mat setGeneralProp(float heatCap, float thermalConduct, float maxSpeed, float maxTorque, float dielectricConstant, float electrialResistance, float redstoneResistance)
	{
		PropertyBasic property = basic = new PropertyBasic();
		property.heatCap = heatCap;
		property.thermalConduct = thermalConduct;
		property.dielectricConstant = dielectricConstant;
		property.maxSpeed = maxSpeed;
		property.maxTorque = maxTorque;
		property.electrialResistance = electrialResistance;
		property.redstoneResistance = redstoneResistance;
		return addProperty(M.property_basic, property);
	}

	public Mat setToolable(int harvestLevel, int maxUse, float hardness, float brittleness, float attackSpeed, float dVE, int enchantability)
	{
		PropertyTool property = new PropertyTool();
		canMakeTool = true;
		property.harvestLevel = toolHarvestLevel = harvestLevel;
		property.maxUse = toolMaxUse = maxUse;
		property.hardness = toolHardness = hardness;
		property.brittleness = toolBrittleness = brittleness;
		property.damageToEntity = toolDamageToEntity = dVE;
		property.enchantability = toolEnchantability = enchantability;
		property.attackSpeed = toolAttackSpeed = attackSpeed;
		add(SubTag.TOOL);
		return addProperty(M.property_tool, property);
	}

	public Mat setHandable(float toughness)
	{
		handleToughness = toughness;
		add(SubTag.HANDLE);
		return this;
	}
	
	public Mat setOreProperty(int harvestLevel, float hardness, float resistance)
	{
		return setOreProperty(harvestLevel, hardness, resistance, SubTag.ORE_SIMPLE);
	}
	
	public Mat setOreProperty(int harvestLevel, float hardness, float resistance, SubTag type)
	{
		return setOreProperty(harvestLevel, hardness, resistance, IOreProperty.property, type);
	}

	public Mat setOreProperty(int harvestLevel, float hardness, float resistance, IOreProperty oreProperty, SubTag type)
	{
		PropertyOre property;
		if(oreProperty == IOreProperty.property)
		{
			property = new PropertyOre();
		}
		else if(oreProperty instanceof PropertyOre)
		{
			property = (PropertyOre) oreProperty;
		}
		else
		{
			property = new PropertyOre.PropertyOreWrapper(this.oreProperty = oreProperty);
		}
		property.harvestLevel = blockHarvestLevel = harvestLevel;
		property.hardness = blockHardness = hardness;
		property.explosionResistance = blockExplosionResistance = resistance;
		add(SubTag.ORE, type);
		return addProperty(M.property_ore, property);
	}

	public Mat setWood(float woodHardness, float ashcontent, float woodBurnHeat)
	{
		PropertyWood property = new PropertyWood();
		isWood = true;
		property.hardness = blockHardness = 1.5F + woodHardness / 4F;
		property.explosionResistance = blockExplosionResistance = 0.4F + woodHardness / 8F;
		property.harvestLevel = blockHarvestLevel = 1;
		blockHarvestTool = "axe";
		this.woodHardness = woodHardness;
		property.ashcontent = this.ashcontent = ashcontent;
		property.burnHeat = this.woodBurnHeat = woodBurnHeat;
		addProperty(M.fallen_damage_deduction, (int) (1000 / (woodHardness + 1)));
		addProperty(M.flammability, 50);
		addProperty(M.fire_encouragement, 4);
		addProperty(M.fire_spread_speed, 25);
		return addProperty(M.property_wood, property);
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
		PropertyWood property0 = propertyMap.get(M.property_wood);
		PropertyTree property = new PropertyTree.PropertyTreeWrapper(this.tree = tree);
		property.ashcontent = property0.ashcontent;
		property.burnHeat = property0.burnHeat;
		property.explosionResistance = property0.explosionResistance;
		property.hardness = property0.hardness;
		property.harvestLevel = property0.harvestLevel;
		hasTree = true;
		add(SubTag.TREE);
		if(createBlock && Config.createLog)
		{
			BlockLogNatural logNatural = BlockLogNatural.create(this, property);
			BlockLogArtificial logArtificial = BlockLogArtificial.create(this, property);
			BlockLeaves leaves = BlockLeaves.create(this, property);
			BlockLeavesCore coreLeaves = BlockLeavesCore.create(leaves, this, property);
			BlockPlank plank = new BlockPlank(this, property);
			this.leaves = leaves;
			this.plank = plank;
			log = logNatural;
			logArt = logArtificial;
			property.initInfo(logNatural, logArtificial, leaves, coreLeaves);
		}
		property.setMaterial(this);
		return addProperty(M.property_wood, property);//Override old property.
	}

	public Mat setSoil(float hardness, float resistance, Material material)
	{
		PropertyBlockable property = new PropertyBlockable();
		isDirt = true;
		property.harvestLevel = blockHarvestLevel = -1;//It seems no soil need use tool to harvest.
		property.hardness = blockHardness = hardness;
		property.explosionResistance = blockExplosionResistance = resistance;
		if(Config.createSoil)
		{
			soil = new BlockSoil(modid, "soil." + name, material, this, property);
		}
		add(SubTag.DIRT);
		return addProperty(M.property_soil, property);
	}

	public Mat setRock(int harvestLevel, float hardness, float resistance, int minDetTemp)
	{
		PropertyRock property = new PropertyRock();
		isRock = true;
		property.harvestLevel = blockHarvestLevel = harvestLevel;
		property.hardness = blockHardness = hardness;
		property.explosionResistance = blockExplosionResistance = resistance;
		minTemperatureForExplosion = property.minTemperatureForExplosion = minDetTemp;
		if(Config.createRock)
		{
			rock = new BlockRock("rock." + name, this, property);
		}
		add(SubTag.ROCK);
		return addProperty(M.property_rock, property);
	}

	public Mat setRock(int harvestLevel, float hardness, float resistance, int minDetTemp, Block rock)
	{
		PropertyRock property = new PropertyRock();
		isRock = true;
		property.harvestLevel = blockHarvestLevel = harvestLevel;
		property.hardness = blockHardness = hardness;
		property.explosionResistance = blockExplosionResistance = resistance;
		minTemperatureForExplosion = property.minTemperatureForExplosion = minDetTemp;
		this.rock = rock;//Use custom rock block might have other use, will not auto-register into ore dictionary.
		add(SubTag.ROCK);
		return addProperty(M.property_rock, property);
	}

	public Mat setCrop(ICrop crop)
	{
		isCrop = true;
		add(SubTag.CROP);
		return addProperty(M.property_crop, this.crop = crop);
	}

	public Mat setTag(SubTag...tags)
	{
		add(tags);
		return this;
	}
	
	public Mat addProperty(String tag, int value)
	{
		properties.put(tag, value);
		return this;
	}

	public int getProperty(String tag)
	{
		return properties.get(tag);
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