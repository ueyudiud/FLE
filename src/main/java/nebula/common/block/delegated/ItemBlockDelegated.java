/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.common.block.delegated;

import nebula.common.block.ItemBlockBase;
import nebula.common.world.ICoord;
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
public class ItemBlockDelegated extends ItemBlockBase
{
	public ItemBlockDelegated(BlockDelegated block)
	{
		super(block);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		ITileDelegate delegate = ((BlockDelegated) this.block).delegateOf(stack);
		return delegate.getUnlocalizedName(this.block, stack);
	}
	
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		ITileDelegate delegate = ((BlockDelegated) this.block).delegateOf(stack);
		if (delegate instanceof ITileDelegate.ITD_ItemUse)
		{
			return ((ITileDelegate.ITD_ItemUse) delegate).onItemUse(this.block, worldIn, ICoord.create(worldIn, pos), stack, playerIn, hand, facing, hitX, hitY, hitZ);
		}
		else
		{
			return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
		}
	}
}
