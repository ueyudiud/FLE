/*
 * copyright 2016-2018 ueyudiud
 */
package fle.core.items.behavior;

import nebula.common.item.BehaviorBase;
import nebula.common.util.W;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BehaviorBlockable extends BehaviorBase
{
	public final Block	block;
	public final int	meta;
	public int			usePerBlock;
	
	public BehaviorBlockable(Block block, int usePerBlock)
	{
		this(block, 0, usePerBlock);
	}
	
	public BehaviorBlockable(Block block, int meta, int usePerBlock)
	{
		this.block = block;
		this.meta = meta;
		this.usePerBlock = usePerBlock;
	}
	
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (stack.stackSize < this.usePerBlock)
			return EnumActionResult.PASS;
		else
		{
			EnumActionResult result = W.checkAndPlaceBlockAt(world, pos, facing, player, stack, this.block.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, this.meta, player, stack), false);
			if (result == EnumActionResult.SUCCESS)
			{
				stack.stackSize -= this.usePerBlock;
			}
			return result;
		}
	}
}
