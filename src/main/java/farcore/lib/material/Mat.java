package farcore.lib.material;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableList;

import farcore.data.Config;
import farcore.data.MP;
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
import farcore.lib.material.behavior.IItemMatProp;
import farcore.lib.material.ore.IOreProperty;
import farcore.lib.material.prop.PropertyBasic;
import farcore.lib.material.prop.PropertyBlockable;
import farcore.lib.material.prop.PropertyOre;
import farcore.lib.material.prop.PropertyRock;
import farcore.lib.material.prop.PropertyTool;
import farcore.lib.material.prop.PropertyTree;
import farcore.lib.material.prop.PropertyWood;
import farcore.lib.tree.ITree;
import farcore.lib.util.IDataChecker;
import farcore.lib.util.IRegisteredNameable;
import farcore.lib.util.ISubTagContainer;
import farcore.lib.util.LanguageManager;
import farcore.lib.util.SubTag;
import farcore.util.U.Mod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class Mat implements ISubTagContainer, IRegisteredNameable, Comparable<Mat>
{
	private static final Register<Mat> REGISTER = new Register(32768);
	
	private static final Map<IDataChecker<ISubTagContainer>, List<Mat>> MATERIALS_CACHE = new HashMap();
	
	/**
	 * Default material, will not register in to list.
	 */
	public static final Mat VOID = new Mat(-1, false, "", "void", "Void", "Void").setToolable(0, 1, 1.0F, 0.0F, 1.0F, 1.0F, 0).setHandable(1.0F).setCrop(ICrop.VOID);

	public static Register<Mat> materials()
	{
		return REGISTER;
	}
	public static Mat material(int id)
	{
		return REGISTER.get(id);
	}
	public static Mat material(int id, Mat def)
	{
		return REGISTER.get(id, def);
	}
	public static Mat material(String name)
	{
		return REGISTER.get(name, Mat.VOID);
	}
	public static Mat material(String name, Mat def)
	{
		return REGISTER.get(name, def);
	}
	public static boolean contain(String name)
	{
		return REGISTER.contain(name);
	}
	public static List<Mat> filt(IDataChecker<ISubTagContainer> filter)
	{
		return filt(filter, false);
	}
	public static List<Mat> filt(IDataChecker<ISubTagContainer> filter, boolean alwaysInit)
	{
		if(!MATERIALS_CACHE.containsKey(filter) || alwaysInit)
		{
			ImmutableList.Builder<Mat> list = ImmutableList.builder();
			for(Mat material : REGISTER)
			{
				if(filter.isTrue(material))
				{
					list.add(material);
				}
			}
			List<Mat> ret = list.build();
			if(!alwaysInit)
			{
				MATERIALS_CACHE.put(filter, ret);
			}
			return ret;
		}
		else return MATERIALS_CACHE.get(filter);
	}

	public final String modid;
	public final String name;
	public final String oreDictName;
	public final String localName;
	public final short id;
	/**
	 * Some material is variant of other material,
	 * this field is the source material target.
	 */
	public Mat unificationMaterial = this;
	public String chemicalFormula;
	public String customDisplayInformation;
	public short[] RGBa = {255, 255, 255, 255};
	public int RGB = 0xFFFFFF;
	//For general property, because it often be used, extract from property map.
	public PropertyBasic basic;
	//Multi item configuration.
	public IItemMatProp itemProp;
	
	private IPropertyMap propertyMap = new HashPropertyMap();
	//Reused now.
	private IntegerMap<String> properties = new IntegerMap();
	private Set<SubTag> subTags = new HashSet();
	
	public Mat(int id, String name, String oreDict, String localized)
	{
		this(id, Mod.getActiveModID(), name, oreDict, localized);
	}
	public Mat(int id, String modid, String name, String oreDict, String localized)
	{
		this(id, true, modid, name, oreDict, localized);
	}
	public Mat(int id, boolean register, String modid, String name, String oreDict, String localized)
	{
		this.id = (short) id;
		this.modid = modid;
		this.name = name;
		oreDictName = oreDict;
		localName = localized;
		LanguageManager.registerLocal("material." + name + ".name", localized);
		if(register)
		{
			Mat.REGISTER.register(id, name, this);
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
		if(property == MP.property_basic)
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
		return property == MP.property_basic ? (basic != null ? (V) basic : property.defValue()) : getProperty(property, property.defValue());
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
		return addProperty(MP.property_basic, property);
	}
	
	public Mat setToolable(int harvestLevel, int maxUse, float hardness, float brittleness, float attackSpeed, float dVE, int enchantability)
	{
		PropertyTool property = new PropertyTool();
		property.harvestLevel = harvestLevel;
		property.maxUse = maxUse;
		property.hardness = hardness;
		property.brittleness = brittleness;
		property.damageToEntity = dVE;
		property.enchantability = enchantability;
		property.attackSpeed = attackSpeed;
		add(SubTag.TOOL);
		return addProperty(MP.property_tool, property);
	}
	
	@Deprecated
	public Mat setHandable(float toughness)
	{
		//		handleToughness = toughness;
		add(SubTag.HANDLE);
		return this;
	}

	public Mat setOreProperty(int harvestLevel, float hardness, float resistance)
	{
		return setOreProperty(harvestLevel, hardness, resistance, SubTag.ORE_SIMPLE);
	}

	public Mat setOreProperty(int harvestLevel, float hardness, float resistance, SubTag type)
	{
		return setOreProperty(harvestLevel, hardness, resistance, IOreProperty.PROPERTY, type);
	}
	
	public Mat setOreProperty(int harvestLevel, float hardness, float resistance, IOreProperty oreProperty, SubTag type)
	{
		PropertyOre property;
		if(oreProperty == IOreProperty.PROPERTY)
		{
			property = new PropertyOre();
		}
		else if(oreProperty instanceof PropertyOre)
		{
			property = (PropertyOre) oreProperty;
		}
		else
		{
			property = new PropertyOre.PropertyOreWrapper(oreProperty);
		}
		property.material = this;
		property.harvestLevel = harvestLevel;
		property.hardness = hardness;
		property.explosionResistance = resistance;
		add(SubTag.ORE, type);
		return addProperty(MP.property_ore, property);
	}
	
	public Mat setWood(float woodHardness, float ashcontent, float woodBurnHeat)
	{
		PropertyWood property = new PropertyWood();
		property.hardness = 1.5F + woodHardness / 4F;
		property.explosionResistance = 0.4F + woodHardness / 8F;
		property.harvestLevel = 1;
		property.ashcontent = ashcontent;
		property.burnHeat = woodBurnHeat;
		addProperty(MP.fallen_damage_deduction, (int) (1000 / (woodHardness + 1)));
		addProperty(MP.flammability, 50);
		addProperty(MP.fire_encouragement, 4);
		addProperty(MP.fire_spread_speed, 25);
		add(SubTag.WOOD);
		return addProperty(MP.property_wood, property);
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
		PropertyWood property0 = propertyMap.get(MP.property_wood);
		PropertyTree property = new PropertyTree.PropertyTreeWrapper(tree);
		property.ashcontent = property0.ashcontent;
		property.burnHeat = property0.burnHeat;
		property.explosionResistance = property0.explosionResistance;
		property.hardness = property0.hardness;
		property.harvestLevel = property0.harvestLevel;
		add(SubTag.TREE);
		if(createBlock && Config.createLog)
		{
			BlockLogNatural logNatural = BlockLogNatural.create(this, property);
			BlockLogArtificial logArtificial = BlockLogArtificial.create(this, property);
			BlockLeaves leaves = BlockLeaves.create(this, property);
			BlockLeavesCore coreLeaves = BlockLeavesCore.create(leaves, this, property);
			BlockPlank plank = new BlockPlank(this, property);
			property.block = logArtificial;
			property.initInfo(logNatural, logArtificial, leaves, coreLeaves);
		}
		property.setMaterial(this);
		return addProperty(MP.property_wood, property);//Override old property.
	}
	
	public Mat setSoil(float hardness, float resistance, Material material)
	{
		PropertyBlockable property = new PropertyBlockable();
		property.material = this;
		property.harvestLevel = -1;//It seems no soil need use tool to harvest.
		property.hardness = hardness;
		property.explosionResistance = resistance;
		if(Config.createSoil)
		{
			new BlockSoil(modid, "soil." + name, material, this, property);
		}
		add(SubTag.DIRT);
		return addProperty(MP.property_soil, property);
	}
	
	public Mat setRock(int harvestLevel, float hardness, float resistance, int minDetTemp)
	{
		PropertyRock property = new PropertyRock();
		property.material = this;
		property.harvestLevel = harvestLevel;
		property.hardness = hardness;
		property.explosionResistance = resistance;
		property.minTemperatureForExplosion = minDetTemp;
		if(Config.createRock)
		{
			property.block = new BlockRock("rock." + name, this, property);
		}
		add(SubTag.ROCK);
		return addProperty(MP.property_rock, property);
	}
	
	public Mat setRock(int harvestLevel, float hardness, float resistance, int minDetTemp, Block rock)
	{
		PropertyRock property = new PropertyRock();
		property.material = this;
		property.harvestLevel = harvestLevel;
		property.hardness = hardness;
		property.explosionResistance = resistance;
		property.minTemperatureForExplosion = minDetTemp;
		property.block = rock;//Use custom rock block might have other use, will not auto-register into ore dictionary.
		add(SubTag.ROCK);
		return addProperty(MP.property_rock, property);
	}
	
	public Mat setCrop(ICrop crop)
	{
		add(SubTag.CROP);
		return addProperty(MP.property_crop, crop);
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