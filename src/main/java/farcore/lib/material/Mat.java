package farcore.lib.material;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import farcore.data.Config;
import farcore.data.MP;
import farcore.data.SubTags;
import farcore.lib.block.behavior.MetalBlockBehavior;
import farcore.lib.block.behavior.RockBehavior;
import farcore.lib.block.instance.BlockLeaves;
import farcore.lib.block.instance.BlockLeavesCore;
import farcore.lib.block.instance.BlockLogArtificial;
import farcore.lib.block.instance.BlockLogNatural;
import farcore.lib.block.instance.BlockPlank;
import farcore.lib.block.instance.BlockRock;
import farcore.lib.block.instance.BlockSoil;
import farcore.lib.crop.ICrop;
import farcore.lib.material.behavior.IItemMatProp;
import farcore.lib.material.ore.IOreProperty;
import farcore.lib.material.prop.PropertyBasic;
import farcore.lib.material.prop.PropertyBlockable;
import farcore.lib.material.prop.PropertyOre;
import farcore.lib.material.prop.PropertyTool;
import farcore.lib.material.prop.PropertyTree;
import farcore.lib.material.prop.PropertyWood;
import farcore.lib.plant.IPlant;
import farcore.lib.tree.ITree;
import nebula.common.LanguageManager;
import nebula.common.base.HashPropertyMap;
import nebula.common.base.IPropertyMap;
import nebula.common.base.IPropertyMap.IProperty;
import nebula.common.base.IntegerMap;
import nebula.common.base.Judgable;
import nebula.common.base.Register;
import nebula.common.util.Game;
import nebula.common.util.IRegisteredNameable;
import nebula.common.util.ISubTagContainer;
import nebula.common.util.ItemStacks;
import nebula.common.util.SubTag;
import nebula.io.javascript.ScriptLoad;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class Mat implements ISubTagContainer, IRegisteredNameable, Comparable<Mat>
{
	private static final Register<Mat> REGISTER = new Register(32768);
	
	private static final Map<Judgable<? super Mat>, List<Mat>> MATERIALS_CACHE = new HashMap();
	
	/**
	 * Default material, will not register in to list.
	 */
	public static final Mat VOID = new Mat(-1, false, "", "void", "Void", "Void").setToolable(0, 1, 1.0F, 0.0F, 1.0F, 1.0F, 0).setHandable(1.0F).setCrop(ICrop.VOID).setWood(0.0F, 0.0F, 0.0F).setTree(ITree.VOID);
	
	private static void onDataChanged()
	{
		MATERIALS_CACHE.clear();
	}
	
	static
	{
		VOID.addProperty(MP.property_wood, PropertyTree.VOID);
	}
	
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
	public static <V> Iterable<V> filtAndGet(Judgable<? super Mat> filter, IProperty<V> property)
	{
		return Iterables.transform(filt(filter), m->m.getProperty(property));
	}
	public static <V> Iterable<V> filtAndGet(Judgable<? super Mat> filter, boolean alwaysInit, IProperty<V> property)
	{
		return Iterables.transform(filt(filter, alwaysInit), m->m.getProperty(property));
	}
	public static List<Mat> filt(Judgable<? super Mat> filter)
	{
		return filt(filter, false);
	}
	public static List<Mat> filt(Judgable<? super Mat> filter, boolean alwaysInit)
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
	
	@ScriptLoad
	public final String modid;
	@ScriptLoad
	public final String name;
	public final String oreDictName;
	public final String localName;
	@ScriptLoad
	public final short id;
	/**
	 * Some material is variant of other material,
	 * this field is the source material target.
	 */
	public Mat unificationMaterial = this;
	public String chemicalFormula;
	public String customDisplayInformation;
	public short[] RGBa = {255, 255, 255, 255};
	@ScriptLoad
	public int RGB = 0xFFFFFF;
	//Multi item configuration.
	public IItemMatProp itemProp;
	
	/**
	 * The heat capacity of material, unit : J/(m^3*K)
	 */
	public double heatCapacity;
	/**
	 * The thermal conductivity of material, unit : W/(m*K)
	 */
	public double thermalConductivity;
	public double maxSpeed;
	public double maxTorque;
	public double dielectricConstant;
	public double electrialResistance;
	public float redstoneResistance;
	
	public int toolMaxUse = 1;
	public int toolHarvestLevel;
	public float toolHardness = 1.0F;
	public float toolBrittleness;
	public float toolDamageToEntity;
	
	private IPropertyMap propertyMap = new HashPropertyMap();
	//Reused now.
	private IntegerMap<String> properties = new IntegerMap();
	private Set<SubTag> subTags = new HashSet();
	
	public Mat(int id, String name, String oreDict, String localized)
	{
		this(id, Game.getActiveModID(), name, oreDict, localized);
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
		this.oreDictName = oreDict;
		this.localName = localized;
		LanguageManager.registerLocal("material." + name + ".name", localized);
		if(register)
		{
			Mat.REGISTER.register(id, name, this);
		}
	}
	
	@Override
	public final String getRegisteredName()
	{
		return this.name;
	}
	
	public String getLocalName()
	{
		return LanguageManager.translateToLocal("material." + this.name + ".name");
	}
	
	public Mat setRGBa(int colorIndex)
	{
		this.RGBa[0] = (short) ((colorIndex >> 24)       );
		this.RGBa[1] = (short) ((colorIndex >> 16) & 0xFF);
		this.RGBa[2] = (short) ((colorIndex >> 8 ) & 0xFF);
		this.RGBa[3] = (short) ((colorIndex      ) & 0xFF);
		this.RGB = colorIndex >> 8;
		return this;
	}
	
	public Mat setRGBa(short[] colorIndex)
	{
		this.RGBa = colorIndex;
		this.RGB = colorIndex[0] << 16 | colorIndex[1] << 8 | colorIndex[2];
		return this;
	}
	
	public Mat setUnificationMaterial(Mat material)
	{
		this.unificationMaterial = material;
		return this;
	}
	
	public Mat setChemicalFormula(String name)
	{
		this.chemicalFormula = name;
		return this;
	}
	
	public Mat setCustomInformation(String info)
	{
		this.customDisplayInformation = info;
		return this;
	}
	
	public <V> Mat addProperty(IProperty<V> property, V value)
	{
		this.propertyMap.put(property, value);
		onDataChanged();
		return this;
	}
	
	public <V> V getProperty(IProperty<V> property)
	{
		return getProperty(property, property.defValue());
	}
	
	public <V> V getProperty(IProperty<V> property, V def)
	{
		V value = this.propertyMap.get(property);
		return value == null ? def : value;
	}
	
	public Mat setGeneralProp(float heatCap, float thermalConduct, float maxSpeed, float maxTorque, float dielectricConstant, float electrialResistance, float redstoneResistance)
	{
		PropertyBasic property = new PropertyBasic();
		this.heatCapacity = property.heatCap = heatCap;
		this.thermalConductivity = property.thermalConduct = thermalConduct;
		this.dielectricConstant = property.dielectricConstant = dielectricConstant;
		this.maxSpeed = property.maxSpeed = maxSpeed;
		this.maxTorque = property.maxTorque = maxTorque;
		this.electrialResistance = property.electrialResistance = electrialResistance;
		this.redstoneResistance = property.redstoneResistance = redstoneResistance;
		return addProperty(MP.property_basic, property);
	}
	
	public Mat setMetalic(int harvestLevel, float hardness, float resistance)
	{
		MetalBlockBehavior behavior = new MetalBlockBehavior<>();
		behavior.explosionResistance = resistance;
		behavior.hardness = hardness;
		behavior.harvestLevel = harvestLevel;
		behavior.material = this;
		add(SubTags.METAL);
		return this;
	}
	
	public Mat setToolable(int harvestLevel, int maxUse, float hardness, float brittleness, float attackSpeed, float dVE, int enchantability)
	{
		PropertyTool property = new PropertyTool();
		this.toolHarvestLevel = property.harvestLevel = harvestLevel;
		this.toolMaxUse = property.maxUse = maxUse;
		this.toolHardness = property.hardness = hardness;
		this.toolBrittleness = property.brittleness = brittleness;
		this.toolDamageToEntity = property.damageToEntity = dVE;
		property.enchantability = enchantability;
		property.attackSpeed = attackSpeed;
		add(SubTags.TOOL);
		return addProperty(MP.property_tool, property);
	}
	
	@Deprecated
	public Mat setHandable(float toughness)
	{
		//		handleToughness = toughness;
		add(SubTags.HANDLE);
		return this;
	}
	
	public Mat setOreProperty(int harvestLevel, float hardness, float resistance)
	{
		return setOreProperty(harvestLevel, hardness, resistance, SubTags.ORE_SIMPLE);
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
		add(SubTags.ORE, type);
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
		add(SubTags.WOOD);
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
		PropertyWood property0 = this.propertyMap.get(MP.property_wood);
		PropertyTree property = new PropertyTree.PropertyTreeWrapper(tree);
		property.ashcontent = property0.ashcontent;
		property.burnHeat = property0.burnHeat;
		property.explosionResistance = property0.explosionResistance;
		property.hardness = property0.hardness;
		property.harvestLevel = property0.harvestLevel;
		add(SubTags.TREE);
		if(createBlock && Config.createLog)
		{
			BlockLogNatural logNatural = BlockLogNatural.create(this, property);
			BlockLogArtificial logArtificial = BlockLogArtificial.create(this, property);
			BlockLeaves leaves = BlockLeaves.create(this, property);
			BlockLeavesCore coreLeaves = BlockLeavesCore.create(leaves, this, property);
			BlockPlank plank = new BlockPlank(this, property);
			property.block = logArtificial;
			property.plank = plank;
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
			property.block = new BlockSoil(this.modid, "soil." + this.name, material, this, property);
		}
		add(SubTags.DIRT);
		return addProperty(MP.property_soil, property);
	}
	
	public Mat setRock(int harvestLevel, float hardness, float resistance, RockBehavior behavior)
	{
		behavior.harvestLevel = harvestLevel;
		behavior.hardness = hardness;
		behavior.explosionResistance = resistance;
		behavior.block = new BlockRock(this, behavior);
		add(SubTags.ROCK);
		return addProperty(MP.property_rock, behavior);
	}
	public Mat setRock(int harvestLevel, float hardness, float resistance)
	{
		return setRock(harvestLevel, hardness, resistance, new RockBehavior(this));
	}
	
	public Mat setCrop(ICrop crop)
	{
		add(SubTags.CROP);
		return addProperty(MP.property_crop, crop);
	}
	
	public Mat setPlant(IPlant plant)
	{
		add(SubTags.PLANT);
		return addProperty(MP.property_plant, plant);
	}
	
	public Mat setTag(SubTag...tags)
	{
		add(tags);
		return this;
	}
	
	public Mat addProperty(String tag, int value)
	{
		this.properties.put(tag, value);
		return this;
	}
	
	public Mat addProperty(String tag, float value)
	{
		return addProperty(tag, Float.floatToIntBits(value));
	}
	
	public int getProperty(String tag)
	{
		return this.properties.get(tag);
	}
	
	public float getPropertyF(String tag)
	{
		return Float.intBitsToFloat(getProperty(tag));
	}
	
	@Override
	public void add(SubTag... tags)
	{
		this.subTags.addAll(Arrays.asList(tags));
		onDataChanged();
	}
	
	@Override
	public boolean contain(SubTag tag)
	{
		return this.subTags.contains(tag);
	}
	
	@Override
	public int compareTo(Mat o)
	{
		return this.name.compareTo(o.name);
	}
	
	@Override
	public String toString()
	{
		return this.name;
	}
	
	public static Mat getMaterialByIDOrDefault(NBTTagCompound nbt, String key, Mat def)
	{
		return nbt.hasKey(key) ? material(nbt.getShort(key), def) : def;
	}
	
	public static Mat getMaterialByNameOrDefault(NBTTagCompound nbt, String key, Mat def)
	{
		return nbt.hasKey(key) ? material(nbt.getString(key), def) : def;
	}
	
	public static void setMaterialToStack(ItemStack stack, String key, Mat material)
	{
		ItemStacks.getOrSetupNBT(stack, true).setString(key, material.name);
	}
	
	public static Mat getMaterialFromStack(ItemStack stack, String key, Mat def)
	{
		return getMaterialByNameOrDefault(ItemStacks.getOrSetupNBT(stack, false), key, def);
	}
}