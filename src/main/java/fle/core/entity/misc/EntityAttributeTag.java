/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.core.entity.misc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ueyudiud
 */
public class EntityAttributeTag
{
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public static @interface Attribute
	{
		float health();
		
		float followRange() default 0.0F;
		
		float speed();
		
		float attack();
		
		float armor();
	}
	
	public float	maxHealth;
	public float	followRange;
	public float	movementSpeed;
	public float	attackDamage;
	public float	armor;
	
	public EntityAttributeTag()
	{
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
