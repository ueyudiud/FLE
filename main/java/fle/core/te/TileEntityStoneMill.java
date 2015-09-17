package fle.core.te;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.api.gui.GuiCondition;
import fle.api.te.TEIT;
import fle.core.inventory.InventoryStoneMill;

public class TileEntityStoneMill extends TEIT<InventoryStoneMill>
{
	public GuiCondition type;

	public TileEntityStoneMill()
	{
		super(new InventoryStoneMill());
	}

	@Override
	public void updateEntity() 
	{
		inv.updateEntity(this);
	}
	
	public void onPower()
	{
		inv.onPower();
	}
	
	@SideOnly(Side.CLIENT)
	public double getRotation()
	{
		return (double) inv.buf * Math.PI / 20.0D;
	}

	@SideOnly(Side.CLIENT)
	public double getProgress(int i)
	{
		return (int) (inv.getProgress() * i);
	}
}