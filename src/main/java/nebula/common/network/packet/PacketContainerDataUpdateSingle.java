/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.common.network.packet;

import java.io.IOException;

import nebula.common.data.IBufferSerializer;
import nebula.common.gui.ContainerBase;
import nebula.common.gui.ContainerDataHandlerManager;
import nebula.common.network.IPacket;
import nebula.common.network.Network;
import nebula.common.network.PacketBufferExt;

public class PacketContainerDataUpdateSingle extends PacketGui
{
	private IBufferSerializer<PacketBufferExt, Object>	serializer;
	private int											id;
	private Object										value;
	
	public PacketContainerDataUpdateSingle()
	{
	}
	
	public <T> PacketContainerDataUpdateSingle(ContainerBase container, IBufferSerializer<?, ? super T> serializer, int id, T value)
	{
		super(container);
		this.serializer = (IBufferSerializer<PacketBufferExt, Object>) serializer;
		this.id = id;
		this.value = value;
	}
	
	@Override
	protected void encode(PacketBufferExt output) throws IOException
	{
		super.encode(output);
		output.writeByte(this.id);
		output.writeByte(ContainerDataHandlerManager.SERIALIZERS.indexOf(this.serializer));
		this.serializer.write(output, this.value);
	}
	
	@Override
	protected void decode(PacketBufferExt input) throws IOException
	{
		super.decode(input);
		this.id = input.readByte();
		this.serializer = (IBufferSerializer<PacketBufferExt, Object>) ContainerDataHandlerManager.SERIALIZERS.get(input.readByte());
		this.value = this.serializer.read(input);
	}
	
	@Override
	public IPacket process(Network network)
	{
		ContainerBase container = container();
		if (container != null)
		{
			container.getDataHandler().updateValue(this.serializer.getTargetClass(), this.id, this.value);
		}
		return null;
	}
}
