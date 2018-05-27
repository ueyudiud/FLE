/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.data;

import static nebula.base.collection.IPropertyMap.IProperty.to;

import farcore.lib.block.behavior.MetalBlockBehavior;
import farcore.lib.block.behavior.RockBehavior;
import farcore.lib.crop.ICropSpecie;
import farcore.lib.material.prop.PropertyBasic;
import farcore.lib.material.prop.PropertyBlockable;
import farcore.lib.material.prop.PropertyEdible;
import farcore.lib.material.prop.PropertyOre;
import farcore.lib.material.prop.PropertyTool;
import farcore.lib.material.prop.PropertyWood;
import farcore.lib.plant.IPlant;
import farcore.lib.tree.Tree;
import nebula.base.collection.IPropertyMap.IProperty;

/**
 * Material properties.
 * 
 * @author ueyudiud
 */
public class MP
{
	public static final String	fire_encouragement		= "fire_encouragement";
	public static final String	flammability			= "flammability";
	public static final String	light_value				= "light_value";
	public static final String	light_opacity			= "light_opacity";
	public static final String	fire_spread_speed		= "fire_spread_speed";
	public static final String	fallen_damage_deduction	= "fallen_damage_deduction";
	public static final String	tool_attackspeed		= "tool_attackspeed";
	
	public static final IProperty<PropertyBasic>		property_basic			= PropertyBasic::new;
	public static final IProperty<PropertyTool>			property_tool			= to();
	public static final IProperty<MetalBlockBehavior>	property_metal_block	= to();
	public static final IProperty<PropertyOre>			property_ore			= to();
	public static final IProperty<PropertyWood>			property_wood			= to();
	public static final IProperty<Tree>					property_tree			= to(Tree.VOID);
	public static final IProperty<PropertyBlockable>	property_soil			= to();
	public static final IProperty<PropertyBlockable>	property_sand			= to();
	public static final IProperty<RockBehavior>			property_rock			= to();
	public static final IProperty<ICropSpecie>			property_crop			= to(ICropSpecie.VOID);
	public static final IProperty<IPlant>				property_plant			= to();
	public static final IProperty<PropertyEdible>		property_edible			= to();
	public static final IProperty<PropertyBlockable>	property_brick			= to();
}
