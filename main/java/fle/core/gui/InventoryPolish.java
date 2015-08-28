package fle.core.gui;

import java.util.Arrays;
import java.util.Collection;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import fle.FLE;
import fle.api.enums.EnumCraftingType;
import fle.api.gui.GuiCondition;
import fle.api.gui.GuiError;
import fle.api.gui.IConditionContainer;
import fle.api.gui.InventoryTileBase;
import fle.api.item.IPolishTool;
import fle.api.net.INetEventListener;
import fle.api.net.FlePackets.CoderTileUpdate;
import fle.api.recipe.CraftingState;
import fle.core.recipe.PolishRecipe;
import fle.core.recipe.RecipeHelper;
import fle.core.te.TileEntityPolish;

public class InventoryPolish extends InventoryTileBase<TileEntityPolish> implements IConditionContainer, INetEventListener
{
	public ItemStack input;
	ItemStack lastSlot;
	public char[] inputMap = "         ".toCharArray();
	public GuiCondition condition = GuiError.DEFAULT;
	
	public InventoryPolish()
	{
		super(3);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) 
	{
		super.writeToNBT(nbt);
		nbt.setString("RecipeMap", new String(inputMap));
		if(input != null)
			nbt.setTag("RecipeItemStack", input.writeToNBT(new NBTTagCompound()));
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) 
	{
		super.readFromNBT(nbt);
		inputMap = "         ".toCharArray();
		char[] c = nbt.getString("RecipeMap").toCharArray();
		int i;
		for(i = 0; i < c.length; ++i)
		{
			inputMap[i] = new Character(c[i]).charValue();
		}
		input = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("RecipeItemStack"));
	}
	
	public void clearMap(TileEntityPolish tile)
	{
		input = null;
		stacks[2] = null;
		inputMap = "         ".toCharArray();
	}

	public void craftedOnce(TileEntityPolish tile, int selectState, EntityPlayer player)
	{
		if(stacks[1] != null)
		{
			ItemStack i = stacks[1].copy();
			if(input == null)
			{
				if(PolishRecipe.canPolish(stacks[0]))
				{
					input = stacks[0].copy();
					RecipeHelper.onInputItemStack(this, 0);
					input.stackSize = 1;
					condition = GuiError.DEFAULT;
				}
				else
				{
					condition = GuiError.CAN_NOT_INPUT;
				}
			}
			if(i.getItem() instanceof IPolishTool && input != null)
			{
				inputMap[selectState] = ((IPolishTool) i.getItem()).getState(stacks[1], EnumCraftingType.polish, CraftingState.getState(inputMap[selectState])).getCharIndex();
				stacks[1] = ((IPolishTool) i.getItem()).getOutput(player, i);
				if(stacks[1].stackSize <= 0)
				{
					stacks[1] = null;
				}
				ItemStack itemstack = PolishRecipe.getRecipeResult(input, inputMap);
				if(!ItemStack.areItemStacksEqual(itemstack, stacks[2]))
				{
					if(itemstack != null)
						stacks[2] = itemstack.copy();
					else stacks[2] = null;
				}
				condition = GuiError.DEFAULT;
			}
			else
			{
				condition = GuiError.CAN_NOT_INPUT;
			}
		}
	}
	
	@Override
	public ItemStack decrStackSize(int i, int size) 
	{
		if(stacks[i] == null) return null;
		ItemStack ret = stacks[i].copy();
		int a = ret.stackSize;
		stacks[i].stackSize -= size;
		if(stacks[i].stackSize <= 0) stacks[i] = null;
		ret.stackSize = Math.min(size, ret.stackSize);
		if(i == 2)
		{
			clearMap(null);
		}
		return ret;
	}
	
	@Override
	public void updateEntity(TileEntityPolish tile)
	{
		
	}

	@Override
	public String getInventoryName()
	{
		return "inventory.polishTable";
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return false;
	}

	@Override
	public boolean canInsertItem(int aSlotID, ItemStack aResource,
			ForgeDirection aDirection)
	{
		return false;
	}

	@Override
	public boolean canExtractItem(int aSlotID, ItemStack aResource,
			ForgeDirection aDirection) 
	{
		return false;
	}

	@Override
	public Collection<GuiCondition> get() 
	{
		return Arrays.asList(condition);
	}

	@Override
	public void add(GuiCondition tag) 
	{
		condition = tag;
	}

	@Override
	public boolean contain(GuiCondition tag) 
	{
		return condition.getName() == tag.getName();
	}

	@Override
	public void remove(GuiCondition tag) 
	{
		if(contain(condition))
			condition = GuiError.DEFAULT;
	}

	@Override
	public void onReseave(byte type, Object contain) 
	{
		if(type == 1)
		{
			if(contain != null)
				input = ((ItemStack) contain).copy();
		}
	}
}