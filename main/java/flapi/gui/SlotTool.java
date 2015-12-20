package flapi.gui;

import net.minecraft.inventory.IInventory;

public class SlotTool extends SlotHolographic
{
	public SlotTool(IInventory inventory, int slotID, int x, int y) 
	{
		super(inventory, slotID, x, y, false, false);
	}	
}