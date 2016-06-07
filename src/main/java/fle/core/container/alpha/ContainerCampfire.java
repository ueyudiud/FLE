package fle.core.container.alpha;

import farcore.lib.container.ContainerBase;
import farcore.lib.container.SlotBase;
import farcore.lib.container.SlotOutput;
import farcore.lib.container.SlotTool;
import fle.core.tile.TileEntityCampfire;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerCampfire extends ContainerBase<TileEntityCampfire>
{	
	public ContainerCampfire(TileEntityCampfire inventory, EntityPlayer player)
	{
		super(inventory, player);
		addPlayerSlot(8, 140);
		int k = this.inventorySlots.size();
		addSlotToContainer(new SlotBase(inventory, 0, 22, 15));
		addSlotToContainer(new SlotBase(inventory, 1, 40, 15));
		addSlotToContainer(new SlotOutput(inventory, 2, 22, 43));
		addSlotToContainer(new SlotOutput(inventory, 3, 40, 43));
		addSlotToContainer(new SlotBase(inventory, 4, 22, 73));
		addSlotToContainer(new SlotBase(inventory, 5, 40, 73));
		addSlotToContainer(new SlotBase(inventory, 6, 95, 15));
		addSlotToContainer(new SlotBase(inventory, 7, 113, 15));
		addSlotToContainer(new SlotTool(inventory, 8, 78, 87));
		addSlotToContainer(new SlotBase(inventory, 9, 95, 87));
		addSlotToContainer(new SlotBase(inventory, 10, 113, 87));
		addSlotToContainer(new SlotBase(inventory, 11, 151, 77));

		TransLocate locate1, locate2, locate3, locate4;
		addTransLocate(locate1 = new TransLocate("input", k + 0, k + 2, false, false));
		addTransLocate(locate2 = new TransLocate("tool", k + 5));
		addTransLocate(locate3 = new TransLocate("output", k + 2, k + 4, false, true));
		addTransLocate(locate4 = new TransLocate("fuel", k + 9, k + 11, false, false));
		locate4.append(locatePlayer);
		locate3.append(locatePlayer);
		locate2.append(locateHand).append(locateBag);
		locate1.append(locateBag).append(locateHand);
		locateBag.append(locate1).append(locate2).append(locate3).append(locateHand);
		locateHand.append(locate2).append(locate3).append(locate1).append(locateBag);
	}
	
	@Override
	protected int getUpdateSize()
	{
		return 4;
	}
	
	@Override
	protected int getUpdate(int id) 
	{
		return (int) (id == 0 ? inventory.burningEnergy :
			id == 1 ? inventory.currentBurningEnergy :
				id == 2 ? inventory.progress1 :
					id == 3 ? inventory.progress2 : 0);
	}
	
	@Override
	protected void setUpdate(int id, int value)
	{
		switch (id)
		{
		case 0 : inventory.burningEnergy = value;			
		break;
		case 1 : inventory.currentBurningEnergy = value;
		break;
		case 2 : inventory.progress1 = value;
		break;
		case 3 : inventory.progress2 = value;
		break;
		}
	}
}