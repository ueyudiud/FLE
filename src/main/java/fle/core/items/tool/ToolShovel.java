package fle.core.items.tool;

import farcore.data.EnumToolTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class ToolShovel extends Tool
{
	public ToolShovel(float speedMultiplier)
	{
		super(EnumToolTypes.SHOVEL);
		this.speedMultiplier = speedMultiplier;
		this.damagePerBreak = 0.25F;
		this.damagePerAttack = 2.0F;
	}
	
	@Override
	protected String getDeathMessage(Entity target, EntityLivingBase user)
	{
		return "(M) has been uprooted by (S)";
	}
}
