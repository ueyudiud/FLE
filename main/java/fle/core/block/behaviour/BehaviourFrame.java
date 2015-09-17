package fle.core.block.behaviour;

import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;
import fle.core.block.BlockSubTile;
import fle.core.gui.Container3By3;
import fle.core.gui.Gui3By3;
import fle.core.te.chest.TileEntityChest3By3;

public class BehaviourFrame extends BehaviourTile
{
	public BehaviourFrame()
	{
		super(TileEntityChest3By3.class);
	}
	
	@Override
	public void onBlockBreak(BlockSubTile block, World aWorld, int x, int y,
			int z, Block aBlock, int aMeta)
	{
		if(aWorld.isRemote) return;
		if(aWorld.getTileEntity(x, y, z) instanceof IInventory)
		{
			IInventory inv = (IInventory) aWorld.getTileEntity(x, y, z);
			for(int i = 0; i < inv.getSizeInventory(); ++i)
			{
				block.dropBlockAsItem(aWorld, x, y, z, inv.getStackInSlot(i));
			}
		}
	}

	@Override
	public Container openContainer(World aWorld, int x, int y, int z,
			EntityPlayer aPlayer)
	{
		return new Container3By3(aPlayer.inventory, (IInventory) aWorld.getTileEntity(x, y, z));
	}

	@Override
	public GuiContainer openGui(World aWorld, int x, int y, int z,
			EntityPlayer aPlayer)
	{
		return new Gui3By3(aPlayer, (IInventory) aWorld.getTileEntity(x, y, z));
	}
	
}