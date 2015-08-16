package fle.core.te;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.api.gui.GuiError;
import fle.core.gui.InventoryDryingTable;
import fle.core.te.base.TEInventory;
import fle.core.util.WorldUtil;

public class TileEntityDryingTable extends TEInventory<InventoryDryingTable>
{
	private int levelCheckBuffer;
	public double tem;
	public double water;
	public GuiError type;
	
	public TileEntityDryingTable() 
	{
		super(new InventoryDryingTable());
	}
	
	public double getTempretureLevel()
	{
		return tem == 0D ? WorldUtil.getTempretureLevel(worldObj, xCoord, yCoord, zCoord) : tem;
	}
	
	public double getWaterLevel() 
	{
		return water == 0D ? WorldUtil.getWaterLevel(worldObj, xCoord, yCoord, zCoord) : water;
	}
	
	@Override
	public void updateEntity() 
	{	
		inv.updateEntity(this);
		++levelCheckBuffer;
		if(levelCheckBuffer >= 200)
		{
			levelCheckBuffer = 0;
			tem = WorldUtil.getTempretureLevel(worldObj, xCoord, yCoord, zCoord);
			water = WorldUtil.getWaterLevel(worldObj, xCoord, yCoord, zCoord);
		}
	}

	@SideOnly(Side.CLIENT)
	public int getRecipeProgressBar(int i) 
	{
		return inv.getProgressBar(i);
	}
}