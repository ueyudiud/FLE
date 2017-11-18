/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.solid;

import java.util.function.UnaryOperator;

import javax.annotation.Nullable;

import nebula.common.nbt.INBTCompoundReaderAndWritter;
import nebula.common.util.ItemStacks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.registry.RegistryDelegate;

/**
 * Solid stack. Like fluid stack, this type provide stack of solid.
 * <p>
 * 
 * @author ueyudiud
 */
public class SolidStack
{
	public static final UnaryOperator<SolidStack> COPY_SOLIDSTACK = SolidStack::copyOf;
	
	/**
	 * Solid stack reader and writer.
	 */
	public static final INBTCompoundReaderAndWritter<SolidStack> RW = new INBTCompoundReaderAndWritter<SolidStack>()
	{
		@Override
		public SolidStack readFromNBT(NBTTagCompound nbt)
		{
			return loadFromNBT(nbt);
		}
		
		@Override
		public void writeToNBT(SolidStack target, NBTTagCompound nbt)
		{
			target.writeToNBT(nbt);
		}
	};
	
	public int								amount;
	public NBTTagCompound					tag;
	private RegistryDelegate<SolidAbstract>	solidDelegate;
	
	public static SolidStack sizeOf(SolidStack stack, int amount)
	{
		SolidStack stack1 = new SolidStack(stack);
		stack1.amount = amount;
		return stack1;
	}
	
	public static SolidStack loadFromNBT(NBTTagCompound nbt)
	{
		SolidStack stack;
		return (stack = new SolidStack()).readFromNBT(nbt).solidDelegate == null ? null : stack;
	}
	
	protected SolidStack()
	{
	}
	
	public SolidStack(SolidAbstract solid, int amount)
	{
		this.solidDelegate = solid.delegate;
		this.amount = amount;
	}
	
	public SolidStack(SolidAbstract solid, int amount, NBTTagCompound nbt)
	{
		this(solid, amount);
		if (nbt != null)
		{
			this.tag = nbt.copy();
		}
	}
	
	SolidStack(SolidStack stack)
	{
		this.solidDelegate = stack.solidDelegate;
		this.amount = stack.amount;
		if (stack.tag != null)
		{
			this.tag = stack.tag.copy();
		}
	}
	
	public final SolidAbstract getSolid()
	{
		return this.solidDelegate.get();
	}
	
	public SolidStack splitStack(int amount)
	{
		this.amount -= amount;
		return sizeOf(this, amount);
	}
	
	public SolidStack readFromNBT(NBTTagCompound nbt)
	{
		this.amount = nbt.getInteger("Amount");
		if (nbt.hasKey("Tag"))
		{
			this.tag = nbt.getCompoundTag("Tag");
		}
		return this;
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString("SolidName", this.solidDelegate.get().getRegistryName().toString());
		nbt.setInteger("Amount", this.amount);
		if (this.tag != null)
		{
			nbt.setTag("Tag", this.tag);
		}
		return nbt;
	}
	
	/**
	 * @param other the other solid stack.
	 * @return return <tt>true</tt> if solid and NBT of two stack are equal.
	 */
	public boolean isSoildEqual(SolidStack other)
	{
		return other == null ? false : other.getSolid() == getSolid() && ItemStacks.areTagEqual(this.tag, other.tag);
	}
	
	/**
	 * Copy a solid stack, the stack is nullable.
	 * 
	 * @param stack the source stack.
	 * @return the copied solid stack.
	 */
	public static SolidStack copyOf(@Nullable SolidStack stack)
	{
		return stack == null ? null : stack.copy();
	}
	
	public SolidStack copy()
	{
		return new SolidStack(this);
	}
	
	public boolean containsStack(SolidStack stack)
	{
		return stack == null || (isSoildEqual(stack) && this.amount >= stack.amount);
	}
}
