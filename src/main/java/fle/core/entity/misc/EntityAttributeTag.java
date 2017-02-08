/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.core.entity.misc;

import net.minecraftforge.common.config.Configuration;

/**
 * @author ueyudiud
 */
public class EntityAttributeTag
{
	public final float maxHealth;
	public final float followRange;
	public final float movementSpeed;
	public final float attackDamage;
	public final float armor;
	
	public EntityAttributeTag(Configuration configuration, String tag, String category, float maxHealth, float followRange, float movementSpeed, float attackDamage, float armor)
	{
		this.maxHealth = configuration.getFloat(tag + "MaxHealth", category, maxHealth, 0, 1024, null);
		this.followRange = configuration.getFloat(tag + "FollowRange", category, followRange, 0, 2048, null);
		this.movementSpeed = configuration.getFloat(tag + "MovementSpeed", category, movementSpeed, 0, 1024, null);
		this.attackDamage = configuration.getFloat(tag + "AttackDamage", category, attackDamage, 0, 2048, null);
		this.armor = configuration.getFloat(tag + "Armor", category, armor, 0, 30, null);
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