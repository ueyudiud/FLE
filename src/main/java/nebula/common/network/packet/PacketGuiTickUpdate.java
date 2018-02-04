/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.common.network.packet;

import nebula.common.gui.ContainerBase;
import nebula.common.network.IPacket;
import nebula.common.network.Network;
import net.minecraft.util.ITickable;

/**
 * The ClientToServer packet.
 * <p>
 * Sent when GUI ticking each tick in client.
 * 
 * @author ueyudiud
 */
public class PacketGuiTickUpdate extends PacketGui
{
	ContainerBase container;
	
	public PacketGuiTickUpdate()
	{
	}
	
	public PacketGuiTickUpdate(ContainerBase container)
	{
		super(container);
		this.container = container;
	}
	
	@Override
	public boolean needToSend()
	{
		return this.container instanceof ITickable;
	}
	
	@Override
	public IPacket process(Network network)
	{
		ContainerBase container = container();
		if (container instanceof ITickable)
		{
			((ITickable) container).update();
		}
		return null;
	}
}
