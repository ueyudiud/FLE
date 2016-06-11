package fle.core.container.alpha;

import farcore.interfaces.gui.IGuiUpdatable;
import farcore.lib.container.ContainerBase;
import farcore.lib.container.FluidSlot;
import farcore.lib.container.SlotBase;
import fle.core.tile.TileEntityTerrine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.IFluidContainerItem;

public class ContainerTerrine extends ContainerBase<TileEntityTerrine>
implements IGuiUpdatable
{
	public ContainerTerrine(TileEntityTerrine inventory, EntityPlayer player)
	{
		super(inventory, player);
		addPlayerSlot();
		int id = inventorySlots.size();
		addSlotToContainer(new SlotBase(inventory, 0, 89, 28)
		{
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				return ContainerTerrine.this.inventory.getMode() == 1 ? 
						FluidContainerRegistry.isContainer(stack) ||
						stack.getItem() instanceof IFluidContainerItem :
							true;					
			}
		});
		addSlotToContainer(new SlotBase(inventory, 1, 89, 46)
		{
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				return ContainerTerrine.this.inventory.getMode() != 1;					
			}
		});
		addSlotToContainer(new FluidSlot(inventory, 0, 75, 32, 8, 30));
		TransLocate 
		locate1 = new TransLocate("inventory1", id)
		{
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				return inventory.getMode() == 1 ? 
						FluidContainerRegistry.isContainer(stack) ||
						stack.getItem() instanceof IFluidContainerItem :
							true;
					
			}
		}, 
		locate2 = new TransLocate("inventory2", id + 1)
		{
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				return inventory.getMode() != 1;
			}
		};
		locateHand.append(locate1).append(locate2).append(locateBag);
		locateBag.append(locate1).append(locate2).append(locateHand);
		locate1.append(locatePlayer);
		locate2.append(locatePlayer);
	}

	@Override
	public void onActive(int type, int contain)
	{
		if(contain == 0)
		{
			inventory.switchMode();
		}
	}	
}