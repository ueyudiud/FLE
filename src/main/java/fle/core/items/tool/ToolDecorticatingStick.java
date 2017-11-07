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
public class ToolDecorticatingStick extends Tool
{
	public ToolDecorticatingStick()
	{
		super(EnumToolTypes.DECORTICATING_STICK);
		this.damagePerAttack = 0.8F;
	}
	
	@Override
	protected String getDeathMessage(Entity target, EntityLivingBase user)
	{
		return "(S) decorticated (M) as wheat.";
	}
}
