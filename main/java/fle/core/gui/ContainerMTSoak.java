package fle.core.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraftforge.fluids.IFluidTank;
import flapi.gui.ContainerCraftable;
import flapi.gui.FluidSlot;
import fle.core.te.tank.TileEntityMultiTankSoak;

public class ContainerMTSoak extends ContainerCraftable
{
	public ContainerMTSoak(InventoryPlayer player, final TileEntityMultiTankSoak tile)
	{
		super(player, tile, 0, 0);
		addSlotToContainer(new Slot(tile, 0, 101, 23));
		addSlotToContainer(new Slot(tile, 1, 119, 23));
		addSlotToContainer(new Slot(tile, 2, 101, 44));
		addSlotToContainer(new Slot(tile, 3, 119, 44));
		addSlotToContainer(new FluidSlot(tile, 0, 41, 18, 50, 50)
		{
		@Override
		public boolean getHasStack()
		{
			return tile.isMultiTank();
		}

		@Override
		public IFluidTank getTank()
		{
			return tile.getMainTank();
		}
		});
		locateRecipeInput = new TransLocation("input", 36, 40);
		locateRecipeOutput = new TransLocation("output", 36, 40);
	}

}