/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.core.items.behavior;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import farcore.data.Materials;
import fle.core.client.gui.GuiBarGrizzly;
import fle.core.common.gui.ContainerBarGrizzly;
import nebula.Nebula;
import nebula.common.LanguageManager;
import nebula.common.item.BehaviorBase;
import nebula.common.item.IItemBehaviorsAndProperties.IIP_Containerable;
import nebula.common.util.Worlds;
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
	public BehaviorBarGrizzly()
	{
		LanguageManager.registerLocal("inventory.bar.grizzly", "Washing");
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
	{
		RayTraceResult result = Worlds.rayTrace(world, player, true);
		if (result != null && result.typeOfHit == Type.BLOCK)
		{
			int count = 0;
			List<BlockPos> checked = new ArrayList<>();
			LinkedList<BlockPos> unchecked = new LinkedList<>();
			unchecked.add(result.getBlockPos());
			while (!unchecked.isEmpty())
			{
				BlockPos pos = unchecked.removeFirst();
				if (checked.contains(pos))
					continue;
				if (world.getBlockState(pos).getMaterial() == Materials.WATER)
				{
					++count;
					if (count == 50) break;
					unchecked.addLast(pos.north());
					unchecked.addLast(pos.south());
					unchecked.addLast(pos.east());
					unchecked.addLast(pos.west());
					unchecked.addLast(pos.down());
				}
				checked.add(pos);
			}
			if (count >= 50)
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