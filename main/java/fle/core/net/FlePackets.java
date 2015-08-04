package fle.core.net;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.relauncher.Side;
import fle.FLE;
import fle.api.net.IFlePacketHandler;
import fle.api.net.INetEventHandler;
import fle.api.net.INetEventListener;
import fle.api.te.IObjectInWorld;
import fle.api.tech.Technology;
import fle.api.util.FleDataInputStream;
import fle.api.util.FleDataOutputStream;
import fle.api.world.BlockPos;

public class FlePackets
{
	public static class CoderKeyType implements IFlePacketHandler
	{
		int keys;
		
		public CoderKeyType() 
		{
			
		}
		public CoderKeyType(int key) 
		{
			keys = key;
		}

		@Override
		public int getCoderID()
		{
			return NetWorkHandler.keyID;
		}

		@Override
		public void createPacket(FleDataOutputStream aStream) throws IOException 
		{
			aStream.writeInt(keys);
		}

		@Override
		public void decode(FleDataInputStream aStream) throws IOException
		{
			keys = aStream.readInt();
		}

		@Override
		public void process(Side aSide, EntityPlayer player) 
		{
			if(aSide == Side.SERVER)
			{
				FLE.fle.getKeyboard().processKeyUpdate(player, keys);
			}
		}
	}
	
	public static class CoderPTUpdate implements IFlePacketHandler
	{
		String str;
		byte r;
		public CoderPTUpdate() 
		{
			
		}
		public CoderPTUpdate(Technology tech, Result result) 
		{
			str = tech.getName();
			r = (byte) result.ordinal();
		}
		
		@Override
		public int getCoderID()
		{
			return NetWorkHandler.ptID;
		}

		@Override
		public void createPacket(FleDataOutputStream aStream)
				throws IOException
		{
			aStream.writeString(str);
			aStream.writeByte(r);
		}

		@Override
		public void decode(FleDataInputStream aStream) throws IOException 
		{
			str = aStream.readString();
			r = aStream.readByte();
		}

		@Override
		public void process(Side aSide, EntityPlayer player) 
		{
			Result ret = Result.values()[r];
			if(ret == Result.ALLOW)
				FLE.fle.getTechManager().getPlayerInfo(player).setTech(FLE.fle.getTechManager().getTechFromId(str));
			else if(ret == Result.DENY)
				FLE.fle.getTechManager().getPlayerInfo(player).removeTech(FLE.fle.getTechManager().getTechFromId(str));
		}
		
	}
	
	public static class CoderFWMUpdate implements IFlePacketHandler
	{
		private int dimID;
		private int x;
		private short y;
		private int z;
		private int type;
		private int data;
		
		public CoderFWMUpdate() 
		{
			
		}
		public CoderFWMUpdate(int aDimID, BlockPos aPos, int aType, int aData) 
		{
			dimID = aDimID;
			x = aPos.x;
			y = aPos.y;
			z = aPos.z;
			type = aType;
			data = aData;
		}

		@Override
		public int getCoderID() 
		{
			return NetWorkHandler.fwmID;
		}

		@Override
		public void createPacket(FleDataOutputStream aStream)
				throws IOException 
		{
			aStream.writeInt(dimID);
			aStream.writeBlockPos(x, y, z);
			aStream.writeInt(type);
			aStream.writeShort((short) data);
		}

		@Override
		public void decode(FleDataInputStream aStream) throws IOException 
		{
			dimID = aStream.readInt();
			BlockPos tPos = aStream.readBlockPos();
			x = tPos.x;
			y = tPos.y;
			z = tPos.z;
			type = aStream.readInt();
			data = aStream.readShort();
		}

		@Override
		public void process(Side aSide, EntityPlayer player) 
		{
			if(aSide == Side.CLIENT)
			{
				FLE.fle.getWorldManager().setData(new BlockPos(FLE.fle.getPlatform().getWorldInstance(dimID), x, y, z), type, data);
			}
		}
	}
	
	public static class CoderGuiUpdate implements IFlePacketHandler
	{
		byte type;
		Object contain;
		public CoderGuiUpdate() 
		{
			
		}
		
		public CoderGuiUpdate(byte aType, int aContain)
		{
			type = aType;
			contain = new Short((short) aContain);
		}
		
		public CoderGuiUpdate(byte aType, Object aContain)
		{
			type = aType;
			contain = aContain;
		}

		@Override
		public int getCoderID() 
		{
			return NetWorkHandler.guiUpdateID;
		}

		@Override
		public void createPacket(FleDataOutputStream aStream)
				throws IOException 
				{
			aStream.writeByte(type);
			aStream.write(contain);
		}

		@Override
		public void decode(FleDataInputStream aStream) throws IOException 
		{
			type = aStream.readByte();
			contain = aStream.read();
		}

		@Override
		public void process(Side aSide, EntityPlayer player) 
		{
			if(aSide == Side.SERVER)
			{
				if(player.openContainer instanceof INetEventListener)
				{
					((INetEventListener) player.openContainer).onReseave(type, contain);
				}
			}
		}
		
	}
	
