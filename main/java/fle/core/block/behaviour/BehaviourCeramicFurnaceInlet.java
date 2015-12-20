package fle.core.block.behaviour;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;
import flapi.world.BlockPos;
import fle.core.gui.ContainerCeramicFurnace;
import fle.core.gui.GuiCeramicFurnace;
import fle.core.te.argil.TileEntityCeramicFurnaceInlet;

public class BehaviourCeramicFurnaceInlet extends BehaviourTile
{
	public BehaviourCeramicFurnaceInlet()
	{
		super("ceramicFurnaceInlet", TileEntityCeramicFurnaceInlet.class);
	}

	@Override
	public Container openContainer(World aWorld, int x, int y, int z,
			EntityPlayer aPlayer)
	{
		return new ContainerCeramicFurnace(new BlockPos(aWorld, x, y, z), aPlayer.inventory);
	}

	@Override
	public GuiContainer openGui(World aWorld, int x, int y, int z,
			EntityPlayer aPlayer)
	{
		return new GuiCeramicFurnace(aWorld, x, y, z, aPlayer);
	}
}