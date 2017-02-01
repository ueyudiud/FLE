package nebula.common.network.packet;

import java.io.IOException;

import nebula.common.gui.ContainerBase;
import nebula.common.network.PacketAbstract;
import nebula.common.network.PacketBufferExt;
import net.minecraft.entity.player.EntityPlayer;

public abstract class PacketGui extends PacketAbstract
{
	private int guiid;
	
	public PacketGui()
	{
		
	}
	public PacketGui(ContainerBase container)
	{
		guiid = container.windowId;
	}
	
	@Override
	protected void encode(PacketBufferExt output) throws IOException
	{
		output.writeInt(guiid);
	}
	
	@Override
	protected void decode(PacketBufferExt input) throws IOException
	{
		guiid = input.readInt();
	}
	
	protected ContainerBase container()
	{
		EntityPlayer player = getPlayer();
		return player.openContainer.windowId == guiid ? (ContainerBase) player.openContainer : null;
	}
}