package fla.core.gui;

import fla.api.item.IPolishTool;
import fla.api.recipe.ItemState;
import fla.core.gui.base.InventoryTileCraftable;
import fla.core.recipe.machine.PolishRecipe;
import fla.core.tileentity.TileEntityPolishTable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class InventoryPolishTable extends InventoryTileCraftable<TileEntityPolishTable>
{	
	ItemStack lastSlot;
	public char[] inputMap = "         ".toCharArray();
	public int selectState = 0;
	
	public InventoryPolishTable()
	{
		super("inventory.polishTable", 3);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) 
	{
		super.writeToNBT(nbt);
		nbt.setString("RecipeMap", new String(inputMap));
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
			inputMap[i] = new Character(c[i]);
		}
	}
	
	public void clearMap(TileEntityPolishTable tile)
	{
		stacks[0] = null;
		stacks[2] = null;
		inputMap = "         ".toCharArray();
		selectState = 0;
		syncSlot(tile);
	}
	
	public void craftedOnce(TileEntityPolishTable tile, EntityPlayer player)
	{
		if(stacks[1] != null)
		{
			ItemStack i = stacks[1].copy();
			inputMap[selectState] = ItemState.StateManager.getCraftedChar(inputMap[selectState], stacks[1]);
			if(i.getItem() instanceof IPolishTool)
			{
				stacks[1] = ((IPolishTool) i.getItem()).getOutput(player, i);
			}
			else if(ItemState.StateManager.isItemEffective(i))
			{
				decrStackSize(1, 1);
			}
			ItemStack itemstack = PolishRecipe.getRecipeResult(stacks[0], inputMap);
			if(!ItemStack.areItemStacksEqual(itemstack, stacks[2]))
			{
				if(itemstack != null)
					stacks[2] = itemstack.copy();
				else stacks[2] = null;
			}
			syncSlot(tile);
		}
	}
	
	public void changeSelect(TileEntityPolishTable tile)
	{
		++selectState;
		if(this.selectState == 9)
		{
			this.selectState = 0;
		}
	}
	
	@Override
	public ItemStack decrStackSize(int i, int size) 
	{
		if(stacks[i] == null) return null;
		if(i == 2)
		{
			stacks[0] = null;
			inputMap = "         ".toCharArray();
			selectState = 0;
		}
		ItemStack ret = stacks[i].copy();
		int a = ret.stackSize;
		stacks[i].stackSize -= size;
		if(stacks[i].stackSize <= 0) stacks[i] = null;
		ret.stackSize = Math.min(size, ret.stackSize);
		return ret;
	}

	@Override
	public void updateEntity(TileEntityPolishTable tile)
	{
		
	}
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer player) 
	{
		return super.isUseableByPlayer(player) && stacks[0] != null;
	}

	@Override
	protected boolean isItemValidForAbstractSlot(int i, ItemStack itemstack)
	{
		return i == 1;
	}

	@Override
	protected boolean isInputSlot(int i) 
	{
		return i == 0;
	}

	@Override
	protected boolean isOutputSlot(int i) 
	{
		return i == 2;
	}
}