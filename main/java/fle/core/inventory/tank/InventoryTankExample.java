package fle.core.inventory.tank;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import fle.api.inventory.InventorySolidTank;
import fle.api.inventory.InventoryTileBase;
import fle.core.te.tank.TileEntityTankExample;

public class InventoryTankExample extends InventoryTileBase<TileEntityTankExample>
{
	public InventoryTankExample(int i)
	{
		super(i);
	}

	@Override
	public void updateEntity(TileEntityTankExample tile)
	{
		
	}

	@Override
	public String getInventoryName()
	{
		return null;
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