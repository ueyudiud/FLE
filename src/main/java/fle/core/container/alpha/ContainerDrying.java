package fle.core.container.alpha;

import farcore.lib.container.ContainerBase;
import farcore.lib.container.SlotBase;
import farcore.lib.container.SlotOutput;
import farcore.lib.container.ContainerBase.TransLocate;
import fle.core.tile.TileEntityDryingTable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ContainerDrying extends ContainerBase<TileEntityDryingTable>
{
	public ContainerDrying(TileEntityDryingTable inventory, EntityPlayer player)
	{
		super(inventory, player);
		addPlayerSlot();
		int k = this.inventorySlots.size();
		addSlotToContainer(new SlotBase(inventory, 0, 53, 17));
		addSlotToContainer(new SlotBase(inventory, 1, 53, 35));
		addSlotToContainer(new SlotBase(inventory, 2, 53, 53));
		addSlotToContainer(new SlotOutput(inventory, 3, 107, 17));
		addSlotToContainer(new SlotOutput(inventory, 4, 107, 35));
		addSlotToContainer(new SlotOutput(inventory, 5, 107, 53));
		TransLocate locate1, locate3;
		addTransLocate(locate1 = new TransLocate("input", k + 0, k + 3, false, false));
		addTransLocate(locate3 = new TransLocate("output", k + 3, k + 6, false, false));
		locate3.append(locatePlayer);
		locate1.append(locateBag).append(locateHand);
		locateBag.append(locate1).append(locateHand);
		locateHand.append(locate1).append(locateBag);
	}
	
	@Override
	protected int getUpdateSize()
	{
		return 3;
	}
	
	@Override
	protected int getUpdate(int id)
	{
		return inventory.ticks[id];
	}
	
	@Override
	protected void setUpdate(int id, int value)
	{
		inventory.ticks[id] = value;
	}
}