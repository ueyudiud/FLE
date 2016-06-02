package fle.api.item;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import farcore.util.U;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;

public class BowlEvent extends Event
{
	public final EntityPlayer player;
	public final ItemStack stack;
	
	public BowlEvent(EntityPlayer player, ItemStack stack)
	{
		this.player = player;
		this.stack = stack;
	}
	
	@Cancelable
	public static class BowlUseEvent extends BowlEvent
	{
		private ItemStack result;
		public final MovingObjectPosition mop;
		
		public BowlUseEvent(EntityPlayer player, ItemStack stack, MovingObjectPosition position)
		{
			super(player, stack);
			this.mop = position;
			this.result = ItemStack.copyItemStack(stack);
		}
		
		public void setStack(ItemStack result)
		{
			this.result = result;
		}
		
		public ItemStack getStack()
		{
			return result;
		}
	}
}