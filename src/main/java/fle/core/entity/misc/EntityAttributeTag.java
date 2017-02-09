/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.core.entity.misc;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

/**
 * @author ueyudiud
 */
public class EntityAttributeTag
{
	public static float getFloat(Configuration configuration, String tag, String value, float defValue, float minValue, float maxValue)
	{
		Property property = configuration.get(tag, value, defValue);
		property.setMinValue(minValue);
		property.setMaxValue(maxValue);
		property.setComment(null);
		return (float) property.getDouble();
	}
	
	public final float maxHealth;
	public final float followRange;
	public final float movementSpeed;
	public final float attackDamage;
	public final float armor;
	
	public EntityAttributeTag(Configuration configuration, String tag, String category, float maxHealth, float followRange, float movementSpeed, float attackDamage, float armor)
	{
		this.maxHealth = getFloat(configuration, category, tag + "MaxHealth", maxHealth, 0, 1024);
		this.followRange = getFloat(configuration, category, tag + "FollowRange", followRange, 0, 2048);
		this.movementSpeed = getFloat(configuration, category, tag + "MovementSpeed", movementSpeed, 0, 1024);
		this.attackDamage = getFloat(configuration, category, tag + "AttackDamage", attackDamage, 0, 2048);
		this.armor = getFloat(configuration, category, tag + "Armor", armor, 0, 30);
	}
	
	public EntityAttributeTag(float maxHealth, float followRange, float movementSpeed, float attackDamage, float armor)
	{
		this.maxHealth = maxHealth;
		this.followRange = followRange;
		this.movementSpeed = movementSpeed;
		this.attackDamage = attackDamage;
		this.armor = armor;
	}
}