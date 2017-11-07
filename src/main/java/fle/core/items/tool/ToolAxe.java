package fle.core.items.tool;

import farcore.data.EnumPhysicalDamageType;
import nebula.common.tool.EnumToolType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class ToolAxe extends Tool
{
	public ToolAxe(EnumToolType type, float speedMultiplier)
	{
		super(type);
		this.damagePerAttack = 2.0F;
		this.speedMultiplier = speedMultiplier;
	}
	
	@Override
	public EnumPhysicalDamageType getPhysicalDamageType()
	{
		return EnumPhysicalDamageType.CUT;
	}
	
	@Override
	protected String getDeathMessage(Entity target, EntityLivingBase user)
	{
		return "(M) has been chopped by (S)";
	}
}
