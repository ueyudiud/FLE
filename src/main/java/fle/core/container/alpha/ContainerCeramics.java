package fle.core.container.alpha;

import farcore.interfaces.gui.IGuiUpdatable;
import farcore.inventory.Inventory;
import farcore.lib.container.ContainerBase;
import farcore.lib.container.SlotOutput;
import farcore.util.U;
import fle.api.recipe.machine.CeramicsRecipe;
import fle.load.Langs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerCeramics extends ContainerBase<Inventory> implements IGuiUpdatable
{
	private boolean isCrafted;
	public float[] fs;
	
	public ContainerCeramics(EntityPlayer player)
	{
		super(new Inventory(1, Langs.inventoryCeramics, 64), player);
		this.addSlotToContainer(new SlotOutput(inventory, 0, 130, 35));
		fs = new float[10];
		for(int i = 0; i < fs.length; ++i) fs[i] = 1.0F;
	}
	
	@Override
	public ItemStack slotClick(int id, int mouseClick, int shiftHold, EntityPlayer player)
	{
		if(id >= 0 && id < inventorySlots.size())
		{
			Slot slot = (Slot) inventorySlots.get(id);
			if(slot.isSlotInInventory(inventory, 0))
			{
				ItemStack stack = inventory.getStackInSlot(0);
				if(stack != null)
				{
					isCrafted = true;
					ItemStack currentStack = player.getCurrentEquippedItem();
					currentStack.stackSize -= CeramicsRecipe.getRecipe(currentStack, fs).getInput().size(stack);
					if(currentStack.stackSize <= 0)
					{
						player.destroyCurrentEquippedItem();
					}
					U.Inventorys.givePlayer(player, stack.copy());
					player.closeScreen();
					return null;
				}
				return null;
			}
		}
		return super.slotClick(id, mouseClick, shiftHold, player);
	}
	
	private void onCrafting(int id) 
	{
		if(id < 5)
		{
			isCrafted = true;
			fs[id] = Math.max(fs[id] - 0.046875F, 0.0F);
			fs[id + 5] = Math.min(fs[id + 5] + 0.015625F, 1.0F);
		}
		else
		{
			isCrafted = true;
			fs[id] = Math.max(fs[id] - 0.046875F, 0.0F);
			fs[id - 5] = Math.min(fs[id - 5] + 0.015625F, 1.0F);
		}
		inventory.setInventorySlotContents(0, CeramicsRecipe.getRecipeResult(player.getCurrentEquippedItem(), fs));
	}
		
	@Override
	public void onActive(int type, int contain)
	{
		if(type == (byte) 1)
		{
			onCrafting((Integer) contain);
		}
	}
}