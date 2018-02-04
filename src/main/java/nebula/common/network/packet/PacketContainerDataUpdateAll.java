/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.common.network.packet;

import java.io.IOException;

import nebula.common.data.IBufferSerializer;
import nebula.common.gui.ContainerBase;
import nebula.common.gui.ContainerDataHandlerManager;
import nebula.common.gui.IContainerDataHandler;
import nebula.common.network.IPacket;
import nebula.common.network.Network;
import nebula.common.network.PacketBufferExt;

public class PacketContainerDataUpdateAll extends PacketGui
{
	private Object[][] stacks;
	
	public PacketContainerDataUpdateAll()
	{
		
	}
	
	public PacketContainerDataUpdateAll(ContainerBase container, Object[][] stacks)
	{
		super(container);
		this.stacks = stacks;
	}
	
	@Override
	protected void encode(PacketBufferExt output) throws IOException
	{
		super.encode(output);
		output.writeByte(this.stacks.length);
		for (Object[] values : this.stacks)
		{
			IBufferSerializer<PacketBufferExt, Object> serializer =
					(IBufferSerializer<PacketBufferExt, Object>) values[0];
			output.writeVarInt(values.length);
			output.writeByte(ContainerDataHandlerManager.SERIALIZERS.indexOf(serializer));
			for (int i = 1; i < values.length; ++i)
			{
				serializer.write(output, values[i]);
			}
		}
	}
	
	@Override
	protected void decode(PacketBufferExt input) throws IOException
	{
		super.decode(input);
		this.stacks = new Object[input.readByte()][];
		for (int i = 0; i < this.stacks.length; ++i)
		{
			int length = input.readVarInt();
			if (length <= 1)
				throw new IOException();
			Object[] array = this.stacks[i] = new Object[length];
			IBufferSerializer<PacketBufferExt, Object> serializer =
					(IBufferSerializer<PacketBufferExt, Object>) (array[0] = ContainerDataHandlerManager.SERIALIZERS.get(input.readByte()));
			for (int j = 1; j < length; array[j++] = serializer.read(input));
		}
	}
	
	@Override
	public IPacket process(Network network)
	{
		//The Nebula network send packet faster than local network in client side, to waiting data to get synch, it should wait some time...
		while (getPlayer().openContainer.windowId == 0)
		{
			try
			{
				synchronized (this)
				{
					wait(1);
				}
			}
			catch (InterruptedException e)
			{
			}
		}
		ContainerBase container = container();
		if (container != null)
		{
			final IContainerDataHandler handler = container.getDataHandler();
			for (Object[] stack : this.stacks)
			{
				Class<Object> class1 = ((IBufferSerializer<PacketBufferExt, Object>) stack[0]).getTargetClass();
				for (
						int i = 1;
						i < stack.length;
						handler.updateValue(class1, i - 1, stack[i]),
						++i);
			}
		}
		return null;
	}
}
