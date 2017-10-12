/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.material;

import static farcore.data.V.MATERIAL_SIZE;
import static net.minecraft.init.Bootstrap.isRegistered;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import farcore.data.MP;
import farcore.data.SubTags;
import farcore.lib.block.behavior.MetalBlockBehavior;
import farcore.lib.block.behavior.RockBehavior;
import farcore.lib.block.instance.BlockBrick;
import farcore.lib.block.terria.BlockRock;
import farcore.lib.block.terria.BlockSand;
import farcore.lib.block.terria.BlockSoil;
import farcore.lib.block.terria.BlockStoneChip;
import farcore.lib.block.wood.BlockLeaves;
import farcore.lib.block.wood.BlockLeavesCore;
import farcore.lib.block.wood.BlockLogArtificial;
import farcore.lib.block.wood.BlockLogNatural;
import farcore.lib.block.wood.BlockPlank;
import farcore.lib.block.wood.BlockWoodenFence;
import farcore.lib.crop.ICrop;
import farcore.lib.material.behavior.IItemMatProp;
import farcore.lib.material.ore.IOreProperty;
import farcore.lib.material.prop.PropertyBasic;
import farcore.lib.material.prop.PropertyBlockable;
import farcore.lib.material.prop.PropertyOre;
import farcore.lib.material.prop.PropertyTool;
import farcore.lib.material.prop.PropertyWood;
import farcore.lib.plant.IPlant;
import farcore.lib.tree.Tree;
import nebula.base.HashPropertyMap;
import nebula.base.IPropertyMap;
import nebula.base.IPropertyMap.IProperty;
import nebula.base.IntegerMap;
import nebula.base.Judgable;
import nebula.base.Register;
import nebula.common.LanguageManager;
import nebula.common.nbt.INBTReaderAndWritter;
import nebula.common.util.A;
import nebula.common.util.Game;
import nebula.common.util.IRegisteredNameable;
import nebula.common.util.ISubTagContainer;
import nebula.common.util.ItemStacks;
import nebula.common.util.SubTag;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;

/**
 * Materials of all elements in game.
 * @author ueyudiud
 */
public class Mat implements ISubTagContainer, IRegisteredNameable, Comparable<Mat>
{
	private static final Register<Mat> REGISTER = new Register<>(MATERIAL_SIZE);
	
	private static final Map<Judgable<? super Mat>, List<Mat>> MATERIALS_CACHE = new HashMap<>();
	
	/**
	 * Default material, will not register in to list.<p>
	 * The default result when fail to search material from list.
	 */
	public static final Mat VOID = new Mat(-1, false, "", "void", "Void", "Void");
	
	public static final INBTReaderAndWritter<Mat, NBTTagString> WITH_NULL_RW = new INBTReaderAndWritter<Mat, NBTTagString>()
	{
		@Override
		public Mat readFromNBT(NBTTagString nbt)
		{
			return REGISTER.get(nbt.getString());
		}
		
		@Override
		public NBTTagString writeToNBT(Mat target)
		{
			return new NBTTagString(target.name);
		}
	};
	
	private static void onDataChanged()
	{
		MATERIALS_CACHE.clear();
	}
	