	public static class CoderInventorUpdate implements IFlePacketHandler
	{
		public static CoderTileUpdate instance = new CoderTileUpdate();
		private int dimID;
		private int x;
		private short y;
		private int z;
		
		public CoderInventorUpdate() {}
		public CoderInventorUpdate(World world, int aX, short aY, int aZ) 
		{
			dimID = world.provider.dimensionId;
			x = aX;
			y = aY;
			z = aZ;
		}

		@Override
		public int getCoderID() 
		{
			return NetWorkHandler.inventoryUpdateID;
		}

		@Override
		public void createPacket(FleDataOutputStream aStream)
				throws IOException 
		{
			aStream.writeInt(dimID);
			aStream.writeInt(x);
			aStream.writeShort(y);
			aStream.writeInt(z);
			if(FLE.fle.getPlatform().getWorldInstance(dimID).getTileEntity(x, y, z) instanceof IInventory)
			{
				aStream.writeBoolean(true);
				IInventory inv = (IInventory) FLE.fle.getPlatform().getWorldInstance(dimID).getTileEntity(x, y, z);
				aStream.writeInt(inv.getSizeInventory());
				for(int i = 0; i < inv.getSizeInventory(); ++i)
				{
					aStream.writeItemStack(inv.getStackInSlot(i));
				}
			}
			else
			{
				aStream.writeBoolean(false);				
			}
		}
		
		private ItemStack[] stacks;

		@Override
		public void decode(FleDataInputStream aStream) throws IOException 
		{
			dimID = aStream.readInt();
			x = aStream.readInt();
			y = aStream.readShort();
			z = aStream.readInt();
			if(aStream.readBoolean())
			{
				int size = aStream.readInt();
				stacks = new ItemStack[size];
				for(int i = 0; i < size; ++i)
				{
					stacks[i] = aStream.readItemStack();
				}			
			}
		}

		@Override
		public void process(Side aSide, EntityPlayer player) 
		{
			if(FLE.fle.getPlatform().getWorldInstance(dimID).getTileEntity(x, y, z) instanceof IInventory)
			{
				IInventory inv = (IInventory) FLE.fle.getPlatform().getWorldInstance(dimID).getTileEntity(x, y, z);
				for(int i = 0; i < stacks.length; ++i)
				{
					inv.setInventorySlotContents(i, stacks[i]);
				}
			}
		}
	}
	
	public static class CoderTileUpdate implements IFlePacketHandler
	{
		public static CoderTileUpdate instance = new CoderTileUpdate();
		
		private int dimID;
		private int x;
		private short y;
		private int z;
		private byte type;
		private Object contain;
		
		public CoderTileUpdate() {}
		
		public CoderTileUpdate(World world, IObjectInWorld obj, INetEventHandler tile) 
		{
			this(world, obj, (byte) -1, tile.onEmmitNBT());
		}
		public CoderTileUpdate(World world, IObjectInWorld obj, short contain) 
		{
			this(world, obj, (byte) -1, contain);
		}
		public CoderTileUpdate(World world, IObjectInWorld obj, byte aType, Object aContain) 
		{
			this(world, obj.getBlockPos().x, obj.getBlockPos().y, obj.getBlockPos().z, aType, aContain);
		}
		public CoderTileUpdate(World world, int aX, short aY, int aZ, byte aType, Object aContain) 
		{
			dimID = world.provider.dimensionId;
			x = aX;
			y = aY;
			z = aZ;
			type = aType;
			contain = aContain;
		}

		@Override
		public int getCoderID() 
		{
			return NetWorkHandler.tileUpdateID;
		}

		@Override
		public void createPacket(FleDataOutputStream aStream) throws IOException 
		{
			try
			{
				aStream.writeInt(dimID);
				aStream.writeInt(x);
				aStream.writeShort(y);
				aStream.writeInt(z);
				aStream.writeByte(type);
				aStream.write(contain);
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}

		@Override
		public void decode(FleDataInputStream aStream) throws IOException 
		{
			dimID = aStream.readInt();
			x = aStream.readInt();
			y = aStream.readShort();
			z = aStream.readInt();
			type = aStream.readByte();
			contain = aStream.read();
		}

		@Override
		public void process(Side aSide, EntityPlayer player) 
		{
			if(aSide == Side.CLIENT)
			{
				BlockPos pos = new BlockPos(FLE.fle.getPlatform().getWorldInstance(dimID), x, y, z);
				if(pos.getBlockTile() instanceof INetEventListener)
				{
					((INetEventListener) pos.getBlockTile()).onReseave(type, contain);
				}
				if(pos.getBlockTile() instanceof INetEventHandler)
				{
					((INetEventHandler) pos.getBlockTile()).onReseaveNBT((NBTTagCompound) contain);
				}
				FLE.fle.getPlatform().getWorldInstance(dimID).markBlockForUpdate(x, y, z);
			}
		}
	}
}