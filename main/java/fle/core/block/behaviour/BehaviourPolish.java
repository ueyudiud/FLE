package fle.core.block.behaviour;

import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;
import fle.core.block.BlockSubTile;
import fle.core.gui.ContainerPolish;
import fle.core.gui.GuiPolish;
import fle.core.te.TileEntityPolish;

public class BehaviourPolish extends BehaviourTile
{
	public BehaviourPolish()
	{
		super("Polish", TileEntityPolish.class);
	}
	
	@Override
	public void onBlockBreak(BlockSubTile block, World aWorld, int x, int y,
			int z, Block aBlock, int aMeta) 
	{
		TileEntityPolish tile = (TileEntityPolish) aWorld.getTileEntity(x, y, z);
		if(tile != null)
		{
			for(int i = 0; i < tile.getSizeInventory(); ++i)
			{
				block.dropBlockAsItem(aWorld, x, y, z, tile.getStackInSlot(i));
			}
		}
		super.onBlockBreak(block, aWorld, x, y, z, aBlock, aMeta);
	}

	@Override
	public Container openContainer(World aWorld, int x, int y, int z,
			EntityPlayer aPlayer) 
	{
		return new ContainerPolish(aPlayer.inventory, (TileEntityPolish) aWorld.getTileEntity(x, y, z));
	}

	@Override
	public GuiContainer openGui(World aWorld, int x, int y, int z,
			EntityPlayer aPlayer)
	{
		return new GuiPolish(aPlayer, (TileEntityPolish) aWorld.getTileEntity(x, y, z));
	}
}