package fle.core.container.alpha;

import farcore.lib.container.ContainerBase;
import farcore.lib.container.FluidSlot;
import farcore.lib.container.SlotBase;
import fle.core.tile.TileEntityTerrine;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerTerrine extends ContainerBase<TileEntityTerrine>
{
	public ContainerTerrine(TileEntityTerrine inventory, EntityPlayer player)
	{
		super(inventory, player);
		addSlotToContainer(new SlotBase(inventory, 0, 89, 28));
		addSlotToContainer(new SlotBase(inventory, 1, 89, 46));
		addSlotToContainer(new FluidSlot(inventory, 0, 75, 32, 8, 30));
	}
}