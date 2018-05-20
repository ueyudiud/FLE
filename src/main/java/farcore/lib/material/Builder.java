/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.material;

import farcore.blocks.BlockBrick;
import farcore.blocks.flora.BlockLeaves;
import farcore.blocks.flora.BlockLeavesCore;
import farcore.blocks.flora.BlockLogArtificial;
import farcore.blocks.flora.BlockLogNatural;
import farcore.blocks.flora.BlockPlank;
import farcore.blocks.flora.BlockWoodenFence;
import farcore.blocks.terria.BlockClay;
import farcore.blocks.terria.BlockRock;
import farcore.blocks.terria.BlockSand;
import farcore.blocks.terria.BlockSoil;
import farcore.blocks.terria.BlockStoneChip;
import farcore.data.MP;
import farcore.data.Materials;
import farcore.data.SubTags;
import farcore.lib.block.behavior.MetalBlockBehavior;
import farcore.lib.block.behavior.RockBehavior;
import farcore.lib.crop.ICropSpecie;
import farcore.lib.material.behavior.IMaterialProperty;
import farcore.lib.material.behavior.MaterialPropertyManager;
import farcore.lib.material.ore.IOreProperty;
import farcore.lib.material.prop.PropertyBasic;
import farcore.lib.material.prop.PropertyBlockable;
import farcore.lib.material.prop.PropertyOre;
import farcore.lib.material.prop.PropertyTool;
import farcore.lib.material.prop.PropertyWood;
import farcore.lib.plant.IPlant;
import farcore.lib.tree.Tree;
import nebula.base.collection.IPropertyMap.IProperty;
import nebula.common.util.SubTag;

/**
 * @author ueyudiud
 */
public class Builder
{
	private final Mat material;
	
	Builder(Mat mat)
	{
		this.material = mat;
	}
	
	public Builder setRGBa(int colorIndex)
	{
		this.material.RGBa[0] = (short) ((colorIndex >> 24));
		this.material.RGBa[1] = (short) ((colorIndex >> 16) & 0xFF);
		this.material.RGBa[2] = (short) ((colorIndex >> 8) & 0xFF);
		this.material.RGBa[3] = (short) ((colorIndex) & 0xFF);
		this.material.RGB = colorIndex >> 8;
		return this;
	}
	
	public Builder setRGBa(short[] colorIndex)
	{
		this.material.RGBa = colorIndex;
		this.material.RGB = colorIndex[0] << 16 | colorIndex[1] << 8 | colorIndex[2];
		return this;
	}
	
	public Builder setUnificationMaterial(Mat material)
	{
		material.unificationMaterial = material;
		return this;
	}
	
	public Builder setChemicalFormula(String name)
	{
		this.material.chemicalFormula = name;
		return this;
	}
	
	public Builder setCustomInformation(String info)
	{
		this.material.customDisplayInformation = info;
		return this;
	}
	
	public <V> Builder addProperty(IProperty<V> property, V value)
	{
		this.material.propertyMap.put(property, value);
		Mat.onDataChanged();
		return this;
	}
	
	public Builder setGeneralProp(float heatCap, float thermalConduct, float maxSpeed, float maxTorque, float dielectricConstant, float electrialResistance, float redstoneResistance)
	{
		PropertyBasic property = new PropertyBasic();
		this.material.heatCapacity = property.heatCap = heatCap;
		this.material.thermalConductivity = property.thermalConduct = thermalConduct;
		this.material.dielectricConstant = property.dielectricConstant = dielectricConstant;
		this.material.maxSpeed = property.maxSpeed = maxSpeed;
		this.material.maxTorque = property.maxTorque = maxTorque;
		this.material.electrialResistance = property.electrialResistance = electrialResistance;
		this.material.redstoneResistance = property.redstoneResistance = redstoneResistance;
		return addProperty(MP.property_basic, property);
	}
	
