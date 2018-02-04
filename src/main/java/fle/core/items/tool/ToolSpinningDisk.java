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
public class ToolSpinningDisk extends Tool
{
	public ToolSpinningDisk()
	{
		super(EnumToolTypes.SPINNING_TOOL);
		this.speedMultiplier = 0.5F;
		this.damagePerAttack = 1.2F;
		this.durabilityMultiplier = 1.5F;
	}
	
	@Override
	protected String getDeathMessage(Entity target, EntityLivingBase user)
	{
		return "(S) spined (M) and decided to make a new cloth.";
	}
	
}
