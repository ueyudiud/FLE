package farcore.network;

import java.io.IOException;

import farcore.lib.fluid.FluidStackExt;
import farcore.util.U;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;

public abstract class PacketAbstract implements IPacket
{
	protected Side side;
	protected INetHandler handler;

	@Override
	public Side getSide()
	{
		return side;
	}

	@Override
	public void side(Side side)
	{
		this.side = side;
	}

	@Override
	public void handler(INetHandler handler)
	{
		this.handler = handler;
	}

	@Override
	public EntityPlayer getPlayer()
	{
		return (handler instanceof NetHandlerPlayServer) ?
				((NetHandlerPlayServer) handler).playerEntity :
					U.Players.player();
	}

	@Override
	public ByteBuf encode(ByteBuf buf) throws IOException
	{
		encode(new PacketBuffer(buf));
		return buf;
	}

	protected abstract void encode(PacketBuffer output) throws IOException;

	@Override
	public void decode(ByteBuf buf) throws IOException
	{
		decode(new PacketBuffer(buf));
	}

	protected abstract void decode(PacketBuffer input) throws IOException;

	@Override
	public boolean needToSend()
	{
		return true;
	}
	
	protected FluidStackExt readFluidStack(PacketBuffer buffer) throws IOException
	{
		int amt = buffer.readInt();
		if (amt > 0)
		{
			if (buffer.readBoolean())
				return new FluidStackExt(FluidRegistry.getFluid(buffer.readShort()), amt, buffer.readNBTTagCompoundFromBuffer());
			else
				return new FluidStackExt(FluidRegistry.getFluid(buffer.readShort()), amt, buffer.readInt(), buffer.readNBTTagCompoundFromBuffer());
		}
		return null;
	}
	
	protected void writeFluidStack(PacketBuffer buffer, FluidStack stack) throws IOException
	{
		if (stack == null)
		{
			buffer.writeInt(0);
		}
		else
		{
			buffer.writeInt(stack.amount);
			boolean flag = stack instanceof FluidStackExt;
			buffer.writeBoolean(flag);
			buffer.writeShort(FluidRegistry.getFluidID(stack.getFluid()));
			if (flag)
			{
				buffer.writeInt(((FluidStackExt) stack).temperature);
			}
			buffer.writeNBTTagCompoundToBuffer(stack.tag);
		}
	}
}