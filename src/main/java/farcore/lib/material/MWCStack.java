/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.material;

import nebula.base.Stack;
import nebula.common.nbt.INBTCompoundReaderAndWriter;
import nebula.common.nbt.INBTSelfCompoundReaderAndWriter;
import nebula.common.util.NBTs;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author ueyudiud
 */
public class MWCStack extends Stack<Mat> implements INBTSelfCompoundReaderAndWriter
{
	private static final long serialVersionUID = -116240400653078125L;
	
	public static final INBTCompoundReaderAndWriter<MWCStack> HANDLER = new INBTCompoundReaderAndWriter<MWCStack>()
	{
		@Override
		public MWCStack readFrom(NBTTagCompound nbt)
		{
			return loadFromNBT(nbt);
		}
		
		@Override
		public void writeTo(MWCStack target, NBTTagCompound nbt)
		{
			target.writeTo(nbt);
		}
	};
	
	public MatCondition condition;
	
	public static MWCStack loadFromNBT(NBTTagCompound nbt)
	{
		MWCStack stack = new MWCStack();
		stack.readFrom(nbt);
		return stack.element == null ? null : stack;
	}
	
	private MWCStack()
	{
		super(null, 0L);
	}
	
	public MWCStack(Mat material, MatCondition condition)
	{
		super(material);
		this.condition = condition;
	}
	
	public MWCStack(Mat material, MatCondition condition, long size)
	{
		super(material, size);
		this.condition = condition;
	}
	
	@Override
	public MWCStack split(long size)
	{
		MWCStack stack = new MWCStack(this.element, this.condition, size);
		this.size -= size;
		return stack;
	}
	
	@Override
	public boolean same(Stack stack)
	{
		return (stack instanceof MWCStack) ? same((MWCStack) stack) : super.same(stack);
	}
	
	public boolean same(MWCStack stack)
	{
		return stack.element == this.element && stack.condition == this.condition;
	}
	
	@Override
	public int hashCode()
	{
		return super.hashCode() * 31 + this.condition.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return obj == this ? true : !(obj instanceof MWCStack) ? false : ((MWCStack) obj).element == this.element && ((MWCStack) obj).size == this.size && ((MWCStack) obj).condition == this.condition;
	}
	
	@Override
	public String toString()
	{
		return new StringBuilder().append('(').append(this.element).append('[').append(this.condition).append("])x").append(this.size).toString();
	}
	
	@Override
	public MWCStack clone()
	{
		return new MWCStack(this.element, this.condition, this.size);
	}
	
	public void readFrom(NBTTagCompound nbt)
	{
		this.element = Mat.getMaterialByNameOrDefault(nbt, "material", null);
		if (this.element != null)
		{
			this.condition = MatCondition.register.get(nbt.getString("condition"));
			if (this.condition == null)
			{
				this.element = null;
			}
			else
			{
				this.size = nbt.getLong("size");
			}
		}
	}
	
	@Override
	public void writeTo(NBTTagCompound nbt)
	{
		nbt.setString("material", this.element.name);
		nbt.setString("condition", this.condition.name);
		NBTs.setNumber(nbt, "size", this.size);
	}
}
