package fle.core.block.behaviour;

import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import fle.core.block.BlockSubTile;
import fle.core.gui.ContainerBoilingHeater;
import fle.core.gui.GuiBoilingHeater;
import fle.core.te.argil.TileEntityBoilingHeater;

public class BehaviourBoilingHeater extends BehaviourTile
{
	public BehaviourBoilingHeater()
	{
		super("boilingHeater", TileEntityBoilingHeater.class);
	}

	@Override
	public Container openContainer(World aWorld, int x, int y, int z,
			EntityPlayer aPlayer)
	{
		return new ContainerBoilingHeater(aPlayer.inventory, (TileEntityBoilingHeater) aWorld.getTileEntity(x, y, z));
	}

	@Override
	public GuiContainer openGui(World aWorld, int x, int y, int z,
			EntityPlayer aPlayer)
	{
		return new GuiBoilingHeater(aPlayer, (TileEntityBoilingHeater) aWorld.getTileEntity(x, y, z));
	}
	
	@Override
	public void onBlockBreak(BlockSubTile block, World aWorld, int x, int y,
			int z, Block aBlock, int aMeta)
	{
		TileEntity tile = aWorld.getTileEntity(x, y, z);
		if(tile instanceof IInventory)
		{
			for(int i = 0; i < ((IInventory) tile).getSizeInventory(); ++i)
			{
				block.dropBlockAsItem(aWorld, x, y, z, ((IInventory) tile).getStackInSlot(i));
			}
		}
	}
}