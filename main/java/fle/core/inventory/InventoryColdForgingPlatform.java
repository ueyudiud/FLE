package fle.core.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import fle.api.FleAPI;
import fle.api.enums.EnumDamageResource;
import fle.api.inventory.InventoryTileCraftable;
import fle.api.recipe.CraftingState;
import fle.api.recipe.ItemOreStack;
import fle.core.init.Lang;
import fle.core.recipe.ColdForgingRecipe;
import fle.core.recipe.RecipeHelper;
import fle.core.te.TileEntityColdForgingPlatform;

public class InventoryColdForgingPlatform extends InventoryTileCraftable<TileEntityColdForgingPlatform>
{
	public int dir = 0;
	public int hard = 0;
	public char[] array = "         ".toCharArray();
	
	public InventoryColdForgingPlatform()
	{
		super(Lang.inventory_coldForgingPlatform, 6);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setString("Recipe", new String(array));
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		array = nbt.getString("Recipe").toCharArray();
	}

	@Override
	public void updateEntity(TileEntityColdForgingPlatform tile)
	{
		
	}

	@Override
	protected boolean isItemValidForAbstractSlot(int i, ItemStack itemstack)
	{
		return false;
	}

	@Override
	protected boolean isInputSlot(int i)
	{
		return i != 5;
	}

	@Override
	protected boolean isOutputSlot(int i)
	{
		return i == 5;
	}
	
	@Override
	public int[] getAccessibleSlotsFromSide(int aSide)
	{
		return new int[0];
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

	public void onOutput()
	{
		array = "         ".toCharArray();
		for(int i = 0; i < 4; ++i)
			RecipeHelper.onInputItemStack(this, i);
	}
	
	public boolean canTakeStack()
	{
		for(char c : array)
		{
			if(c != ' ') return false;
		}
		return true;
	}

	
	public void onSlotChanged()
	{
		stacks[5] = ColdForgingRecipe.getResult(this, new String(array));
	}

	public void onToolClick(EntityPlayer aPlayer, int activeSlot,
			char state)
	{
		if(new ItemOreStack("craftingToolHardHammer").isStackEqul(stacks[4]))
		{
			FleAPI.damageItem(aPlayer, stacks[4], EnumDamageResource.Crafting, 1.0F);
			array[activeSlot] = ColdForgingRecipe.getForgingMapOutputChar(array[activeSlot], state);
		}
	}

	@Override
	public int[] getAccessibleSlotsFromSide(ForgeDirection dir)
	{
		return null;
	}
}