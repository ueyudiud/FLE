package fle.core.items.tool;

import nebula.common.tool.EnumToolType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class ToolHoe extends Tool
{
	public ToolHoe(EnumToolType type)
	{
		super(type);
		this.speedMultiplier = 0.2F;
		this.damagePerAttack = 1.5F;
		this.durabilityMultiplier = 1.5F;
	}
	
	@Override
	protected String getDeathMessage(Entity target, EntityLivingBase user)
	{
		return "(M) has been killed by (S)";
	}
}