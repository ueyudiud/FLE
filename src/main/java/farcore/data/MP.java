/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.data;

import farcore.lib.block.behavior.MetalBlockBehavior;
import farcore.lib.block.behavior.RockBehavior;
import farcore.lib.crop.ICrop;
import farcore.lib.material.prop.PropertyBasic;
import farcore.lib.material.prop.PropertyBlockable;
import farcore.lib.material.prop.PropertyEdible;
import farcore.lib.material.prop.PropertyOre;
import farcore.lib.material.prop.PropertyTool;
import farcore.lib.material.prop.PropertyTree;
import farcore.lib.material.prop.PropertyWood;
import farcore.lib.plant.IPlant;
import nebula.common.base.IPropertyMap.IProperty;

/**
 * Material properties.
 * @author ueyudiud
 */
public class MP
{
	public static final String fire_encouragement = "fire_encouragement";
	public static final String flammability = "flammability";
	public static final String light_value = "light_value";
	public static final String light_opacity = "light_opacity";
	public static final String fire_spread_speed = "fire_spread_speed";
	public static final String fallen_damage_deduction = "fallen_damage_deduction";
	public static final String tool_attackspeed = "tool_attackspeed";
	
	public static final IProperty<PropertyBasic> property_basic = PropertyBasic::new;
	public static final IProperty<PropertyTool> property_tool = () -> null;
	public static final IProperty<MetalBlockBehavior> property_metal_block = () -> null;
	public static final IProperty<PropertyOre> property_ore = PropertyOre::new;
	public static final IProperty<PropertyWood> property_wood = () -> PropertyTree.VOID;
	/**
	 * Only the casting helper.
	 */
	public static final IProperty<PropertyTree> property_tree = (IProperty) property_wood;
	public static final IProperty<PropertyBlockable> property_soil = () -> null;
	public static final IProperty<RockBehavior> property_rock = () -> null;
	public static final IProperty<ICrop> property_crop = () -> ICrop.VOID;
	public static final IProperty<IPlant> property_plant = () -> null;
	public static final IProperty<PropertyEdible> property_edible = () -> null;
}