/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.inventory;

import java.util.Iterator;

import com.google.common.collect.Iterators;

import nebula.base.INode;
import nebula.base.Node;
import nebula.common.util.ItemStacks;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public class InventoryTypeUnlimited implements IBasicInventory
{
	private int limit;
	private int size;
	private int slots;
	private INode<ItemStack> stacks;
	
	private void recaculateSize()
	{
		this.size = 0;
		this.slots = 0;
		if (this.stacks == null) return;
		for (ItemStack stackRaw : this.stacks)
		{
			ItemStack stack = ItemStacks.valid(stackRaw);
			if (stack != null)
			{
				this.size += stack.stackSize;
				this.slots += 1;
			}
		}
	}
	
	private void merge()
	{
		if (this.stacks == null) return;
		INode<ItemStack> node1 = this.stacks;
		while (node1.hasNext())
		{
			ItemStack stack = node1.value();
			if (stack == null)
			{
				node1 = node1.next();
				node1.last().remove();
			}
			else
			{
				Iterator<ItemStack> itr = node1.iterator();
				itr.forEachRemaining(s-> {
					if (ItemStacks.isItemAndTagEqual(stack, s))
					{
						stack.stackSize += s.stackSize;
						itr.remove();
					}
				});
				node1 = node1.next();
			}
		}
	}
	
	@Override
	public ItemStack[] toArray()
	{
		return this.stacks == null ? null : this.stacks.toArray(ItemStack.class);
	}
	
	@Override
	public void fromArray(ItemStack[] stacks)
	{
		this.stacks = Node.chain(stacks);
		merge();
		recaculateSize();
	}
	
	@Override
	public int getSizeInventory()
	{
		return this.slots + 1;
	}
	
	@Override
	public ItemStack getStack(int index)
	{
		return this.stacks == null || index >= this.slots ? null : Iterators.get(this.stacks.iterator(), index);
	}
	
	@Override
	public int incrStack(int index, ItemStack resource, boolean process)
	{
		if (this.size == this.limit) return 0;
		if (index < this.slots)
		{
			ItemStack stack = Iterators.get(this.stacks.iterator(), index);
			assert (stack != null);
			if (!ItemStacks.isItemAndTagEqual(stack, resource))
			{
				return 0;
			}
			int in = Math.min(this.limit - this.size, resource.stackSize);
			if (process)
			{
				stack.stackSize += in;
				this.size += in;
			}
			return in;
		}
		else
		{
			int in = Math.min(this.limit - this.size, resource.stackSize);
			if (process && in > 0)
			{
				if (this.stacks == null)
				{
					this.stacks = Node.first(ItemStacks.sizeOf(resource, in));
				}
				else
				{
					this.stacks.addLast(ItemStacks.sizeOf(resource, in));
				}
				this.size += in;
			}
			return in;
		}
	}
	
	@Override
	public ItemStack decrStack(int index, int count, boolean process)
	{
		return null;
	}
	
	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		if (this.stacks == null) return null;
		if (index == 0)
		{
			ItemStack result = this.stacks.value();
			this.stacks = null;
			recaculateSize();
			return result;
		}
		INode<ItemStack> node = this.stacks;
		int i = 0;
		for (; i < index && node.hasNext(); ++i)
		{
			node = this.stacks.next();
		}
		if (i == index)
		{
			ItemStack stack = node.remove();
			this.size -= stack.stackSize;
			this.slots --;
			return stack;
		}
		return null;
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		if (this.stacks == null)
		{
			this.stacks = Node.first(ItemStacks.copyNomoreThan(stack, this.limit));
		}
		else
		{
			this.stacks.addNext(ItemStacks.copyNomoreThan(stack, this.limit));
		}
	}
}