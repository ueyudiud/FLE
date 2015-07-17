package fla.core.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import fla.api.network.IListenerContainer;
import fla.api.recipe.ErrorType;
import fla.core.Fla;
import fla.core.gui.base.ContainerCraftable;
import fla.core.gui.base.RecipeHelper;
import fla.core.gui.base.SlotOutput;
import fla.core.tool.WasherManager;

public class ContainerWashing extends ContainerCraftable implements IListenerContainer
{
	ErrorType type = null;
	String recipeName;
	int washTime;
	
	public ContainerWashing(InventoryPlayer player)
	{
		super(player, null, 0, 0);
		this.inv = new InventoryWashing(this);
		this.addSlotToContainer(new Slot(inv, 0, 59, 19));
		
		for(int i = 0; i < 3; ++i)
			for(int j = 0; j < 3; ++j)
				this.addSlotToContainer(new SlotOutput(inv, 1 + i + j * 3, 98 + i * 18, 19 + j * 18));
		this.locateRecipeInput = new TransLocation("inventory_input", 36);
		this.locateRecipeOutput = new TransLocation("inventory_output", 37, 46);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return true;
	}
	
	int getWashPrograss(int a)
	{
		return a * washTime / 20;
	}
	
	void washItem()
	{
		if(recipeName == null)
		{
			recipeName = WasherManager.getRecipeName(inv.getStackInSlot(0));
			if(recipeName != null)
			{
				RecipeHelper.onInputItemStack(inv, 0);
				onCraftMatrixChanged(inv);
			}
		}
		if(recipeName != null)
		{
			++washTime;
			if(washTime > 20)
			{
				ItemStack[] output = WasherManager.outputRecipe(recipeName);
				if(RecipeHelper.matchOutput(inv, 1, 10, output))
				{
					RecipeHelper.onOutputShapelessStacks(inv, 1, 10, output);
					washTime = 0;
					recipeName = null;
					onCraftMatrixChanged(inv);
					if(player.player instanceof EntityPlayerMP)
					{
						Fla.fla.nwm.get().initiateContainerError(ErrorType.DEFAULT, (EntityPlayerMP) player.player);
					}
				}
				else
				{
					if(player.player instanceof EntityPlayerMP)
					{
						Fla.fla.nwm.get().initiateContainerError(ErrorType.CAN_NOT_OUTPUT, (EntityPlayerMP) player.player);
					}
				}
			}
		}
	}

	@Override
	public void onPacketData(int x, int y, int z, byte type, short contain) 
	{
		if(type == (byte) 1)
		{
			washItem();
		}
		else if(type == (byte) 2)
		{
			this.type = ErrorType.values()[contain];
		}
	}
	
	@Override
	public void onContainerClosed(EntityPlayer player) 
	{
		super.onContainerClosed(player);
		dropInventoryItem(inv, player);
	}
}
