package fle.core.inventory.tank;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import fle.api.inventory.InventoryTileBase;
import fle.core.te.tank.TileEntityMultiTank;

public class InventoryMultiTank extends InventoryTileBase<TileEntityMultiTank>
{
	private String name;
	
	public InventoryMultiTank(String aName, int i)
	{
		super(i);
		name = aName;
	}

	@Override
	public void updateEntity(TileEntityMultiTank tile)
	{
		
	}

	@Override
	public String getInventoryName()
	{
		return name;
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return false;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(ForgeDirection dir)
	{
		return null;
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
}