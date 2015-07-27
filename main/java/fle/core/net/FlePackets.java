package fle.core.net;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import fle.api.net.FleAbstractPacket;
import fle.api.net.INetEventHandler;
import fle.api.net.PacketInfo;
import fle.api.util.FleDataOutputStream;

public class FlePackets
{	
	public static class PacketUpdateTile extends FleAbstractPacket<TileEntityUpdateInfo>
	{
		protected PacketUpdateTile(TileEntityUpdateInfo aInfo) 
		{
			super(aInfo);
		}
		protected PacketUpdateTile(EntityPlayerMP aPlayer,
				TileEntityUpdateInfo aInfo)
		{
			super(aPlayer, aInfo);
		}
		
		@Override
		public void createPacket(FleDataOutputStream aStream,
				TileEntityUpdateInfo aInfo) 
		{
			try 
			{
				aStream.writeInt(aInfo.getPacketID());
				aStream.writeTileEntity(aInfo.tile);
				if(aInfo.tile instanceof INetEventHandler)
				{
					aStream.writeNBT(((INetEventHandler) aInfo.tile).onEmmitNBT());
				}
				if(aInfo.updateSlotID != -2 && aInfo.tile instanceof IInventory)
				{
					IInventory tInv = (IInventory) aInfo.tile;
					if(aInfo.updateSlotID == -1)
					{
						aStream.writeByte((byte) -1);
						aStream.writeByte((byte) tInv.getSizeInventory());
						for(int i = 0; i < tInv.getSizeInventory(); ++i)
						{
							ItemStack stack = tInv.getStackInSlot(i);
							aStream.writeItemStack(stack);
						}
					}
					else
					{
						aStream.writeByte((byte) aInfo.updateSlotID);
						aStream.writeItemStack(tInv.getStackInSlot(aInfo.updateSlotID));
					}
				}
				else
				{
					aStream.writeByte((byte) -2); 
				}
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	public static class PacketGuiUpdate extends FleAbstractPacket<NetEventListenerInfo>
	{
		public PacketGuiUpdate(NetEventListenerInfo aInfo) 
		{
			super(aInfo);
		}
		public PacketGuiUpdate(EntityPlayerMP aPlayer,
				NetEventListenerInfo aInfo) 
		{
			super(aPlayer, aInfo);
		}
		
		@Override
		public void createPacket(FleDataOutputStream aStream,
				NetEventListenerInfo aInfo) 
		{
			try
			{
				aStream.writeInt(aInfo.getPacketID());
				aStream.writeByte(aInfo.type);
				aStream.writeShort(aInfo.contain);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	public static class TileEntityUpdateInfo implements PacketInfo
	{
		private TileEntity tile;
		private int updateSlotID = -2;

		public TileEntityUpdateInfo(TileEntity aTile, boolean allSlotUpdate) 
		{
			tile = aTile;
			updateSlotID = -1;
		}
		public TileEntityUpdateInfo(TileEntity aTile, int aSlotID) 
		{
			tile = aTile;
			updateSlotID = aSlotID;
		}
		public TileEntityUpdateInfo(TileEntity aTile) 
		{
			tile = aTile;
		}
		
		@Override
		public int getPacketID() 
		{
			return NetWorkHandler.tileUpdateID;
		}
	}

	public static class NetEventListenerInfo implements PacketInfo
	{
		private byte type;
		private short contain;
		
		public NetEventListenerInfo(byte aType, short aTag) 
		{
			type = aType;
			contain = aTag;
		}
		
		@Override
		public int getPacketID() 
		{
			return NetWorkHandler.guiUpdateID;
		}
	}
}