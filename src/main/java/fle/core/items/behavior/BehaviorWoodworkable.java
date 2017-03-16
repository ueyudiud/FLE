/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.core.items.behavior;

import farcore.data.Materials;
import fle.core.client.gui.GuiWoodworkPortable;
import fle.core.gui.ContainerWoodworkPortable;
import nebula.Nebula;
import nebula.common.item.BehaviorBase;
import nebula.common.item.IItemBehaviorsAndProperties.IIP_Containerable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class BehaviorWoodworkable extends BehaviorBase implements IIP_Containerable
{
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (hand == EnumHand.MAIN_HAND && facing == EnumFacing.UP)
		{
			IBlockState state = world.getBlockState(pos);
			Material material = state.getMaterial();
			if ((material == Materials.ROCK || material == Materials.LOG) && state.isSideSolid(world, pos, EnumFacing.UP))
			{
				player.openGui(Nebula.MODID, -1, world, pos.getX(), pos.getY(), pos.getZ());
				return EnumActionResult.SUCCESS;
			}
		}
		return super.onItemUse(stack, player, world, pos, hand, facing, hitX, hitY, hitZ);
	}
	
	@Override
	public Container openContainer(World world, BlockPos pos, EntityPlayer player, ItemStack stack)
	{
		return new ContainerWoodworkPortable(player, world, pos);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public GuiContainer openGui(World world, BlockPos pos, EntityPlayer player, ItemStack stack)
	{
		return new GuiWoodworkPortable(player, world, pos);
	}
}