package fle.core.block.behaviour;

import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import fle.core.block.BlockSubTile;
import fle.core.gui.ContainerShifter;
import fle.core.gui.GuiSifter;
import fle.core.te.TileEntitySifter;

public class BehaviourSifter extends BehaviourTile
{
	public BehaviourSifter()
	{
		super("Sifter", TileEntitySifter.class);
	}
	
	@Override
	public boolean canBlockStay(BlockSubTile block, World aWorld, int x, int y,
			int z)
	{
		return aWorld.getBlock(x, y - 1, z).isSideSolid(aWorld, x, y - 1, z, ForgeDirection.DOWN);
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

	@Override
	public Container openContainer(World aWorld, int x, int y, int z,
			EntityPlayer aPlayer)
	{
		return new ContainerShifter(aPlayer.inventory, (TileEntitySifter) aWorld.getTileEntity(x, y, z));
	}

	@Override
	public GuiContainer openGui(World aWorld, int x, int y, int z,
			EntityPlayer aPlayer)
	{
		return new GuiSifter(aPlayer, (TileEntitySifter) aWorld.getTileEntity(x, y, z));
	}
}