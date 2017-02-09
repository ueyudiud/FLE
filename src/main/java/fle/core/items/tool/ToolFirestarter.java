/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.core.items.tool;

import farcore.data.EnumToolTypes;
import fle.core.items.behavior.BehaviorFirestarter;
import fle.core.items.behavior.BehaviorFirestarter.FirestarterCache;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

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
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
	{
		return new ICapabilityProvider()
		{
			FirestarterCache cache = new FirestarterCache();
			
			@Override
			public boolean hasCapability(Capability<?> capability, EnumFacing facing)
			{
				return capability == BehaviorFirestarter.capability;
			}
			
			@Override
			public <T> T getCapability(Capability<T> capability, EnumFacing facing)
			{
				return capability == BehaviorFirestarter.capability ?
						BehaviorFirestarter.capability.cast(this.cache) : null;
			}
		};
	}
}