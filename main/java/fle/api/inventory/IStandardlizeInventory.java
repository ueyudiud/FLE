package fle.api.inventory;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

public interface IStandardlizeInventory
{
	int[] getInputSlots();
	
	int[] getOutputSlots();
	
	int[] getFreeToInputSlots();
	
	int[] getFreeToOutputSlots();
	
	ItemStack getItemStack(int slotID);
	
	int[] getAccessSlotFromSide(ForgeDirection dir);
}