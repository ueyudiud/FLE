package fle.core.inventory;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import flapi.gui.IToolClickHandler;
import flapi.gui.InventoryCraftable;
import flapi.recipe.IPlayerToolCraftingRecipe;
import fle.core.gui.ContainerPlayerCrafting;
import fle.core.recipe.RecipeHelper;
import fle.core.recipe.crafting.PlayerToolCraftingRecipe;

public class InventoryPlayerCrafting extends InventoryCraftable<ContainerPlayerCrafting> implements IToolClickHandler
{
	public InventoryPlayerCrafting(ContainerPlayerCrafting container)
	{
		super("inventory.player.crafting", container, 4);
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack stack)
	{
		super.setInventorySlotContents(i, stack);
		if(i != 3)
		{
			container.onCraftMatrixChanged(this);
		}
	}
	
	@Override
	public ItemStack decrStackSize(int i, int size)
	{
		container.onCraftMatrixChanged(this);
		return super.decrStackSize(i, size);
	}
	
	@Override
	protected boolean isItemValidForAbstractSlot(int i,
			ItemStack itemstack)
	{
		return true;
	}

	@Override
	protected boolean isInputSlot(int i)
	{
		return i < 3;
	}

	@Override
	protected boolean isOutputSlot(int i)
	{
		return i == 3;
	}

	@Override
	public ItemStack onToolClick(ItemStack stack, EntityLivingBase player,
			int activeID)
	{
		IPlayerToolCraftingRecipe recipe = PlayerToolCraftingRecipe.getResult(stacks[0], stacks[2], stack);
		if(recipe != null)
		{
			ItemStack output = recipe.getOutput(stacks[0], stacks[2], stack);
			decrStackSize(0, 1);
			decrStackSize(2, 1);
			ItemStack ret = recipe.useTool((EntityPlayer) player, stack);
			if(!RecipeHelper.matchOutput(this, 3, output))
			{
				if(!player.worldObj.isRemote)
					((EntityPlayer) player).dropPlayerItemWithRandomChoice(stacks[3], false);
				stacks[3] = null;
			}
			RecipeHelper.onOutputItemStack(this, 3, output);
			return ret;
		}
		return stack;
	}
}