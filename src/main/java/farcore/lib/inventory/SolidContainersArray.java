/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.inventory;

import java.util.Arrays;

import farcore.lib.solid.SolidStack;
import nebula.base.collection.A;
import nebula.common.inventory.IContainersArray;
import nebula.common.nbt.NBTs;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author ueyudiud
 */
public class SolidContainersArray extends SolidContainers<SolidContainerArraySimple> implements IContainersArray<SolidStack>
{
	private static SolidContainerArraySimple[] create(SolidStack[] stacks, int capacity)
	{
		return A.fill(new SolidContainerArraySimple[stacks.length],
				i -> new SolidContainerArraySimple(stacks, i, capacity));
	}
	
	public final SolidStack[] stacks;
	
	public SolidContainersArray(SolidStack[] stacks, int capacity)
	{
		super(create(stacks, capacity));
		this.stacks = stacks;
	}
	
	@Override
	public NBTTagCompound writeTo(NBTTagCompound nbt, String key)
	{
		NBTs.set(nbt, key, this.stacks, SolidStack.RW);
		return nbt;
	}
	
	@Override
	public void readFrom(NBTTagCompound nbt, String key)
	{
		NBTs.get(nbt, key, this.stacks, SolidStack.RW);
	}
	
	@Override
	public void fromArray(SolidStack[] array)
	{
		A.fill(this.stacks, 0, Math.min(this.stacks.length, array.length), i -> SolidStack.copyOf(array[i]));
		if (this.stacks.length > array.length)
		{
			Arrays.fill(this.stacks, array.length, this.stacks.length, null);
		}
	}
	
	@Override
	public SolidStack[] toArray()
	{
		SolidStack[] result = new SolidStack[this.stacks.length];
		A.fill(result, i -> SolidStack.copyOf(this.stacks[i]));
		return result;
	}
	
	@Override
	public void clear()
	{
		Arrays.fill(this.stacks, null);
	}
	
	@Override
	public void clearRange(int from, int to)
	{
		Arrays.fill(this.stacks, from, to, null);
	}
}
