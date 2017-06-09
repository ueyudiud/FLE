/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.api.mat;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import nebula.base.Stack;
import nebula.common.nbt.INBTReader;
import nebula.common.nbt.INBTWriter;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;

/**
 * @author ueyudiud
 */
public class StackContainer<E>
{
	private long capacity;
	private long amount;
	private Map<E, Stack<E>> stacks = new HashMap<>();
	
	public StackContainer(long capacity)
	{
		this.capacity = capacity;
	}
	
	public <N extends NBTBase> void readFromNBT(NBTTagCompound compound, String key, INBTReader<E, N> reader)
	{
		readFromNBT(compound.getTagList(key, NBT.TAG_COMPOUND), reader);
	}
	
	public synchronized <N extends NBTBase> void readFromNBT(NBTTagList list, INBTReader<E, N> reader)
	{
		clear();
		for (int i = 0; i < list.tagCount(); ++i)
		{
			NBTTagCompound compound = list.getCompoundTagAt(i);
			E e = reader.readFromNBT(compound, "e");
			if (e != null)
			{
				long size = compound.getLong("s");
				this.stacks.put(e, new Stack<>(e, size));
				this.amount += size;
			}
		}
	}
	
	public <N extends NBTBase> void writeToNBT(NBTTagCompound compound, String key, INBTWriter<E, N> writer)
	{
		compound.setTag(key, writeToNBT(new NBTTagList(), writer));
	}
	
	public <N extends NBTBase> NBTTagList writeToNBT(NBTTagList list, INBTWriter<E, N> writer)
	{
		NBTTagCompound compound;
		for (Stack<E> stack : this.stacks.values())
		{
			compound = new NBTTagCompound();
			compound.setTag("e", writer.writeToNBT(stack.element));
			compound.setLong("s", stack.size);
			list.appendTag(compound);
		}
		return list;
	}
	
	public long getCapacity()
	{
		return this.capacity;
	}
	
	public long getAmount()
	{
		return this.amount;
	}
	
	public long getAmount(E e)
	{
		Stack<E> stack = this.stacks.get(e);
		return stack == null ? 0 : stack.size;
	}
	
	public double getContain(E e)
	{
		return (double) getAmount(e) / (double) this.amount;
	}
	
	public boolean isEmpty()
	{
		return this.amount == 0;
	}
	
	public boolean isFull()
	{
		return this.amount == this.capacity;
	}
	
	public synchronized long fill(E e, long size)
	{
		long l = Math.min(size, this.capacity - this.amount);
		if (l <= 0) return 0L;
		this.amount += l;
		Stack<E> source = this.stacks.get(e);
		if (source == null)
		{
			this.stacks.put(e, new Stack<>(e, l));
		}
		else
		{
			source.size += l;
		}
		return l;
	}
	
	public long fill(Stack<E> stack)
	{
		return fill(stack.element, stack.size);
	}
	
	public Stack<E> drain(E e)
	{
		return drain(e, Long.MAX_VALUE);
	}
	
	public synchronized Stack<E> drain(E e, long maxDrain)
	{
		Stack<E> stack = this.stacks.get(e);
		if (stack != null)
		{
			if (stack.size >= maxDrain)
			{
				this.stacks.remove(e);
				this.amount -= stack.size;
				return stack;
			}
			else
			{
				this.amount -= maxDrain;
				return stack.split(maxDrain);
			}
		}
		else return null;
	}
	
	public synchronized void clear()
	{
		this.amount = 0;
		this.stacks.clear();
	}
	
	public Collection<Stack<E>> getList()
	{
		return Collections.unmodifiableCollection(this.stacks.values());
	}
}