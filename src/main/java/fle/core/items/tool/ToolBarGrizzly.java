/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.core.items.tool;

import farcore.data.EnumToolTypes;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public class ToolBarGrizzly extends Tool
{
	public ToolBarGrizzly()
	{
		super(EnumToolTypes.BAR_GRIZZLY);
		this.durabilityMultiplier = 1.5F;
		this.damagePerAttack = 3.0F;
		this.damagePerBreak = 5.0F;
	}
	
	@Override
	public boolean canBreakEffective(ItemStack stack, IBlockState state)
	{
		return state.getMaterial().isToolNotRequired();
	}
	
	@Override
	protected String getDeathMessage(Entity target, EntityLivingBase user)
	{
		return "How does (M) killed by (S) by some stick and rope?";
	}
}