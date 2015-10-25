package fle.core.te;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.common.util.ForgeDirection;
import fle.api.gui.GuiCondition;
import fle.api.soild.ISolidHandler;
import fle.api.soild.Solid;
import fle.api.soild.SolidStack;
import fle.api.soild.SolidTankInfo;
import fle.api.te.TEIST;
import fle.core.inventory.InventorySifter;

public class TileEntitySifter extends TEIST<InventorySifter>
{
	public GuiCondition type;
	
	public TileEntitySifter()
	{
		super(new InventorySifter());
	}

	@Override
	protected void updateInventory()
	{
		getTileInventory().updateEntity(this);
	}
	
	@SideOnly(Side.CLIENT)
	public int getRecipeProgress(int length)
	{
		return (int) ((double) getTileInventory().recipeTick / 200 * length);
	}
}