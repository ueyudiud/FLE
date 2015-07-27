package fle.core.net;

import io.netty.buffer.ByteBufInputStream;

import java.io.DataInputStream;
import java.io.InputStream;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import fle.api.net.INetEventHandler;
import fle.api.net.INetEventListener;
import fle.api.util.FleDataInputStream;
import fle.core.net.FlePackets.NetEventListenerInfo;
import fle.core.net.FlePackets.PacketGuiUpdate;
import fle.core.net.FlePackets.PacketUpdateTile;
import fle.core.net.FlePackets.TileEntityUpdateInfo;

public class NetWorkClient extends NetWorkHandler
{
	public void updateSlotInTile(TileEntity aTile, int aUpdateID)
	{
		;
	}
	
	public void updateTileEntity(TileEntity tile)
	{
		;
	}
	
	public void updateGUIPress(short buttonID, EntityPlayer player)
	{
		new PacketGuiUpdate(new NetEventListenerInfo((byte) 0, buttonID)).sendPacket();
		if(player.openContainer instanceof INetEventListener)
		{
			((INetEventListener) player.openContainer).onReseave((byte) 0, buttonID);
		}
	}
	
	@SubscribeEvent
	public void readPkt(FMLNetworkEvent.ClientCustomPacketEvent evt)
	{
		this.onPacketData(new ByteBufInputStream(evt.packet.payload()), Minecraft.getMinecraft().thePlayer);
	}
	
	@Override
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
				TileEntity tile = is.readTileEntity();
				if(tile instanceof INetEventHandler)
					((INetEventHandler) tile).onReseaveNBT(is.readNBT());
				byte slotUpdateType = is.readByte();
				if(slotUpdateType != -2)
				{
					IInventory inv = (IInventory) tile;
					if(slotUpdateType == -1)
					{
						int updateSize = is.readByte();
						for(int i = 0; i < updateSize; ++i)
						{
							inv.setInventorySlotContents(i, is.readItemStack());
						}
					}
					else
					{
						inv.setInventorySlotContents(slotUpdateType, is.readItemStack());
					}
				}
			}
			break;
			case guiUpdateID : 
			{
				
			}
			break;
			}
		}
		catch(Throwable e)
		{
			e.printStackTrace();
		}
	}
}