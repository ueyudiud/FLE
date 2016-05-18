package farcore.interfaces.item;

import farcore.util.U;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * The interface of item which show custom behavior in
 * container (extended ContainerBase).<br>
 * This provide some custom method for them.<br>
 * @author ueyudiud
 * @see farcore.lib.container.ContainerBase
 */
public interface IContainerCustomBehaviorItem
{
	/**
	 * Check whether this slot can access stack for input.
	 * @param slot
	 * @param stack
	 * @param target
	 * @return
	 */
	default boolean canAccessStack(Slot slot, ItemStack stack, ItemStack target)
	{
		return U.Inventorys.areStackSimilar(stack, target);
	}
	
	/**
	 * Split stack when click target into slot.<br>
	 * 
	 * @param slot The inputed slot.
	 * @param stack The stack current before put.
	 * @param target The stack wait to split into slot.
	 * @param clickMode The mode of click, (true is left click, false is right click), left click is
	 * suggested use full target size, or use single target size.
	 * @return
	 */
	default ItemStack splitSize(Slot slot, ItemStack stack, ItemStack target, boolean clickMode)
	{
		int m = Math.min(slot.getSlotStackLimit(), stack.getMaxStackSize());
		if(clickMode)
		{
			int l = Math.min(m - stack.stackSize, target.stackSize);
			stack.stackSize += l;
			slot.putStack(stack.copy());
			target.stackSize -= l;
			return target.stackSize <= 0 ? null : target;
		}
		else
		{
			if(stack.stackSize < m - stack.stackSize)
			{
				stack.stackSize += 1;
				slot.putStack(stack.copy());
				target.stackSize -= 1;
			}
			return target.stackSize <= 0 ? null : target;
		}
	}
	
	/**
	 * Called when right click this slot (Left click will put all
	 * stack out), usually to divide stack to two half. 
	 * @param slot
	 * @param stack
	 * @return
	 */
	default ItemStack splitItemWhenRightClick(Slot slot, ItemStack stack)
	{
		if(stack.stackSize == 1)
		{
			slot.putStack(null);
			return stack;
		}
		else
		{
			ItemStack ret = slot.decrStackSize((1 + stack.stackSize) / 2);
			if(stack.stackSize == 0)
			{
				slot.putStack(null);
			}
			return ret;
		}
	}
	
	/**
	 * Get slot merge behavior.<br>
	 * 1 means not merge this stack.
	 * 2 means merge this stack before range check.
	 * 4 means merge this stack after empty check.
	 * @param slot The slot current this stack.
	 * @param stack The stack current in slot.
	 * @param merging The merging stack (NOT THIS STACK).
	 * @return
	 */
	default byte getMergeBehavior(Slot slot, ItemStack stack, ItemStack merging)
	{
		return 0;
	}
	
	/**
	 * Check whether this stack can be merged.
	 * @return
	 */
	default boolean isMergeable(ItemStack stack)
	{
		return true;
	}
}