package farcore.lib.net.gui;

import java.io.IOException;

import farcore.lib.gui.ContainerBase;
import farcore.network.PacketAbstract;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;

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
	protected void encode(PacketBuffer output) throws IOException
	{
		output.writeInt(guiid);
	}

	@Override
	protected void decode(PacketBuffer input) throws IOException
	{
		guiid = input.readInt();
	}

	protected ContainerBase container()
	{
		EntityPlayer player = getPlayer();
		return player.openContainer.windowId == guiid ? (ContainerBase) player.openContainer : null;
	}
}