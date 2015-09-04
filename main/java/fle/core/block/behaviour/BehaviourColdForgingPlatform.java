package fle.core.block.behaviour;

import java.util.ArrayList;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import fle.core.block.BlockSubTile;
import fle.core.gui.ContainerColdForging;
import fle.core.gui.GuiColdForging;
import fle.core.item.ItemFleSub;
import fle.core.te.TileEntityColdForgingPlatform;

public class BehaviourColdForgingPlatform extends BehaviourTile
{
	public BehaviourColdForgingPlatform() 
	{
		super("coldForgingPlatform", TileEntityColdForgingPlatform.class);
	}

	@Override
	public Container openContainer(World aWorld, int x, int y, int z,
			EntityPlayer aPlayer)
	{
		return new ContainerColdForging(aPlayer.inventory, (TileEntityColdForgingPlatform) aWorld.getTileEntity(x, y, z));
	}

	@Override
	public GuiContainer openGui(World aWorld, int x, int y, int z,
			EntityPlayer aPlayer)
	{
		return new GuiColdForging(aPlayer, (TileEntityColdForgingPlatform) aWorld.getTileEntity(x, y, z));
	}
	
	@Override
	public ArrayList<ItemStack> getHarvestDrop(BlockSubTile block,
			World aWorld, int aMeta, TileEntity aTile, int aFotune) 
	{
		ArrayList<ItemStack> drops = new ArrayList();
		drops.add(ItemFleSub.a("stone_plate"));
		return drops;
	}
}