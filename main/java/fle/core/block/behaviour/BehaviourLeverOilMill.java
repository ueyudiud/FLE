package fle.core.block.behaviour;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;
import fle.core.gui.ContainerLeverOilMill;
import fle.core.gui.GuiLeverOilMill;
import fle.core.te.TileEntityOilMill;

public class BehaviourLeverOilMill extends BehaviourTile
{
	public BehaviourLeverOilMill()
	{
		super("leverOilMill", TileEntityOilMill.class);
	}

	@Override
	public Container openContainer(World aWorld, int x, int y, int z,
			EntityPlayer aPlayer)
	{
		return new ContainerLeverOilMill(aPlayer.inventory, (TileEntityOilMill) aWorld.getTileEntity(x, y, z));
	}

	@Override
	public GuiContainer openGui(World aWorld, int x, int y, int z,
			EntityPlayer aPlayer)
	{
		return new GuiLeverOilMill(aPlayer, (TileEntityOilMill) aWorld.getTileEntity(x, y, z));
	}
}