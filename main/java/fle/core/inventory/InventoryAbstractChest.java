package fle.core.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import fle.api.inventory.InventoryTileBase;
import fle.core.te.chest.TileEntityAbstractChest;

public class InventoryAbstractChest<T extends TileEntityAbstractChest> extends InventoryTileBase<T>
{
	final int[] retSize;
	
	public InventoryAbstractChest(int i)
	{
		super(i);
		retSize = new int[i];
		for(int h = 0; h < retSize.length; ++h)
		{
			retSize[h] = h;
		}
	}
	
	@Override
	public void updateEntity(T tile)
	{
		for(int i = 0; i < stacks.length; ++i)
		{
			
		}
	}

	@Override
	public String getInventoryName()
	{
		return "inventory.chest";
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return false;
	}
	
	@Override
	public int[] getAccessibleSlotsFromSide(int aSide)
	{
		int[] ret = new int[getSizeInventory()];
		for(int i = 0; i < getSizeInventory(); ++i)
			ret[i] = i;
		return ret;
	}

	@Override
	public boolean canInsertItem(int aSlotID, ItemStack aResource,
			ForgeDirection aDirection)
	{
		return aDirection == ForgeDirection.UP;
	}

	@Override
	public boolean canExtractItem(int aSlotID, ItemStack aResource,
			ForgeDirection aDirection) 
	{
		return aDirection != ForgeDirection.UP;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(ForgeDirection dir)
	{
		return null;
	}
}
