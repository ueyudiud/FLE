/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.core.items.tool;

import farcore.data.EnumToolTypes;
import fle.core.items.behavior.BehaviorFirestarter.FirestarterCache;
import nebula.common.capability.CapabilityProviderItem;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public class ToolFirestarter extends Tool
{
	public ToolFirestarter()
	{
		super(EnumToolTypes.FIRESTARTER);
		this.durabilityMultiplier = 5.0F;
		this.damageVsEntity = 0.0F;
	}
	
	@Override
	public boolean canBreakEffective(ItemStack stack, IBlockState state)
	{
		return false;
	}
	
	@Override
	protected String getDeathMessage(Entity target, EntityLivingBase user)
	{
		return "(M) flamed by (S).";
	}
	
	@Override
	public CapabilityProviderItem createProvider()
	{
		return new FirestarterCache();
	}
}
