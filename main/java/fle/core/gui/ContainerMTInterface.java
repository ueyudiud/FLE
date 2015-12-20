package fle.core.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraftforge.fluids.IFluidTank;
import flapi.gui.ContainerCraftable;
import flapi.gui.FluidSlot;
import flapi.gui.SlotOutput;
import fle.core.te.tank.TileEntityMultiTankInterface;

public class ContainerMTInterface extends ContainerCraftable
{
	public ContainerMTInterface(InventoryPlayer player, final TileEntityMultiTankInterface tile)
	{
		super(player, tile, 0, 0);
		addSlotToContainer(new Slot(tile, 0, 93, 18));
		addSlotToContainer(new SlotOutput(tile, 1, 93, 52));
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
		locateRecipeInput = new TransLocation("input", 36);
		locateRecipeOutput = new TransLocation("output", 37);
	}
}