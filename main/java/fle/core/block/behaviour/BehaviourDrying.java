package fle.core.block.behaviour;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;
import fle.core.gui.ContainerDryingTable;
import fle.core.gui.GuiDryingTable;
import fle.core.te.TileEntityDryingTable;

public class BehaviourDrying extends BehaviourTile
{
	public BehaviourDrying()
	{
		super(TileEntityDryingTable.class);
	}

	@Override
	public Container openContainer(World aWorld, int x, int y, int z,
			EntityPlayer aPlayer)
	{
		return new ContainerDryingTable(aPlayer.inventory, (TileEntityDryingTable) aWorld.getTileEntity(x, y, z));
	}

	@Override
	public GuiContainer openGui(World aWorld, int x, int y, int z,
			EntityPlayer aPlayer)
	{
		return new GuiDryingTable(aPlayer, (TileEntityDryingTable) aWorld.getTileEntity(x, y, z));
	}
}