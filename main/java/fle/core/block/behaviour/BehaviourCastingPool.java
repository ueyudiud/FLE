package fle.core.block.behaviour;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;
import fle.core.gui.ContainerCastingPool;
import fle.core.gui.GuiCastingPool;
import fle.core.te.TileEntityCastingPool;

public class BehaviourCastingPool extends BehaviourTile
{
	public BehaviourCastingPool()
	{
		super(TileEntityCastingPool.class);
	}

	@Override
	public Container openContainer(World aWorld, int x, int y, int z,
			EntityPlayer aPlayer)
	{
		return new ContainerCastingPool(aPlayer.inventory, (TileEntityCastingPool) aWorld.getTileEntity(x, y, z));
	}

	@Override
	public GuiContainer openGui(World aWorld, int x, int y, int z,
			EntityPlayer aPlayer)
	{
		return new GuiCastingPool(aPlayer, (TileEntityCastingPool) aWorld.getTileEntity(x, y, z));
	}
}