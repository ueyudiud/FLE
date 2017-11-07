/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.foodstat;

import nebula.common.util.IRegisteredNameable;

/**
 * The nutrition type.
 * 
 * @author ueyudiud
 */
public enum EnumNutrition implements IRegisteredNameable
{
	GRAIN("Grain"), VEGETABLE("Vegetable"), FRUIT("Fruit"), PROTEIN("Protein"), DAIRY("Dairy"), SALT("Salt");
	
	public static final int length = values().length;
	
	String name;
	
	EnumNutrition(String name)
	{
		this.name = name;
	}
	
	@Override
	public String getRegisteredName()
	{
		return this.name;
	}
}
