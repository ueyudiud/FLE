/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.core.items.behavior;

import fle.core.blocks.BlockDirtMixture;
import nebula.common.item.BehaviorBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author ueyudiud
 */
public class BehaviorShovel extends BehaviorBase
{
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (!world.isRemote)
		{
			if (player.isSneaking())
			{
				return BlockDirtMixture.checkAndSetBlock(world, pos.offset(facing.getOpposite())) ?
						EnumActionResult.SUCCESS : EnumActionResult.FAIL;
			}
		}
		return super.onItemUse(stack, player, world, pos, hand, facing, hitX, hitY, hitZ);
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos,
			EntityLivingBase entity)
	{
		return true;
	}
}