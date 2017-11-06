/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.core.items.behavior;

import fle.core.blocks.BlockTools;
import fle.loader.IBFS;
import nebula.client.util.UnlocalizedList;
import nebula.common.item.BehaviorBase;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_BlockPlacedBy;
import nebula.common.util.Direction;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class BehaviorBlockableTool extends BehaviorBase
{
	private int meta;
	
	public BehaviorBlockableTool(int meta)
	{
		this.meta = meta;
	}
	
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		IBlockState iblockstate = world.getBlockState(pos);
		Block block = iblockstate.getBlock();
		
		if (!block.isReplaceable(world, pos))
		{
			pos = pos.offset(facing);
		}
		
		if (stack.stackSize != 0 && player.canPlayerEdit(pos, facing, stack) && world.canBlockBePlaced(IBFS.bTool, pos, false, facing, (Entity) null, stack))
		{
			IBlockState state = ((BlockTools) IBFS.bTool).property_TE.withProperty(IBFS.bTool.getDefaultState(), this.meta);
			
			if (placeBlockAt(stack, player, world, pos, facing, hitX, hitY, hitZ, state))
			{
				SoundType soundtype = IBFS.bTool.getSoundType();
				world.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
				stack.stackSize --;
			}
			
			return EnumActionResult.SUCCESS;
		}
		else
			return EnumActionResult.FAIL;
	}
	
	/**
	 * Called to actually place the block, after the location is determined
	 * and all permission checks have been made.
	 *
	 * @param stack The item stack that was used to place the block. This can be changed inside the method.
	 * @param player The player who is placing the block. Can be null if the block is not being placed by a player.
	 * @param side The side the player (or machine) right-clicked on.
	 */
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState)
	{
		if (!world.setBlockState(pos, newState, 3)) return false;
		
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock() == IBFS.bTool)
		{
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof ITB_BlockPlacedBy)
			{
				((ITB_BlockPlacedBy) tile).onBlockPlacedBy(state, player, Direction.of(side), stack);
			}
		}
		
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, UnlocalizedList list, boolean advanced)
	{
		super.addInformation(stack, player, list, advanced);
		list.add("info.blockable.tool.place");
	}
}