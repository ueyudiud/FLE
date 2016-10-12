package farcore.lib.net.gui;

import farcore.lib.gui.ContainerBase;
import farcore.network.IPacket;
import farcore.network.Network;
import net.minecraft.util.ITickable;

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
		return container instanceof ITickable;
	}

	@Override
	public IPacket process(Network network)
	{
		ContainerBase container = container();
		if(container instanceof ITickable)
		{
			((ITickable) container).update();
		}
		return null;
	}
}