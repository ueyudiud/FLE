/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.container;

import farcore.lib.tile.IHasToolSlotTile;
import nebula.common.gui.ItemSlot;
import nebula.common.inventory.ItemContainerSimple;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

/**
 * @author ueyudiud
 */
public class ItemSlotTool extends ItemSlot
{
	private final IHasToolSlotTile tile;
	
	public ItemSlotTool(IHasToolSlotTile tile, EntityPlayer player, int index, int xPosition, int yPosition)
	{
		super(new ItemContainerSimple(0)
		{
			@Override
			protected void set(ItemStack stack)
			{
				
			}
			
			@Override
			protected ItemStack get()
			{
				return null;
			}
			
			@Override
			protected void add(int size)
			{
				
			}
			
			@Override
			public ActionResult<ItemStack> clickContainer(ItemStack stack, int modifier)
			{
				return tile.clickSlot(index, stack, player);
			}
		}, xPosition, yPosition);
		
		this.tile = tile;
	}
	
	@Override
	public boolean canPutStack(EntityPlayer player)
	{
		return false;
	}
	
	@Override
	public boolean canTakeStack(EntityPlayer player)
	{
		return false;
	}
	
	@Override
	public boolean isItemExpected(ItemStack stack)
	{
		return false;
	}
	
	@Override
	public boolean isSameInventory(Slot other)
	{
		return other == this || (other instanceof ItemSlotTool && ((ItemSlotTool) other).tile == this.tile);
	}
}
