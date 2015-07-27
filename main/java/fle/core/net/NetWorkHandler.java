package fle.core.net;

import io.netty.buffer.ByteBufInputStream;

import java.io.DataInputStream;
import java.io.InputStream;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import fle.FLE;
import fle.api.net.FleNetworkHandler;
import fle.api.net.INetEventListener;
import fle.api.util.FleDataInputStream;
import fle.core.net.FlePackets.NetEventListenerInfo;
import fle.core.net.FlePackets.PacketGuiUpdate;
import fle.core.net.FlePackets.PacketUpdateTile;
import fle.core.net.FlePackets.TileEntityUpdateInfo;

public class NetWorkHandler extends FleNetworkHandler
{
	public static final int tileUpdateID = 1;
	public static final int guiUpdateID = 2;
	
	static FMLEventChannel channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(FLE.MODID);

	public NetWorkHandler() 
	{
		channel.register(this);
	}
	
	public void updateSlotInTile(TileEntity aTile, int aUpdateID)
	{
		new PacketUpdateTile(new TileEntityUpdateInfo(aTile, aUpdateID)).sendPacket();
	}
	
	public void updateTileEntity(TileEntity aTile)
	{
		new PacketUpdateTile(new TileEntityUpdateInfo(aTile)).sendPacket();
	}
	
	public void updateGUIPress(int buttonID, EntityPlayer player)
	{
		;
	}
	
	@Override
	public FMLEventChannel getChannel() 
	{
		return channel;
	}

	@SubscribeEvent
	public void readPkt(FMLNetworkEvent.ServerCustomPacketEvent evt)
	{
		if(getClass() == NetWorkHandler.class)
			onPacketData(new ByteBufInputStream(evt.packet.payload()), ((NetHandlerPlayServer)evt.handler).playerEntity);
	}
	
	protected void onPacketData(InputStream isRaw, EntityPlayer player)
	{
		try 
		{
			if (isRaw.available() == 0)
				return;
			int id = isRaw.read();
			FleDataInputStream is = new FleDataInputStream(new DataInputStream(isRaw));
			switch(id)
			{
			case tileUpdateID : 
			{
				
			}
			break;
			case guiUpdateID : 
			{
				if(player.openContainer instanceof INetEventListener)
				{
					byte type = is.readByte();
					short contain = is.readShort();
					((INetEventListener) player.openContainer).onReseave(type, contain);
				}
			}
			}
		}
		catch(Throwable e)
		{
			e.printStackTrace();
		}
	}
}