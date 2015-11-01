package fle.core.te.tank;

import fle.core.inventory.tank.InventoryMultiTank;
import fle.core.recipe.RecipeHelper;
import net.minecraft.tileentity.TileEntity;

public class TileEntityMultiTankInterface extends TileEntityMultiTank
{
	public TileEntityMultiTankInterface()
	{
		super("MultiTankInterface", 2);
	}
	
	@Override
	protected boolean canBeMainTile()
	{
		return false;
	}
	
	@Override
	protected boolean canConnectWith(TileEntity tile)
	{
		return tile instanceof TileEntityMultiTank;
	}
	
	@Override
	protected void updateInventory()
	{
		super.updateInventory();
		if(mainTile != null)
		{
			if(RecipeHelper.fillOrDrainInventoryTank(getTileInventory(), getMainTank(), 0, 1))
			{
				getTileInventory().syncSlot(this);
			}
		}
	}
	
	@Override
	public InventoryMultiTank getTileInventory()
	{
		return getThisInventory();
	}
}