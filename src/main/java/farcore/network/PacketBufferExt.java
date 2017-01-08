/*
 * copyright© 2016 ueyudiud
 */

package farcore.network;

import java.io.IOException;

import farcore.lib.fluid.FluidStackExt;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

/**
 * @author ueyudiud
 */
public class PacketBufferExt extends PacketBuffer
{
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
				return new FluidStackExt(fluid, amt, temperature, readNBTTagCompoundFromBuffer());
			}
			else
			{
				return new FluidStackExt(fluid, amt, readNBTTagCompoundFromBuffer());
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
			writeNBTTagCompoundToBuffer(stack.tag);
		}
	}
}