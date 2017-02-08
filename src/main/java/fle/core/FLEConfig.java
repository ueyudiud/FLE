/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.core;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import fle.core.entity.misc.EntityAttributeTag;
import nebula.common.base.IntegerMap;
import nebula.common.util.L;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

/**
 * @author ueyudiud
 */
public class FLEConfig
{
	private static final IntegerMap<String> intValueMap = new IntegerMap<>();
	private static final Map<String, EntityAttributeTag> entityAttributesMap = new HashMap<>();
	
	public static void init(Configuration config)
	{
		setValue(config, "Zombie", 200.0F, 40.0F, 0.24F, 4.0F, 5.0F);
		setValue(config, "Skeleton", 200.0F, 20.0F, 0.25F, 4.0F, 5.0F);
		setValue(config, "Spider", 160.0F, 20.0F, 0.3F, 3.0F, 0.0F);
		setValue(config, "Creeper", 200.0F, 20.0F, 0.25F, 3.0F, 0.0F);
	}
	
	public static void setValue(Configuration configuration, String name, float maxHealth, float followRange, float movementSpeed, float attackDamage, float armor)
	{
		entityAttributesMap.put(name, new EntityAttributeTag(configuration, name, "Entities", maxHealth, followRange, movementSpeed, attackDamage, armor));
	}
	
	public static void setValue(Configuration configuration, String tag, String value, int defValue, int minValue, int maxValue, @Nullable String comment)
	{
		Property property = configuration.get(tag, value, defValue);
		if (comment != null)
		{
			property.setComment(String.format("%s(%d~%d)", comment, minValue, maxValue));
		}
		property.setMinValue(minValue);
		property.setMaxValue(maxValue);
		intValueMap.put(tag + "." + value, L.range(minValue, maxValue, property.getInt()));
	}
	
	public static void setValue(Configuration configuration, String tag, String value, float defValue, float minValue, float maxValue, @Nullable String comment)
	{
		Property property = configuration.get(tag, value, defValue);
		if (comment != null)
		{
			property.setComment(String.format("%s(%d~%d)", comment, minValue, maxValue));
		}
		property.setMinValue(minValue);
		property.setMaxValue(maxValue);
		intValueMap.put(tag + "." + value, Float.floatToIntBits(L.range(minValue, maxValue, property.getInt())));
	}
	
	public static EntityAttributeTag getEntityAttributeTag(String name)
	{
		return entityAttributesMap.get(name);
	}
	
	public static int getIntValue(String tag, String value)
	{
		return intValueMap.get(tag + "." + value);
	}
	
	public static float getFloatValue(String tag, String value)
	{
		return Float.intBitsToFloat(intValueMap.get(tag + "." + value));
	}
}