/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.core.items.behavior;

import fle.core.client.gui.GuiCeramics;
import fle.core.common.gui.ContainerCeramics;
import nebula.Nebula;
import nebula.common.LanguageManager;
import nebula.common.item.BehaviorBase;
import nebula.common.item.IItemBehaviorsAndProperties.IIP_Containerable;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class BehaviorArgilBall extends BehaviorBase implements IIP_Containerable
{
	{
		LanguageManager.registerLocal("inventory.ceramics", "Ceramics");
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
	{
		if (hand == EnumHand.MAIN_HAND && stack.stackSize >= 6)
		{
			stack.stackSize -= 6;
			if (!player.world.isRemote)
			{
				player.openGui(Nebula.MODID, -1, world, (int) player.posX, (int) player.posY, (int) player.posZ);
			}
			return new ActionResult<>(EnumActionResult.SUCCESS, stack);
		}
		return new ActionResult<>(EnumActionResult.PASS, stack);
	}
	
	@Override
	public Container openContainer(World world, BlockPos pos, EntityPlayer player, ItemStack stack)
	{
		return new ContainerCeramics(player, world, pos);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public GuiContainer openGui(World world, BlockPos pos, EntityPlayer player, ItemStack stack)
	{
		return new GuiCeramics(player, world, pos);
	}
}
