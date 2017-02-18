/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.core.items.tool;

import farcore.data.EnumToolTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

/**
 * @author ueyudiud
 */
public class ToolAwl extends Tool
{
	public ToolAwl()
	{
		super(EnumToolTypes.AWL);
		this.damagePerAttack = 2.0F;
		this.damagePerBreak = 1.2F;
		this.damageVsEntity = 3.0F;
		this.durabilityMultiplier = 2.0F;
	}
	
	@Override
	protected String getDeathMessage(Entity target, EntityLivingBase user)
	{
		return "(M)'s brain already bored a hole by (S).";
	}
}