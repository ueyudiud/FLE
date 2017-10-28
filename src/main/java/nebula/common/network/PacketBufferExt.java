/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.network;

import java.io.IOException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import nebula.common.fluid.FluidStackExt;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

/**
 * The improved packet buffer provided more helper
 * methods.
 * @author ueyudiud
 */
public class PacketBufferExt extends PacketBuffer
{
	public PacketBufferExt(byte[] datas)
	{
		super(Unpooled.wrappedBuffer(datas));
	}
	public PacketBufferExt(ByteBuf wrapped)
	{
		super(wrapped);
	}
	
	public FluidStackExt readFluidStack() throws IOException
	{
		int amt = readInt();
		if (amt > 0)
		{
			boolean marker = readBoolean();
			Fluid fluid = FluidRegistry.getFluid(readShort());
			if (marker)//Fluid stack type maker.
			{
				int temperature = readInt();
				return new FluidStackExt(fluid, amt, temperature, readCompoundTag());
			}
			else
			{
				return new FluidStackExt(fluid, amt, readCompoundTag());
			}
		}
		return null;
	}
	
	public void writeFluidStack(FluidStack stack) throws IOException
	{
		if (stack == null)
		{
			writeInt(0);
		}
		else
		{
			writeInt(stack.amount);
			boolean flag = stack instanceof FluidStackExt;//Mark fluid stack type.
			writeBoolean(flag);
			writeShort(FluidRegistry.getFluidID(stack.getFluid()));
			if (flag)
			{
				writeInt(((FluidStackExt) stack).temperature);
			}
			writeCompoundTag(stack.tag);
		}
	}
	
	public void readFixedIntArray(int[] array) throws IOException
	{
		for (int i = 0; i < array.length; ++i)
		{
			array[i] = readInt();
		}
	}
	
	public void writeFixedIntArray(int[] array) throws IOException
	{
		for (int element : array) { writeInt(element); }
	}
}