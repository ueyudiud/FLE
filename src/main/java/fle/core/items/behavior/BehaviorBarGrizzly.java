/*
 * copyright 2016-2018 ueyudiud
 */
package fle.core.items.behavior;

import fle.core.client.gui.GuiBarGrizzly;
import fle.core.common.gui.ContainerBarGrizzly;
import nebula.Nebula;
import nebula.common.LanguageManager;
import nebula.common.item.BehaviorBase;
import nebula.common.item.IItemBehaviorsAndProperties.IIP_Containerable;
import nebula.common.util.W;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class BehaviorBarGrizzly extends BehaviorBase implements IIP_Containerable
{
	{
		LanguageManager.registerLocal("inventory.bar.grizzly", "Washing");
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
	{
		RayTraceResult result = W.rayTrace(world, player, true);
		if (result != null && result.typeOfHit == Type.BLOCK)
		{
			if (W.checkForMatch(world, result.getBlockPos(), true, 50, coord -> coord.getBlockState().getMaterial() == Material.WATER))
			{
				player.openGui(Nebula.MODID, -1, world, result.getBlockPos().getX(), result.getBlockPos().getY(), result.getBlockPos().getZ());
				return new ActionResult<>(EnumActionResult.SUCCESS, stack);
			}
		}
		return super.onItemRightClick(stack, world, player, hand);
	}
	
	@Override
	public Container openContainer(World world, BlockPos pos, EntityPlayer player, ItemStack stack)
	{
		return new ContainerBarGrizzly(player, world, pos);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public GuiContainer openGui(World world, BlockPos pos, EntityPlayer player, ItemStack stack)
	{
		return new GuiBarGrizzly(player, world, pos);
	}
}
