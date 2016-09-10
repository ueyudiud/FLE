package fle.core.items.tool;

import farcore.data.EnumToolType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class ToolHoe extends Tool
{
	public ToolHoe(EnumToolType type)
	{
		super(type);
	}

	@Override
	public float getMaxDurabilityMultiplier()
	{
		return 1.5F;
	}

	@Override
	protected String getDeathMessage(Entity target, EntityLivingBase user)
	{
		return "(M) has been killed by (S)";
	}
}