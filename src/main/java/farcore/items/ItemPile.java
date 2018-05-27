/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.items;

import farcore.blocks.terria.BlockSand;
import farcore.data.EnumItem;
import farcore.data.MC;
import farcore.data.MP;
import farcore.data.SubTags;
import farcore.lib.item.ItemMulti;
import farcore.lib.material.Mat;
import farcore.lib.material.prop.PropertyBlockable;
import nebula.common.util.W;
import net.minecraft.block.state.IBlockState;
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
public class ItemPile extends ItemMulti
{
	public ItemPile()
	{
		super(MC.pile);
		EnumItem.pile.set(this);
	}
	
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (!playerIn.canPlayerEdit(pos, facing, stack)) return EnumActionResult.FAIL;
		Mat material = getMaterialFromItem(stack);
		PropertyBlockable<?> property;
		if (material.contain(SubTags.SAND))
		{
			property = material.getProperty(MP.property_sand);
			IBlockState state1 = worldIn.getBlockState(pos);
			if (state1.getBlock() == property.block)
			{
				int amt = BlockSand.fillBlockWith(worldIn, pos, state1, stack.stackSize);
				if (amt > 0)
				{
					stack.stackSize -= amt;
					return EnumActionResult.SUCCESS;
				}
			}
		}
		if (material.contain(SubTags.DIRT) && stack.stackSize >= 9)
		{
			property = material.getProperty(MP.property_soil);
			if (W.checkAndPlaceBlockAt(worldIn, pos, facing, playerIn, stack, property.block.getDefaultState(), false) == EnumActionResult.SUCCESS)
			{
				stack.stackSize -= 9;
				return EnumActionResult.SUCCESS;
			}
		}
		else if (material.contain(SubTags.SAND))
		{
			int use = Math.min(16, stack.stackSize);
			property = material.getProperty(MP.property_sand);
			if (W.checkAndPlaceBlockAt(worldIn, pos, facing, playerIn, stack, property.block.getDefaultState().withProperty(BlockSand.LAYER, use), false) == EnumActionResult.SUCCESS)
			{
				stack.stackSize -= use;
				return EnumActionResult.SUCCESS;
			}
		}
		return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}
}
