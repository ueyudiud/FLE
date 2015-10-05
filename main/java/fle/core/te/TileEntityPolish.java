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

	public void clearMap()
	{
		inv.clearMap(this);
	}
	
	public void craftedOnce(EntityPlayer player, int id)
	{
		inv.craftedOnce(this, id, player);
	}
	
	@SideOnly(Side.CLIENT)
	public char[] getStates()
	{
		return inv.inputMap;
	}

	@SideOnly(Side.CLIENT)
	public GuiCondition getCondition()
	{
		return inv.condition;
	}

	public ItemStack getRecipeInput()
	{
		return inv.input;
	}

	@Override
	public void updateInventory() 
	{
		inv.updateEntity(this);
	}
	
	@Override
	public boolean canUpdate() 
	{
		return false;
	}
}