	public Builder setToolProp(int maxUses, int harvestLevel, float hardness, float brittleness, float damageToEntity, float attackSpeedMutiple)
	{
		this.material.add(SubTags.TOOL);
		this.material.toolMaxUse = maxUses;
		this.material.toolHarvestLevel = harvestLevel;
		this.material.toolHardness = hardness;
		this.material.toolBrittleness = brittleness;
		this.material.toolDamageToEntity = damageToEntity;
		addProperty(MP.tool_attackspeed, attackSpeedMutiple);
		return this;
	}
	
	public Builder setMetalic(int harvestLevel, float hardness, float resistance)
	{
		MetalBlockBehavior behavior = new MetalBlockBehavior<>(this.material, harvestLevel, hardness, resistance);
		behavior.explosionResistance = resistance;
		behavior.hardness = hardness;
		behavior.harvestLevel = harvestLevel;
		behavior.material = this.material;
		this.material.add(SubTags.METAL);
		return this;
	}
	
	public Builder setToolable(int harvestLevel, int maxUse, float hardness, float brittleness, float attackSpeed, float dVE, int enchantability)
	{
		PropertyTool property = new PropertyTool();
		this.material.toolHarvestLevel = property.harvestLevel = harvestLevel;
		this.material.toolMaxUse = property.maxUse = maxUse;
		this.material.toolHardness = property.hardness = hardness;
		this.material.toolBrittleness = property.brittleness = brittleness;
		this.material.toolDamageToEntity = property.damageToEntity = dVE;
		property.enchantability = enchantability;
		property.attackSpeed = attackSpeed;
		this.material.add(SubTags.TOOL);
		return addProperty(MP.property_tool, property);
	}
	
	@Deprecated
	public Builder setHandable(float toughness)
	{
		// handleToughness = toughness;
		this.material.add(SubTags.HANDLE);
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
		if (oreProperty == IOreProperty.PROPERTY)
		{
			property = new PropertyOre(this.material, harvestLevel, hardness, resistance);
		}
		else if (oreProperty instanceof PropertyOre)
		{
			property = (PropertyOre) oreProperty;
		}
		else
		{
			property = new PropertyOre.PropertyOreWrapper(this.material, harvestLevel, hardness, resistance, oreProperty);
		}
		this.material.add(SubTags.ORE, type);
		return addProperty(MP.property_ore, property);
	}
	
	public Builder setWood(float woodHardness, float ashcontent, float woodBurnHeat)
	{
		PropertyWood property = new PropertyWood(this.material, 1, 1.5F + woodHardness / 4F, 0.4F + woodHardness / 8F, ashcontent, woodBurnHeat);
		property.plank = new BlockPlank(property);
		new BlockWoodenFence(property);
		addProperty(MP.fallen_damage_deduction, (int) (1000 / (woodHardness + 1)));
		addProperty(MP.flammability, 50);
		addProperty(MP.fire_encouragement, 4);
		addProperty(MP.fire_spread_speed, 25);
		this.material.add(SubTags.WOOD);
		return addProperty(MP.property_wood, property);
	}
	
	public Builder setTree(Tree tree)
	{
		return setTree(tree, true);
	}
	
	/**
	 * Set tree information of material.
	 * 
	 * @param tree The tree information.
	 * @param createBlock False to prevent add log and leaves block, you may
	 *            have other block to added, this option is only input false
	 *            in VOID material in FarCore.
	 * @return
	 */
	public Builder setTree(Tree tree, boolean createBlock)
	{
		this.material.add(SubTags.TREE);
		if (createBlock)
		{
			BlockLogNatural logNatural = BlockLogNatural.create(tree);
			BlockLogArtificial logArtificial = BlockLogArtificial.create(tree);
			BlockLeaves leaves = BlockLeaves.create(tree);
			BlockLeavesCore coreLeaves = BlockLeavesCore.create(leaves, tree);
			PropertyWood old = this.material.getProperty(MP.property_wood);
			if (old != null)
			{
				old.block = logArtificial;
			}
			tree.initInfo(logNatural, logArtificial, leaves, coreLeaves);
		}
		addProperty(MP.fallen_damage_deduction, 2000);
		addProperty(MP.flammability, 50);
		addProperty(MP.fire_encouragement, 4);
		addProperty(MP.fire_spread_speed, 25);
		return addProperty(MP.property_tree, tree);
	}
	
