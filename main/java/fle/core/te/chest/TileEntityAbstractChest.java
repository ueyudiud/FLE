package fle.core.te.chest;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import fle.api.inventory.InventoryTileBase;
import fle.api.te.TEInventory;
import fle.core.inventory.InventoryAbstractChest;

public class TileEntityAbstractChest extends TEInventory<InventoryTileBase>
{
	public TileEntityAbstractChest(int size)
	{
		super(new InventoryAbstractChest(size));
	}

	@Override
	public void updateEntity()
	{
		inv.updateEntity(this);
	}
}