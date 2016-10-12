package fle.core.gui;

import farcore.lib.gui.ContainerBlockPosition;
import farcore.lib.gui.SlotBase;
import farcore.lib.gui.SlotButton;
import farcore.lib.gui.SlotOutput;
import farcore.lib.world.ICoord;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ITickable;

public class ContainerOilLampCrafting extends ContainerBlockPosition implements ITickable
{
	private InventoryCrafting crafting;
	private InventoryBasic tools;
	private InventoryCraftResult result;
	private SlotButton output;
	
	public ContainerOilLampCrafting(EntityPlayer player, ICoord coord)
	{
		super(player, coord);
		crafting = new InventoryCrafting(this, 2, 2);
		tools = new InventoryBasic("inventory.oillamp", false, 2);
		result = new InventoryCraftResult();
		int off = inventorySlots.size();
		addStandardSlotMatrix(crafting, 69, 26, 2, 2, 0, 18, 18);
		addSlotToContainer(new SlotBase(tools, 0, 16, 35));
		addSlotToContainer(new SlotBase(tools, 1, 36, 35));
		addSlotToContainer(output = new SlotButton(0, 113, 86));
		addSlotToContainer(new SlotOutput(result, 0, 140, 44));
		TL tl1, tl2;
		transferLocates.add(tl1 = new TL(off, off + 6));
		transferLocates.add(tl2 = new TL(off + 7));
		tl1.appendTransferLocate(locationPlayer);
		tl2.appendTransferLocate(locationPlayer);
		locationBag.appendTransferLocate(locationHand);
		locationHand.appendTransferLocate(locationBag);
	}

	@Override
	protected int getFieldCount()
	{
		return 2;
	}
	
	@Override
	public void update()
	{
		if(!world.isRemote && currentValue[1] != 0 && currentValue[0] > 0)
		{
			++currentValue[0];
			if(currentValue[0] >= currentValue[1])
			{
				outputRecipe();
			}
		}
	}

	private void outputRecipe()
	{

	}
	
	@Override
	protected ItemStack onToolClick(ItemStack tool, IInventory inventoryBelong, int index)
	{
		if(inventoryBelong == null && index == 0)
		{
			if(output != null)
			{
				if(currentValue[0] == 0)
				{
					currentValue[0] = 1;
				}
			}
			return tool;
		}
		return super.onToolClick(tool, inventoryBelong, index);
	}
	
	@Override
	public void onInventoryChanged(InventoryBasic inventory)
	{
		onCraftMatrixChanged(inventory);
	}

	@Override
	public void onCraftMatrixChanged(IInventory inventoryIn)
	{
		super.onCraftMatrixChanged(inventoryIn);
	}
}