package fle.core.block.behaviour;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;
import fle.core.gui.ContainerTerrine;
import fle.core.gui.GuiTerrine;
import fle.core.te.argil.TileEntityTerrine;

public class BehaviourTerrine extends BehaviourTile
{
	public BehaviourTerrine()
	{
		super(TileEntityTerrine.class);
	}

	@Override
	public Container openContainer(World aWorld, int x, int y, int z,
			EntityPlayer aPlayer)
	{
		return new ContainerTerrine(aPlayer.inventory, (TileEntityTerrine) aWorld.getTileEntity(x, y, z));
	}

	@Override
	public GuiContainer openGui(World aWorld, int x, int y, int z,
			EntityPlayer aPlayer)
	{
		return new GuiTerrine(aPlayer, (TileEntityTerrine) aWorld.getTileEntity(x, y, z));
	}
}