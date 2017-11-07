/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.network.packet;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import nebula.common.gui.IGuiDataReciever;
import nebula.common.network.Encoder;
import nebula.common.network.IPacket;
import nebula.common.network.Network;
import nebula.common.network.PacketAbstract;
import nebula.common.network.PacketBufferExt;
import net.minecraft.entity.player.EntityPlayer;

/**
 * The ClientToServer packet.
 * <p>
 * The custom coding sending packet from player opening container.
 * 
 * @author ueyudiud
 */
public class PacketGuiSyncData extends PacketAbstract
{
	@Nullable
	Encoder	encoder;
	byte[]	data;
	
	public PacketGuiSyncData()
	{
	}
	
	public PacketGuiSyncData(@Nonnull Encoder encoder)
	{
		this.encoder = encoder;
	}
	
	@Override
	public IPacket process(Network network) throws IOException
	{
		EntityPlayer player = getPlayer();
		if (player.openContainer instanceof IGuiDataReciever)
		{
			((IGuiDataReciever) player.openContainer).readData(new PacketBufferExt(this.data));
		}
		return null;
	}
	
	@Override
	protected void encode(PacketBufferExt output) throws IOException
	{
		this.encoder.encode(output);
	}
	
	@Override
	protected void decode(PacketBufferExt input) throws IOException
	{
		this.data = input.readByteArray();
	}
}
