/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.inventory;

import farcore.lib.solid.SolidStack;
import farcore.lib.solid.Subsolid;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author ueyudiud
 */
public class SolidContainerSimpleSimulated extends SolidContainerSimple
{
	protected SolidContainerSimple container;
	protected SolidStack stack;
	
	protected SolidContainerSimpleSimulated(SolidContainerSimple container)
	{
		super(container.getCapacity());
		this.container = container;
		this.stack = container.get();
		this.modCount = container.modCount();
	}
	
	@Override
	public NBTTagCompound writeTo(NBTTagCompound nbt, String name)
	{
		return nbt;
	}
	
	@Override
	public void writeTo(NBTTagCompound nbt)
	{
		
	}
	
	@Override
	public void readFrom(NBTTagCompound nbt, String key)
	{
		
	}
	
	@Override
	public void readFrom(NBTTagCompound nbt)
	{
		
	}
	
	@Override
	public boolean isSimulated()
	{
		return true;
	}
	
	@Override
	public boolean isAvailable(SolidStack stack)
	{
		return this.container.isAvailable(stack);
	}
	
	@Override
	public boolean isAvailable(Subsolid solid)
	{
		return this.container.isAvailable(solid);
	}
	
	@Override
	protected SolidStack get()
	{
		if (this.modCount < this.container.modCount())
		{
			this.stack = this.container.get();
			this.modCount = this.container.modCount();
		}
		return this.stack;
	}
	
	@Override
	protected void add(int size)
	{
		get().amount += size;
	}
	
	@Override
	protected void set(SolidStack stack)
	{
		this.stack = stack;
	}
	
	@Override
	public void merge()
	{
		if (this.modCount > this.container.modCount())
		{
			this.container.set(this.stack);
			this.container.refresh();
			this.modCount = this.container.modCount();
		}
		else
		{
			this.stack = this.container.get();
			this.modCount = this.container.modCount();
		}
	}
}
