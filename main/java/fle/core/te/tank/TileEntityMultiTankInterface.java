package fle.core.te.tank;

import net.minecraft.tileentity.TileEntity;
import flapi.te.TEAbstractTank;
import fle.core.recipe.RecipeHelper;

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
		if(mainTile != null)
		{
			if(RecipeHelper.fillOrDrainInventoryTank(this, getMainTank(), 0, 1))
			{
				;
			}
		}
	}
	
	@Override
	public void onNeibourChange(boolean flag)
	{
		if(flag && isMultiTank())
		{
			mainTile.onNeibourChange(false);
		}
	}
	
	@Override
	public boolean canBeConnect(TEAbstractTank main, int xPos,
			int yPos, int zPos, int width, int height)
	{
		return height - yPos > 5 || yPos == 0 ? false : 
			xPos == 0 ? zPos != 0 && zPos != width - 1 : 
				xPos == width - 1 ? zPos != 0 && zPos != width - 1 : true;
	}
}