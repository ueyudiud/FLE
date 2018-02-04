/*
 * copyright© 2016-2018 ueyudiud
 */

package nebula.common.inventory;

import nebula.common.stack.AbstractStack;
import nebula.common.tile.IItemHandlerIO;
import nebula.common.util.Direction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

/**
 * TODO
 * 
 * @author ueyudiud
 */
public class InventoryIO extends InventorySimple implements IItemHandlerIO
{
	public InventoryIO(int size)
	{
		super(size);
	}
	
	@Override
	public boolean canExtractItem(Direction to)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean canInsertItem(Direction from, ItemStack stack)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public ItemStack extractItem(int size, Direction to, boolean simulate)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public ItemStack extractItem(AbstractStack suggested, Direction to, boolean simulate)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int insertItem(ItemStack stack, Direction from, boolean simulate)
	{
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public ActionResult<ItemStack> onPlayerTryUseIO(ItemStack current, EntityPlayer player, Direction side, float x, float y, float z, boolean isActiveHeld)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
