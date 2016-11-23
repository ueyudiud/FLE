/*
 * copyright© 2016 ueyudiud
 */

package farcore.data;

import farcore.lib.collection.IPropertyMap.IProperty;
import farcore.lib.crop.ICrop;
import farcore.lib.material.prop.PropertyBasic;
import farcore.lib.material.prop.PropertyBlockable;
import farcore.lib.material.prop.PropertyOre;
import farcore.lib.material.prop.PropertyRock;
import farcore.lib.material.prop.PropertyTool;
import farcore.lib.material.prop.PropertyWood;
import farcore.lib.plant.IPlant;

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

	public static final IProperty<PropertyBasic> property_basic = new IProperty<PropertyBasic>() { @Override public PropertyBasic defValue() { return new PropertyBasic(); } };
	public static final IProperty<PropertyTool> property_tool = new IProperty<PropertyTool>(){};
	public static final IProperty<PropertyOre> property_ore = new IProperty<PropertyOre>(){@Override public PropertyOre defValue() { return new PropertyOre(); } };
	public static final IProperty<PropertyWood> property_wood = new IProperty<PropertyWood>(){};
	public static final IProperty<PropertyBlockable> property_soil = new IProperty<PropertyBlockable>(){};
	public static final IProperty<PropertyRock> property_rock = new IProperty<PropertyRock>(){};
	public static final IProperty<ICrop> property_crop = new IProperty<ICrop>(){@Override public ICrop defValue() { return ICrop.VOID; } };
	public static final IProperty<IPlant> property_plant = new IProperty<IPlant>(){};
}