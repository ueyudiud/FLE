/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.foodstat;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.Event.HasResult;

/**
 * This event is only handle food stat change,
 * the itemstack change to handle in other event.
 * Posted in {@link MinecraftForge#EVENT_BUS}
 * @author ueyudiud
 */
@Cancelable
@HasResult
public class FoodEatenEvent extends Event
{
	public final FoodStatExt stat;
	public final Item item;
	public final ItemStack stack;
	
	public FoodEatenEvent(FoodStatExt stat, Item item, ItemStack stack)
	{
		this.stat = stat;
		this.item = item;
		this.stack = stack.copy();
	}
}