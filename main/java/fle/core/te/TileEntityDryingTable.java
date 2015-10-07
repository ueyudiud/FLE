package fle.core.te;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.api.gui.GuiError;
import fle.api.te.TEInventory;
import fle.core.inventory.InventoryDryingTable;
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
	
	int tick = 0;
	
	@Override
	public void updateInventory() 
	{	
		inv.updateEntity(this);
		++levelCheckBuffer;
		if(levelCheckBuffer >= 200)
		{
			levelCheckBuffer = 0;
			tem = WorldUtil.getTempretureLevel(worldObj, xCoord, yCoord, zCoord);
			water = WorldUtil.getWaterLevel(worldObj, xCoord, yCoord, zCoord);
		}
		++tick;
		if(tick > 100)
		{
			markNBTUpdate();
			tick = 0;
		}
	}

	@SideOnly(Side.CLIENT)
	public int getRecipeProgressBar(int i) 
	{
		return inv.getProgressBar(i);
	}
}