	static
	{
		if (isRegistered())
		{
			VOID.builder()
			.setToolable(0, 1, 1.0F, 0.0F, 1.0F, 1.0F, 0)
			.setHandable(1.0F)
			.addProperty(MP.property_crop, ICrop.VOID)
			.addProperty(MP.property_wood, Tree.VOID).build();
		}
		/**
		 * For debugging use, generate model file, export material properties, etc.<p>
		 * The game will not initialize so it need prevent using class needed initialized.
		 */
		else System.out.println("The material may be in debug enviorment, skip VOID property registeration.");
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
	public static <V> V propertyOf(String name, IPropertyMap.IProperty<V> property)
	{
		return material(name).getProperty(property);
	}
	public static Mat material(String name)
	{
		return REGISTER.get(name, VOID);
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
		if (!MATERIALS_CACHE.containsKey(filter) || alwaysInit)
		{
			ImmutableList.Builder<Mat> list = ImmutableList.builder();
			for (Mat material : REGISTER)
			{
				if (filter.isTrue(material))
				{
					list.add(material);
				}
			}
			List<Mat> ret = list.build();
			if (!alwaysInit)
			{
				MATERIALS_CACHE.put(filter, ret);
			}
			return ret;
		}
		else return MATERIALS_CACHE.get(filter);
	}
	
	public Builder builder()
	{
		return isRegistered() ? this.new Builder() : new MatBuilderTest(this);
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
	private IntegerMap<String> properties = new IntegerMap<>();
	private Set<SubTag> subTags = new HashSet<>();
	
	public class Builder
	{
		Builder() {}
		
		public Builder setRGBa(int colorIndex)
		{
			Mat.this.RGBa[0] = (short) ((colorIndex >> 24)       );
			Mat.this.RGBa[1] = (short) ((colorIndex >> 16) & 0xFF);
			Mat.this.RGBa[2] = (short) ((colorIndex >> 8 ) & 0xFF);
			Mat.this.RGBa[3] = (short) ((colorIndex      ) & 0xFF);
			Mat.this.RGB = colorIndex >> 8;
			return this;
		}
		
		public Builder setRGBa(short[] colorIndex)
		{
			Mat.this.RGBa = colorIndex;
			Mat.this.RGB = colorIndex[0] << 16 | colorIndex[1] << 8 | colorIndex[2];
			return this;
		}
		
		public Builder setUnificationMaterial(Mat material)
		{
			Mat.this.unificationMaterial = material;
			return this;
		}
		
		public Builder setChemicalFormula(String name)
		{
			Mat.this.chemicalFormula = name;
			return this;
		}
		
		public Builder setCustomInformation(String info)
		{
			Mat.this.customDisplayInformation = info;
			return this;
		}
		
		public <V> Builder addProperty(IProperty<V> property, V value)
		{
			Mat.this.propertyMap.put(property, value);
			onDataChanged();
			return this;
		}
		
		public Builder setGeneralProp(float heatCap, float thermalConduct, float maxSpeed, float maxTorque, float dielectricConstant, float electrialResistance, float redstoneResistance)
		{
			PropertyBasic property = new PropertyBasic();
			Mat.this.heatCapacity = property.heatCap = heatCap;
			Mat.this.thermalConductivity = property.thermalConduct = thermalConduct;
			Mat.this.dielectricConstant = property.dielectricConstant = dielectricConstant;
			Mat.this.maxSpeed = property.maxSpeed = maxSpeed;
			Mat.this.maxTorque = property.maxTorque = maxTorque;
			Mat.this.electrialResistance = property.electrialResistance = electrialResistance;
			Mat.this.redstoneResistance = property.redstoneResistance = redstoneResistance;
			return addProperty(MP.property_basic, property);
		}
		
		public Builder setToolProp(int maxUses, int harvestLevel, float hardness, float brittleness, float damageToEntity, float attackSpeedMutiple)
		{
			add(SubTags.TOOL);
			Mat.this.toolMaxUse = maxUses;
			Mat.this.toolHarvestLevel = harvestLevel;
			Mat.this.toolHardness = hardness;
			Mat.this.toolBrittleness = brittleness;
			Mat.this.toolDamageToEntity = damageToEntity;
			addProperty(MP.tool_attackspeed, attackSpeedMutiple);
			return this;
		}
		
		public Builder setMetalic(int harvestLevel, float hardness, float resistance)
		{
			MetalBlockBehavior behavior = new MetalBlockBehavior<>(Mat.this, harvestLevel, hardness, resistance);
			behavior.explosionResistance = resistance;
			behavior.hardness = hardness;
			behavior.harvestLevel = harvestLevel;
			behavior.material = Mat.this;
			add(SubTags.METAL);
			return this;
		}
		
		public Builder setToolable(int harvestLevel, int maxUse, float hardness, float brittleness, float attackSpeed, float dVE, int enchantability)
		{
			PropertyTool property = new PropertyTool();
			Mat.this.toolHarvestLevel = property.harvestLevel = harvestLevel;
			Mat.this.toolMaxUse = property.maxUse = maxUse;
			Mat.this.toolHardness = property.hardness = hardness;
			Mat.this.toolBrittleness = property.brittleness = brittleness;
			Mat.this.toolDamageToEntity = property.damageToEntity = dVE;
			property.enchantability = enchantability;
			property.attackSpeed = attackSpeed;
			add(SubTags.TOOL);
			return addProperty(MP.property_tool, property);
		}
		
		@Deprecated
		public Builder setHandable(float toughness)
		{
			//		handleToughness = toughness;
			add(SubTags.HANDLE);
			return this;
		}
		
		public Builder setOreProperty(int harvestLevel, float hardness, float resistance)
		{
			return setOreProperty(harvestLevel, hardness, resistance, SubTags.ORE_SIMPLE);
		}
		
		public Builder setOreProperty(int harvestLevel, float hardness, float resistance, SubTag type)
		{
			return setOreProperty(harvestLevel, hardness, resistance, IOreProperty.PROPERTY, type);
		}
		
		public Builder setOreProperty(int harvestLevel, float hardness, float resistance, IOreProperty oreProperty, SubTag type)
		{
			PropertyOre property;
			if(oreProperty == IOreProperty.PROPERTY)
			{
				property = new PropertyOre(Mat.this, harvestLevel, hardness, resistance);
			}
			else if(oreProperty instanceof PropertyOre)
			{
				property = (PropertyOre) oreProperty;
			}
			else
			{
				property = new PropertyOre.PropertyOreWrapper(Mat.this, harvestLevel, hardness, resistance, oreProperty);
			}
			add(SubTags.ORE, type);
			return addProperty(MP.property_ore, property);
		}
		
		public Builder setWood(float woodHardness, float ashcontent, float woodBurnHeat)
		{
			PropertyWood property = new PropertyWood(Mat.this, 1,
					1.5F + woodHardness / 4F,
					0.4F + woodHardness / 8F,
					ashcontent,
					woodBurnHeat);
			property.plank = new BlockPlank(property);
			new BlockWoodenFence(property);
			addProperty(MP.fallen_damage_deduction, (int) (1000 / (woodHardness + 1)));
			addProperty(MP.flammability, 50);
			addProperty(MP.fire_encouragement, 4);
			addProperty(MP.fire_spread_speed, 25);
			add(SubTags.WOOD);
			return addProperty(MP.property_wood, property);
		}
		
		public Builder setTree(Tree tree)
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
		public Builder setTree(Tree tree, boolean createBlock)
		{
			PropertyWood old = getProperty(MP.property_wood);
			if (old != null)
			{
				tree.plank = old.plank;
				old.burnHeat = tree.burnHeat;
				old.hardness = tree.hardness;
				old.ashcontent = tree.ashcontent;
				old.harvestLevel = tree.harvestLevel;
			}
			add(SubTags.TREE, SubTags.WOOD);
			tree.material = Mat.this;
			if (createBlock)
			{
				BlockLogNatural logNatural = BlockLogNatural.create(tree);
				BlockLogArtificial logArtificial = BlockLogArtificial.create(tree);
				BlockLeaves leaves = BlockLeaves.create(tree);
				BlockLeavesCore coreLeaves = BlockLeavesCore.create(leaves, tree);
				tree.block = logArtificial;
				tree.initInfo(logNatural, logArtificial, leaves, coreLeaves);
			}
			addProperty(MP.fallen_damage_deduction, (int) (1000 / (tree.hardness * 4 - 5)));
			addProperty(MP.flammability, 50);
			addProperty(MP.fire_encouragement, 4);
			addProperty(MP.fire_spread_speed, 25);
			addProperty(MP.property_wood, tree);
			return addProperty(MP.property_tree, tree);
		}
		
		public Builder setBrick(int harvestLevel, float hardness, float resistance)
		{
			PropertyBlockable<BlockBrick> property = new PropertyBlockable<>(Mat.this, harvestLevel, hardness, resistance);
			property.block = new BlockBrick(Mat.this.modid, "brick." + Mat.this.name, Mat.this, property);
			add(SubTags.BRICK);
			return addProperty(MP.property_brick, property);
		}
		
		public Builder setSoil(float hardness, float resistance, Material material)
		{
			PropertyBlockable property = new PropertyBlockable(
					Mat.this,
					-1,//It seems no soil need use tool to harvest.
					hardness,
					resistance);
			property.block = new BlockSoil(Mat.this.modid, "soil." + Mat.this.name, material, Mat.this, property);
			add(SubTags.DIRT);
			return addProperty(MP.property_soil, property);
		}
		
		public Builder setSand(float hardness, float resistance)
		{
			PropertyBlockable property = new PropertyBlockable<>(Mat.this, 1, hardness, resistance);
			property.block = new BlockSand(Mat.this.modid, "sand." + Mat.this.name, Mat.this, property);
			add(SubTags.SAND);
			return addProperty(MP.property_sand, property);
		}
		
		public Builder setRock(RockBehavior behavior)
		{
			behavior.block = new BlockRock(Mat.this, behavior);
			behavior.stonechip = new BlockStoneChip(behavior);
			add(SubTags.ROCK);
			return addProperty(MP.property_rock, behavior);
		}
		public Builder setRock(int harvestLevel, float hardness, float resistance)
		{
			return setRock(new RockBehavior(Mat.this, harvestLevel, hardness, resistance));
		}
		
		public Builder setCrop(ICrop crop)
		{
			add(SubTags.CROP);
			return addProperty(MP.property_crop, crop);
		}
		
		public Builder setPlant(IPlant plant)
		{
			add(SubTags.PLANT);
			return addProperty(MP.property_plant, plant);
		}
		
		public Builder setTag(SubTag...tags)
		{
			add(tags);
			return this;
		}
		
		public Builder addProperty(String tag, int value)
		{
			Mat.this.properties.put(tag, value);
			return this;
		}
		
		public Builder addProperty(String tag, float value)
		{
			return addProperty(tag, Float.floatToIntBits(value));
		}
		
		public Mat build()
		{
			return Mat.this;
		}
	}
	
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
	
	public <V> V getProperty(IProperty<V> property)
	{
		return getProperty(property, property.defValue());
	}
	
	public <V> Optional<? extends V> getPropertyOptional(IProperty<V> property)
	{
		return this.propertyMap.getOptional(property);
	}
	
	public <V> V getProperty(IProperty<V> property, V def)
	{
		V value = this.propertyMap.get(property);
		return value == null ? def : value;
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
		A.executeAll(tags, tag->tag.addContainerToList(this));
		this.subTags.addAll(Arrays.asList(tags));
		onDataChanged();
	}
	
	@Override
	public boolean remove(SubTag tag)
	{
		return this.subTags.remove(tag);
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