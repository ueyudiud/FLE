package fle.core.gui;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import fle.FLE;
import fle.api.gui.ContainerCraftable;
import fle.api.gui.GuiCondition;
import fle.api.gui.GuiError;
import fle.api.gui.SlotOutput;
import fle.api.net.INetEventListener;
import fle.api.net.FlePackets.CoderGuiUpdate;
import fle.core.recipe.RecipeHelper;
import fle.core.recipe.WashingRecipe;

public class ContainerWashing extends ContainerCraftable implements INetEventListener
{
	GuiCondition type = null;
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
			recipeName = WashingRecipe.getRecipeName(inv.getStackInSlot(0));
			if(recipeName != null)
			{
				RecipeHelper.onInputItemStack(inv, 0);
				onCraftMatrixChanged(inv);
				if(player.player instanceof EntityPlayerMP)
					FLE.fle.getNetworkHandler().sendToPlayer(new CoderGuiUpdate((byte) 2, GuiError.DEFAULT), (EntityPlayerMP) player.player);
				type = GuiError.DEFAULT;
			}
			else
			{
				if(player.player instanceof EntityPlayerMP)
					FLE.fle.getNetworkHandler().sendToPlayer(new CoderGuiUpdate((byte) 2, GuiError.CAN_NOT_INPUT), (EntityPlayerMP) player.player);
				type = GuiError.CAN_NOT_INPUT;
			}
		}
		if(recipeName != null)
		{
			++washTime;
			if(washTime > 20)
			{
				ItemStack[] output = WashingRecipe.outputRecipe(recipeName);
				if(RecipeHelper.matchOutput(inv, 1, 10, output))
				{
					RecipeHelper.onOutputShapelessStacks(inv, 1, 10, output);
					washTime = 0;
					recipeName = null;
					onCraftMatrixChanged(inv);
					if(player.player instanceof EntityPlayerMP)
					{
						FLE.fle.getNetworkHandler().sendToPlayer(new CoderGuiUpdate((byte) 2, GuiError.DEFAULT), (EntityPlayerMP) player.player);
					}
					type = GuiError.DEFAULT;
				}
				else
				{
					if(player.player instanceof EntityPlayerMP)
					{
						FLE.fle.getNetworkHandler().sendToPlayer(new CoderGuiUpdate((byte) 2, GuiError.CAN_NOT_OUTPUT), (EntityPlayerMP) player.player);
					}
					type = GuiError.CAN_NOT_OUTPUT;
				}
			}
		}
	}
	
	@Override
	public void onContainerClosed(EntityPlayer player) 
	{
		super.onContainerClosed(player);
		dropInventoryItem(inv, player);
	}

	@Override
	public void onReseave(byte type, Object contain)
	{
		if(type == (byte) 1)
		{
			washItem();
		}
		else if(type == (byte) 2)
		{
			this.type = (GuiCondition) contain;
		}
	}
}