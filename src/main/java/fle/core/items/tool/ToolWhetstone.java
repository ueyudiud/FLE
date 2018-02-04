/*
 * copyright© 2016-2018 ueyudiud
 */

package fle.core.items.tool;

import farcore.data.EnumToolTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

/**
 * @author ueyudiud
 */
public class ToolWhetstone extends Tool
{
	public ToolWhetstone()
	{
		super(EnumToolTypes.WHESTONE);
		this.damageVsEntity = 1.0F;
		this.damagePerAttack = 0.8F;
		this.damagePerBreak = 1.2F;
		this.durabilityMultiplier = 2.0F;
	}
	
	@Override
	protected String getDeathMessage(Entity target, EntityLivingBase user)
	{
		return "(M) hited by (S).";
	}
}
