package fla.core.network;

import io.netty.buffer.Unpooled;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import fla.api.tech.Technology;
import fla.api.world.BlockPos;
import fla.core.Fla;

public abstract class FlaPacket
{
	static final int guiPacketType = 1;
	static final int keyPacketType = 2;
	static final int tileUpdatePacketType = 3;
	static final int heatUpdatePacketType = 4;
	static final int techUpdatePacketType = 5;
	
	static FMLEventChannel channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(Fla.MODID);
	private FMLProxyPacket pkt;
	private EntityPlayerMP player;
	
	protected FlaPacket(EntityPlayerMP player, Object...objects) 
	{
		this.player = player;
		pkt = new FMLProxyPacket(Unpooled.wrappedBuffer(init(objects)), Fla.MODID);
	}
	protected FlaPacket(Object...objects) 
	{
		pkt = new FMLProxyPacket(Unpooled.wrappedBuffer(init(objects)), Fla.MODID);
	}

	private byte[] init(Object[] objects)
	{
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		DataOutputStream os = new DataOutputStream(buffer);
		setup(os, objects);
		try
		{
			os.close();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return buffer.toByteArray();
	}

	protected void sendPacket()
	{
		if (Fla.fla.p.get().isSimulating())
			channel.sendToAll(pkt);
		else
			channel.sendToServer(pkt);
	}

	protected void sendPacket(EntityPlayerMP player)
	{
		channel.sendTo(pkt, player);
	}
	
	protected abstract void setup(DataOutputStream os, Object[] objects);

	public static class FlaTileUpdatePacket extends FlaPacket
	{
		private static Object[] toObjects(int i, ItemStack itemstack)
		{
			if(itemstack == null)
			{
				return new Object[]{i, false};
			}
			else
			{
				return new Object[]{i, true, itemstack.getItem(), itemstack.stackSize, itemstack.getItemDamage(), itemstack.stackTagCompound};
			}
		}
		
		public FlaTileUpdatePacket(TileEntity tile, int updateSlot, ItemStack target)
		{
			this(tile, (byte) 0, toObjects(updateSlot, target));
		}
		public FlaTileUpdatePacket(TileEntity tile, NBTTagCompound nbt)
		{
			this(tile, (byte) 1, nbt);
		}
		private FlaTileUpdatePacket(TileEntity tile, byte type, Object...objects)
		{
			super(tile.getWorldObj().provider.dimensionId, tile.xCoord, tile.yCoord, tile.zCoord, type, objects);
		}
		
		@Override
		protected void setup(DataOutputStream os, Object[] objects) 
		{
			int dim = (Integer) objects[0];
			int x = (Integer) objects[1];
			int y = (Integer) objects[2];
			int z = (Integer) objects[3];
			byte type = (Byte) objects[4];
			try
			{
				os.write(tileUpdatePacketType);
				os.writeInt(dim);
				os.writeInt(x);
				os.writeInt(y);
				os.writeInt(z);
				os.writeByte(type);
				switch(type)
				{
				case (byte) 0 : 
				{
					Object[] objs = (Object[]) objects[5];
					os.writeByte((Integer) objs[0]);
					boolean b = (Boolean) objs[1];
					os.writeBoolean(b);
					if(b)
					{
						Item item = (Item) objs[2];
						os.writeUTF(Item.itemRegistry.getNameForObject(item));
						os.writeInt((Integer) objs[3]);
						os.writeInt((Integer) objs[4]);
						if(objs[5] == null)
						{
							os.writeBoolean(false);
						}
						else
						{
							os.writeBoolean(true);
							CompressedStreamTools.writeCompressed((NBTTagCompound) objs[5], os);
						}
					}
				}
				break;
				case 1 :
				{
					Object[] objs = (Object[]) objects[5];
					NBTTagCompound nbt = (NBTTagCompound) objs[0];
					CompressedStreamTools.writeCompressed((NBTTagCompound) nbt, os);
				}
				}
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public static class FlaHeatUpdatePacket extends FlaPacket
	{
		public FlaHeatUpdatePacket(int dimId, BlockPos pos, ForgeDirection dir, int size) 
		{
			super(dimId, pos.x, pos.y, pos.z, (byte) dir.ordinal(), size);
		}

		@Override
		protected void setup(DataOutputStream os, Object[] objects) 
		{
			try 
			{
				os.write(heatUpdatePacketType);
				os.writeInt((Integer) objects[0]);
				os.writeInt((Integer) objects[1]);
				os.writeInt((Integer) objects[2]);
				os.writeInt((Integer) objects[3]);
				os.writeByte((Byte) objects[4]);
				os.writeInt((Integer) objects[5]);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	public static class FlaGuiPacket extends FlaPacket
	{
		public FlaGuiPacket(int x, int y, int z, byte type, short contain) 
		{
			super(x, y, z, type, contain);
		}

		@Override
		protected void setup(DataOutputStream os, Object[] objects) 
		{
			try 
			{
				os.write(guiPacketType);
				os.writeInt((Integer) objects[0]);
				os.writeInt((Integer) objects[1]);
				os.writeInt((Integer) objects[2]);
				os.writeByte((Byte) objects[3]);
				os.writeShort((Short) objects[4]);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	public static class FlaKeyPacket extends FlaPacket
	{
		public FlaKeyPacket(int keyType) 
		{
			super(keyType);
		}

		@Override
		protected void setup(DataOutputStream os, Object[] objects) 
		{
			try 
			{
				os.write(keyPacketType);
				os.writeInt((Integer) objects[0]);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	public static class FlaTechPacket extends FlaPacket
	{
		public FlaTechPacket(Technology tech, byte type) 
		{
			super(tech.getName(), type);
		}

		@Override
		protected void setup(DataOutputStream os, Object[] objects) 
		{
			try 
			{
				os.write(techUpdatePacketType);
				os.writeUTF((String) objects[0]);
				os.writeByte((Byte) objects[1]);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
}
