/*
 * copyright© 2016-2017 ueyudiud
 */
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
		this.guiid = container.windowId;
	}
	
	@Override
	protected void encode(PacketBufferExt output) throws IOException
	{
		output.writeInt(this.guiid);
	}
	
	@Override
	protected void decode(PacketBufferExt input) throws IOException
	{
		this.guiid = input.readInt();
	}
	
	protected ContainerBase container()
	{
		EntityPlayer player = getPlayer();
		return player.openContainer.windowId == this.guiid && (player.openContainer instanceof ContainerBase) ? (ContainerBase) player.openContainer : null;
	}
}
