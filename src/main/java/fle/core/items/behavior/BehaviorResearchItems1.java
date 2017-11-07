/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.core.items.behavior;

import fle.core.client.gui.research.GuiResearchGraph;
import fle.core.common.gui.research.ContainerResearchGraph;
import fle.core.research.ResearchGraph.ResearchInstance;
import fle.core.research.ResearchGraph.ResearchInstanceProxy;
import fle.core.research.ResearchGraph.ResearchInstanceProxyClient;
import nebula.Nebula;
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
public class BehaviorResearchItems1 extends BehaviorBase implements IIP_Containerable
{
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
	{
		player.openGui(Nebula.MODID, -1, world, (int) player.posX, (int) player.posY, (int) player.posZ);
		return new ActionResult<>(EnumActionResult.SUCCESS, stack);
	}
	
	@Override
	public Container openContainer(World world, BlockPos pos, EntityPlayer player, ItemStack stack)
	{
		return new ContainerResearchGraph(new ResearchInstanceProxy(), player, world, pos);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public GuiContainer openGui(World world, BlockPos pos, EntityPlayer player, ItemStack stack)
	{
		return new GuiResearchGraph(generateProxy(player), player, world, pos);
	}
	
	@SideOnly(Side.CLIENT)
	private ResearchInstanceProxyClient generateProxy(EntityPlayer player)
	{
		int[][] points = { { 5, 0 }, { 15, 0 }, { 0, 10 }, { 20, 10 }, { 5, 20 }, { 15, 20 } };
		int[][] links = { { 0, 1 }, { 0, 2 }, { 0, 3 }, { 0, 4 }, { 0, 5 }, { 1, 2 }, { 1, 3 }, { 1, 4 }, { 1, 5 }, { 2, 3 }, { 2, 4 }, { 2, 5 }, { 3, 4 }, { 3, 5 }, { 4, 5 } };
		ResearchInstance instance = new ResearchInstance(points, links);
		return new ResearchInstanceProxyClient(player, instance);
	}
}
