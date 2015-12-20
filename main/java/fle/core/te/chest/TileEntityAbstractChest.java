package fle.core.te.chest;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import flapi.te.TEInventory;

public abstract class TileEntityAbstractChest extends TEInventory
{
	final int[] retSize;
	
	public TileEntityAbstractChest(int size)
	{
		super(size);
		retSize = new int[size];
		for(int h = 0; h < retSize.length; ++h)
		{
			retSize[h] = h;
		}
	}

	@Override
	public void update()
	{
		
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