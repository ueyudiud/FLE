package fle.core.items.tool;

import farcore.data.EnumToolType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class ToolAxe extends Tool
{
	private float speedMultiplier;

	public ToolAxe(EnumToolType type, float speedMultiplier)
	{
		super(type);
		this.speedMultiplier = speedMultiplier;
	}
	
	@Override
	public float getSpeedMultiplier(ItemStack stack)
	{
		return speedMultiplier;
	}
	
	@Override
	public float getToolDamagePerAttack(ItemStack stack, EntityLivingBase user, Entity target)
	{
		return 2.0F;
	}
	
	@Override
	protected String getDeathMessage(Entity target, EntityLivingBase user)
	{
		return "(M) has been chopped by (S)";
	}
}