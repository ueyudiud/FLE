/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.block;

import farcore.data.EnumSlabState;
import nebula.common.block.BlockBase;
import nebula.common.block.ItemBlockBase;
import nebula.common.util.Direction;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBlockSlab extends ItemBlockBase
{
	public ItemBlockSlab(BlockBase block)
	{
		super(block);
	}
	
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		IBlockState iblockstate = worldIn.getBlockState(pos);
		Block block = iblockstate.getBlock();
		
		if (block == this.block)
		{
			EnumSlabState slabState = iblockstate.getValue(EnumSlabState.PROPERTY);
			if (Direction.OPPISITE[facing.ordinal()] == slabState.ordinal())
			{
				if (stack.stackSize == 0 || !playerIn.canPlayerEdit(pos, facing, stack) || pos.getY() == 255) return EnumActionResult.FAIL;
				EnumSlabState state = EnumSlabState.DOUBLE_UD;
				switch (slabState)
				{
				case NORTH:
				case SOUTH:
					state = EnumSlabState.DOUBLE_NS;
					break;
				case EAST:
				case WEST:
					state = EnumSlabState.DOUBLE_WE;
					break;
				default:
					break;
				}
				if (worldIn.setBlockState(pos, iblockstate.withProperty(EnumSlabState.PROPERTY, state), 3))
				{
					SoundType soundtype = this.block.getSoundType();
					worldIn.playSound(playerIn, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
					--stack.stackSize;
					return EnumActionResult.SUCCESS;
				}
				return EnumActionResult.FAIL;
			}
		}
		if (!block.isReplaceable(worldIn, pos)) pos = pos.offset(facing);
		
		if (stack.stackSize != 0 && playerIn.canPlayerEdit(pos, facing, stack) && worldIn.canBlockBePlaced(this.block, pos, false, facing, (Entity) null, stack))
		{
			int i = this.getMetadata(stack.getMetadata());
			IBlockState iblockstate1 = this.block.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, i, playerIn);
			
			if (placeBlockAt(stack, playerIn, worldIn, pos, facing, hitX, hitY, hitZ, iblockstate1))
			{
				SoundType soundtype = this.block.getSoundType();
				worldIn.playSound(playerIn, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
				--stack.stackSize;
			}
			return EnumActionResult.SUCCESS;
		}
		else
			return EnumActionResult.FAIL;
	}
}
