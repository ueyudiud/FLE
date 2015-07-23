package fla.core.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fla.api.world.BlockPos;
import fla.core.gui.InventoryPolishTable;
import fla.core.tileentity.base.TileEntityInventory;

public class TileEntityPolishTable extends TileEntityInventory<InventoryPolishTable>
{
	public TileEntityPolishTable() 
	{
		super(new InventoryPolishTable());
	}

	public void clearMap()
	{
		inv.clearMap(this);
	}
	
	public void craftedOnce(EntityPlayer player)
	{
		inv.craftedOnce(this, player);
	}
	
	public void changeSelect()
	{
		inv.changeSelect(this);
	}
	
	@SideOnly(Side.CLIENT)
	public char[] getStates()
	{
		return inv.inputMap;
	}
	
	public void initSelectSlot()
	{
		inv.selectState = 0;
	}

	@Override
	public void updateEntity() 
	{
		inv.updateEntity(this);
	}

	@Override
	public boolean canSetDirection(World world, BlockPos pos) 
	{
		return false;
	}

	@Override
	public boolean canSetDirectionWith(World world, BlockPos pos, double xPos,
			double yPos, double zPos, ItemStack itemstack) 
	{
		return false;
	}

	@Override
	public ForgeDirection setDirectionWith(World world, BlockPos pos,
			double xPos, double yPos, double zPos, ItemStack itemstack) 
	{
		return null;
	}

	@SideOnly(Side.CLIENT)
	public int getSelectID() 
	{
		return inv.selectState;
	}

}
