package fle.core.items.tool;

import farcore.data.EnumToolTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class ToolHardHammer extends Tool
{
	public ToolHardHammer(float speedMultiplier)
	{
		super(EnumToolTypes.HAMMER_DIGABLE);
		this.damagePerAttack = 2.0F;
		this.speedMultiplier = speedMultiplier;
	}
	
	@Override
	public float getAttackSpeed(ItemStack stack, float mutiplier)
	{
		return -2.5F + mutiplier * 0.4F;
	}
	
	@Override
	protected String getDeathMessage(Entity target, EntityLivingBase user)
	{
		return "(M) has been crushed by (S)";
	}
}