/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.solid;

import java.io.IOException;
import java.util.function.UnaryOperator;

import javax.annotation.Nullable;

import nebula.base.register.IRegisterDelegate;
import nebula.common.data.IBufferSerializer;
import nebula.common.nbt.INBTCompoundReaderAndWritter;
import nebula.common.util.ItemStacks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;

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
	 * Solid stack serializer.
	 */
	public static final DataSerializer<SolidStack> SERIALIZER = new DataSerializer<SolidStack>()
	{
		@Override
		public void write(PacketBuffer buf, SolidStack value)
		{
			if (value == null)
				buf.writeBoolean(false);
			else
			{
				buf.writeBoolean(true);
				buf.writeShort(value.delegate.id());
				buf.writeInt(value.amount);
				buf.writeCompoundTag(value.tag);
			}
		}
		
		@Override
		public SolidStack read(PacketBuffer buf) throws IOException
		{
			return buf.readBoolean() ?
					new SolidStack(
							Solid.REGISTRY.getDelegete(buf.readShort()),
							buf.readInt(), buf.readCompoundTag()) :
								null;
		}
		
		@Override
		public DataParameter<SolidStack> createKey(int id)
		{
			return new DataParameter<>(id, this);
		}
	};
	
	public static final IBufferSerializer<PacketBuffer, SolidStack> BS = new IBufferSerializer<PacketBuffer, SolidStack>()
	{
		@Override
		public void write(PacketBuffer buffer, SolidStack value)
		{
			SERIALIZER.write(buffer, value);
		}
		
		@Override
		public SolidStack read(PacketBuffer buffer) throws IOException
		{
			return SERIALIZER.read(buffer);
		}
		
		@Override
		public Class<SolidStack> getTargetClass()
		{
			return SolidStack.class;
		}
	};
	
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
	
	public int									amount;
	public NBTTagCompound						tag;
	private IRegisterDelegate<Solid>	delegate;
	
	public static SolidStack sizeOf(SolidStack stack, int amount)
	{
		SolidStack stack1 = new SolidStack(stack);
		stack1.amount = amount;
		return stack1;
	}
	
	public static SolidStack loadFromNBT(NBTTagCompound nbt)
	{
		SolidStack stack;
		return (stack = new SolidStack()).readFromNBT(nbt).delegate == null ? null : stack;
	}
	
	public static boolean areStackEqual(SolidStack stack1, SolidStack stack2)
	{
		return stack1 == stack2 ? true :
			stack1 == null || stack2 == null ? false :
				stack1.isSoildEqual(stack2) && stack1.amount == stack2.amount;
	}
	
	protected SolidStack()
	{
	}
	
	protected SolidStack(IRegisterDelegate<Solid> delegate, int amount, NBTTagCompound nbt)
	{
		this.delegate = delegate;
		this.amount = amount;
		this.tag = nbt != null ? nbt.copy() : null;
	}
	
	public SolidStack(Solid solid, int amount)
	{
		this.delegate = solid.delegate;
		this.amount = amount;
	}
	
	public SolidStack(Solid solid, int amount, NBTTagCompound nbt)
	{
		this(solid.delegate, amount, nbt);
	}
	
	SolidStack(SolidStack stack)
	{
		this(stack.delegate, stack.amount, stack.tag);
	}
	
	public final Solid getSolid()
	{
		return this.delegate.get();
	}
	
	public SolidStack splitStack(int amount)
	{
		this.amount -= amount;
		return sizeOf(this, amount);
	}
	
	private SolidStack readFromNBT(NBTTagCompound nbt)
	{
		if (nbt.hasKey("SolidName"))
		{
			this.delegate = Solid.REGISTRY.getDelegate(nbt.getString("SolidName"));
			this.amount = nbt.getInteger("Amount");
			if (nbt.hasKey("Tag"))
			{
				this.tag = nbt.getCompoundTag("Tag");
			}
		}
		return this;
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString("SolidName", this.delegate.name());
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
		return other == null ? false : this.delegate.equals(other.delegate) && ItemStacks.areTagEqual(this.tag, other.tag);
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