	public Builder setBrick(int harvestLevel, float hardness, float resistance)
	{
		PropertyBlockable<BlockBrick> property = new PropertyBlockable<>(this.material, harvestLevel, hardness, resistance);
		property.block = new BlockBrick(this.material.modid, "brick." + this.material.name, this.material, property);
		this.material.add(SubTags.BRICK);
		return addProperty(MP.property_brick, property);
	}
	
	public Builder setSoil(float hardness, float resistance)
	{
		// It seems no soil need use tool to harvest.
		PropertyBlockable property = new PropertyBlockable(this.material, -1, hardness, resistance);
		property.block = new BlockSoil(this.material.modid, "soil." + this.material.name, Materials.DIRT, this.material, property);
		this.material.add(SubTags.DIRT);
		return addProperty(MP.property_soil, property);
	}
	
	public Builder setClay(float hardness, float resistance)
	{
		// It seems no clay need use tool to harvest.
		PropertyBlockable property = new PropertyBlockable(this.material, -1, hardness, resistance);
		property.block = new BlockClay(this.material.modid, "soil." + this.material.name, Materials.CLAY, this.material, property);
		this.material.add(SubTags.CLAY);
		return addProperty(MP.property_soil, property);
	}
	
	public Builder setSand(float hardness, float resistance)
	{
		PropertyBlockable property = new PropertyBlockable<>(this.material, 1, hardness, resistance);
		property.block = new BlockSand(this.material.modid, "sand." + this.material.name, this.material, property);
		this.material.add(SubTags.SAND);
		return addProperty(MP.property_sand, property);
	}
	
	public Builder setRock(RockBehavior behavior)
	{
		behavior.block = new BlockRock(this.material, behavior);
		behavior.stonechip = new BlockStoneChip(behavior);
		this.material.add(SubTags.ROCK);
		return addProperty(MP.property_rock, behavior);
	}
	
	public Builder setRock(int harvestLevel, float hardness, float resistance)
	{
		return setRock(new RockBehavior(this.material, harvestLevel, hardness, resistance));
	}
	
	public Builder setCrop(ICropSpecie crop)
	{
		this.material.add(SubTags.CROP);
		return addProperty(MP.property_crop, crop);
	}
	
	public Builder setPlant(IPlant plant)
	{
		this.material.add(SubTags.PLANT);
		return addProperty(MP.property_plant, plant);
	}
	
	public Builder setTag(SubTag...tags)
	{
		this.material.add(tags);
		return this;
	}
	
	public Builder addProperty(String tag, int value)
	{
		this.material.properties.put(tag, value);
		return this;
	}
	
	public Builder addProperty(String tag, float value)
	{
		return addProperty(tag, Float.floatToIntBits(value));
	}
	
	public Builder setProperty(IMaterialProperty property)
	{
		return setProperty(property, false);
	}
	
	/**
	 * Set {@link Mat#itemProp}.<p>
	 * 
	 * @param property the material property.
	 * @param register the property will register to {@link MaterialPropertyManager} with material name
	 *                 if this option is <code>true</code>
	 * @return the builder.
	 * @see MaterialPropertyManager#registerMaterialProperty(String, IMaterialProperty)
	 */
	public Builder setProperty(IMaterialProperty property, boolean register)
	{
		this.material.itemProp = property;
		if (register)
		{
			MaterialPropertyManager.registerMaterialProperty(this.material.name, property);
		}
		return this;
	}
	
	public Mat build()
	{
		return this.material;
	}
}