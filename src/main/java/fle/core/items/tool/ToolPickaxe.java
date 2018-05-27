/*
 * copyright 2016-2018 ueyudiud
 */
package fle.core.items.tool;

import farcore.data.EnumToolTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

/**
 * @author ueyudiud
 */
public class ToolPickaxe extends Tool
{
	public ToolPickaxe()
	{
		super(EnumToolTypes.PICKAXE);
		this.damagePerAttack = 2.0F;
		this.damageVsEntity = 1.5F;
	}
	
	@Override
	protected String getDeathMessage(Entity target, EntityLivingBase user)
	{
		return "(M) has been mined by (S).";
	}
}
