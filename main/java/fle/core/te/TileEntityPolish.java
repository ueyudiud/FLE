package fle.core.te;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.api.gui.GuiCondition;
import fle.api.te.TEInventory;
import fle.core.inventory.InventoryPolish;

public class TileEntityPolish extends TEInventory<InventoryPolish>
{
	public TileEntityPolish()
	{
		super(new InventoryPolish());
	}
	
	int tick = 0;

	public void clearMap()
	{
		getTileInventory().clearMap(this);
	}
	
	public void craftedOnce(EntityPlayer player, int id)
	{
		getTileInventory().craftedOnce(this, id, player);
	}
	
	@SideOnly(Side.CLIENT)
	public char[] getStates()
	{
		return getTileInventory().inputMap;
	}

	@SideOnly(Side.CLIENT)
	public GuiCondition getCondition()
	{
		return getTileInventory().condition;
	}

	public ItemStack getRecipeInput()
	{
		return getTileInventory().input;
	}

	@Override
	public void updateInventory() 
	{
		getTileInventory().updateEntity(this);
		++tick;
		if(tick > 100)
		{
			markNBTUpdate();
			tick = 0;
		}
	}
	
	@Override
	public boolean canUpdate() 
	{
		return false;
	}
}