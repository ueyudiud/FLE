package fle.core.items.tool;

import farcore.data.EnumToolType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class ToolHardHammer extends Tool
{
	private float speedMultiplier;

	public ToolHardHammer(float speedMultiplier)
	{
		super(EnumToolType.hammer_digable);
		this.speedMultiplier = speedMultiplier;
	}

	@Override
	public float getAttackSpeed(ItemStack stack)
	{
		return -0.2F;
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
		return "(M) has been crushed by (S)";
	}
}