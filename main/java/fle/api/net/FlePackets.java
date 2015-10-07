package fle.api.net;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import fle.FLE;
import fle.api.FleAPI;
import fle.api.material.IAtoms;
import fle.api.material.Matter;
import fle.api.soild.ISolidTanks;
import fle.api.soild.SolidStack;
import fle.api.te.IFluidTanks;
import fle.api.te.IMatterContainer;
import fle.api.te.IObjectInWorld;
import fle.api.te.ITEInWorld;
import fle.api.tech.Technology;
import fle.api.util.FleDataInputStream;
import fle.api.util.FleDataOutputStream;
import fle.api.util.FleLog;
import fle.api.world.BlockPos;

public class FlePackets
{
	public static class CoderMatterUpdate extends FleAbstractPacket<CoderMatterUpdate>
	{
		BlockPos pos;
		Map<IAtoms, Integer> map;
		
		public CoderMatterUpdate()
		{
			map = new HashMap<IAtoms, Integer>();
		}
		public CoderMatterUpdate(IObjectInWorld oiw, IMatterContainer mc)
		{
			pos = oiw.getBlockPos();
			map = mc.getMatterContain();
		}

		@Override
		protected void write(FleDataOutputStream os) throws IOException
		{
			os.writeBlockPos(pos);
			Map<IAtoms, Integer> map = new HashMap();
			for(Entry<IAtoms, Integer> entry : this.map.entrySet())
			{
				if(entry.getKey() == null || entry.getValue() <= 0) continue;
				map.put(entry.getKey(), entry.getValue());
			}
			os.writeInt(map.size());
			for(Entry<IAtoms, Integer> entry : map.entrySet())
			{
				os.writeString(entry.getKey().getChemicalFormulaName());
				os.writeInt(entry.getValue());
			}
		}

		@Override
		protected void read(FleDataInputStream is) throws IOException
		{
			pos = is.readBlockPos();
			int size = is.readInt();
			for(int i = 0; i < size; ++i)
			{
				Matter matter = Matter.getMatterFromName(is.readString());
				int l = is.readInt();
				map.put(matter, l);
			}
		}

		@Override
		public IMessage onMessage(CoderMatterUpdate message, MessageContext ctx)
		{
			if(message.pos.getBlockTile() instanceof IMatterContainer)
			{
				((IMatterContainer) message.pos.getBlockTile()).setMatterContain(message.map);
			}
			return null;
		}
	}
	
	public static class CoderPTUpdate extends FleAbstractPacket<CoderPTUpdate>
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
		protected void write(FleDataOutputStream os) throws IOException
		{
			os.writeString(str);
			os.writeByte(r);
		}
		
		@Override
		protected void read(FleDataInputStream is) throws IOException
		{
			str = is.readString();
			r = is.readByte();			
		}
		
		@Override
		public IMessage onMessage(CoderPTUpdate message, MessageContext ctx)
		{
			Result ret = Result.values()[message.r];
			EntityPlayer player = ctx.getServerHandler().playerEntity;
			if(ret == Result.ALLOW)
				FleAPI.mod.getTechManager().getPlayerInfo(player).setTech(FleAPI.mod.getTechManager().getTechFromId(message.str));
			else if(ret == Result.DENY)
				FleAPI.mod.getTechManager().getPlayerInfo(player).removeTech(FleAPI.mod.getTechManager().getTechFromId(message.str));
			return null;
		}
	}
	
	public static class CoderFWMUpdate extends FleAbstractPacket<CoderFWMUpdate>
	{
		private BlockPos pos;
		private short[] data;
		
		public CoderFWMUpdate() 
		{
			
		}
		public CoderFWMUpdate(BlockPos aPos, short[] aData) 
		{
			pos = aPos;
			data = aData;
		}
		
		@Override
		protected void write(FleDataOutputStream os) throws IOException
		{
			os.writeBlockPos(pos);
			os.writeShortArray(data);
		}
		
		@Override
		protected void read(FleDataInputStream is) throws IOException
		{
			pos = is.readBlockPos();
			data = is.readShortArray();
		}
		
		@Override
		public IMessage onMessage(CoderFWMUpdate message, MessageContext ctx)
		{
			FleAPI.mod.getWorldManager().setDatas(message.pos, message.data, false);
			World world = FLE.fle.getPlatform().getWorldInstance(message.pos.getDim());
			if(world != null)
			{
				world.markBlockRangeForRenderUpdate(message.pos.x, message.pos.y, message.pos.z, message.pos.x, message.pos.y, message.pos.z);
			}
			return null;
		}
	}
	
	public static class CoderGuiUpdate extends FleAbstractPacket<CoderGuiUpdate>
	{
		byte type;
		Object contain;
		public CoderGuiUpdate() 
		{
			
		}
		
		public CoderGuiUpdate(byte aType, int aContain)
		{
			type = aType;
			contain = new Integer((int) aContain);
		}
		
		public CoderGuiUpdate(byte aType, Object aContain)
		{
			type = aType;
			contain = aContain;
		}

		@Override
		protected void write(FleDataOutputStream os) throws IOException
		{
			os.writeByte(type);
			os.write(contain);
		}

		@Override
		protected void read(FleDataInputStream is) throws IOException
		{
			type = is.readByte();
			contain = is.read();			
		}

		@Override
		public IMessage onMessage(CoderGuiUpdate message, MessageContext ctx)
		{
			if(ctx.getServerHandler().playerEntity.openContainer instanceof INetEventListener)
			{
				((INetEventListener) ctx.getServerHandler().playerEntity.openContainer).onReseave(message.type, message.contain);
			}
			return null;
		}
		
	}
	
	public static class CoderInventoryUpdate extends FleAbstractPacket<CoderInventoryUpdate>
	{
		private BlockPos pos;
		private ItemStack[] stacks;
		private int id = -1;
		
		public CoderInventoryUpdate() {}
		public CoderInventoryUpdate(World world, int aX, int aY, int aZ) 
		{
			pos = new BlockPos(world, aX, aY, aZ);
		}
		public CoderInventoryUpdate(BlockPos aPos, int aId) 
		{
			pos = aPos;
			id = aId;
		}
		
		@Override
		protected void write(FleDataOutputStream os) throws IOException
		{
			os.writeBlockPos(pos);
			if(pos.getBlockTile() instanceof IInventory)
			{
				os.writeBoolean(true);
				IInventory inv = (IInventory) pos.getBlockTile();
				os.writeInt(id);
				if(id == -1)
				{
					os.writeInt(inv.getSizeInventory());
					for(int i = 0; i < inv.getSizeInventory(); ++i)
					{
						os.writeItemStack(inv.getStackInSlot(i));
					}
				}
				else
				{
					os.writeItemStack(inv.getStackInSlot(id));
				}
			}
			else
			{
				os.writeBoolean(false);				
			}
		}
		
		@Override
		protected void read(FleDataInputStream is) throws IOException 
		{
			pos = is.readBlockPos();
			if(is.readBoolean())
			{
				id = is.readInt();
				if(id != -1)
				{
					stacks = new ItemStack[]{is.readItemStack()};
				}
				else
				{
					int size = is.readInt();
					stacks = new ItemStack[size];
					for(int i = 0; i < size; ++i)
					{
						stacks[i] = is.readItemStack();
					}
				}
			}
			
		}
		
		@Override
		public IMessage onMessage(CoderInventoryUpdate message,
				MessageContext ctx)
		{
			if(message.pos.getBlockTile() instanceof IInventory)
			{
				IInventory inv = (IInventory) message.pos.getBlockTile();
				if(message.id == -1)
					for(int i = 0; i < message.stacks.length; ++i)
					{
						inv.setInventorySlotContents(i, message.stacks[i]);
					}
				else
					inv.setInventorySlotContents(message.id, message.stacks[0]);
			}
			return null;
		}
	}
	
	public static class CoderTankUpdate extends FleAbstractPacket<CoderTankUpdate>
	{
		private BlockPos pos;
		private FluidStack[] stacks;
		
		public CoderTankUpdate() {}
		public CoderTankUpdate(World world, int aX, int aY, int aZ) 
		{
			pos = new BlockPos(world, aX, aY, aZ);
		}
		public CoderTankUpdate(BlockPos aPos) 
		{
			pos = aPos;
		}
		
		@Override
		protected void write(FleDataOutputStream os) throws IOException
		{
			os.writeBlockPos(pos);
			if(pos.getBlockTile() instanceof IFluidTanks)
			{
				os.writeBoolean(true);
				IFluidTanks inv = (IFluidTanks) pos.getBlockTile();
				os.writeInt(inv.getSizeTank());
				for(int i = 0; i < inv.getSizeTank(); ++i)
				{
					os.writeFluidStack(inv.getFluidStackInTank(i));
				}
			}
			else
			{
				os.writeBoolean(false);				
			}
		}
		
		@Override
		protected void read(FleDataInputStream is) throws IOException 
		{
			pos = is.readBlockPos();
			if(is.readBoolean())
			{
				int size = is.readInt();
				stacks = new FluidStack[size];
				for(int i = 0; i < size; ++i)
				{
					stacks[i] = is.readFluidStack();
				}			
			}
			
		}
		
		@Override
		public IMessage onMessage(CoderTankUpdate message,
				MessageContext ctx)
		{
			if(message.pos.getBlockTile() instanceof IFluidTanks)
			{
				IFluidTanks inv = (IFluidTanks) message.pos.getBlockTile();
				for(int i = 0; i < message.stacks.length; ++i)
				{
					inv.setFluidStackInTank(i, message.stacks[i]);
				}
			}
			return null;
		}
	}
	
	public static class CoderSolidTankUpdate extends FleAbstractPacket<CoderSolidTankUpdate>
	{
		private BlockPos pos;
		private SolidStack[] stacks;
		
		public CoderSolidTankUpdate() {}
		public CoderSolidTankUpdate(World world, int aX, int aY, int aZ) 
		{
			pos = new BlockPos(world, aX, aY, aZ);
		}
		public CoderSolidTankUpdate(BlockPos aPos) 
		{
			pos = aPos;
		}
		
		@Override
		protected void write(FleDataOutputStream os) throws IOException
		{
			os.writeBlockPos(pos);
			if(pos.getBlockTile() instanceof ISolidTanks)
			{
				os.writeBoolean(true);
				ISolidTanks inv = (ISolidTanks) pos.getBlockTile();
				os.writeInt(inv.getSizeSolidTank());
				for(int i = 0; i < inv.getSizeSolidTank(); ++i)
				{
					os.writeSolidStack(inv.getSolidStackInTank(i));
				}
			}
			else
			{
				os.writeBoolean(false);				
			}
		}
		
		@Override
		protected void read(FleDataInputStream is) throws IOException 
		{
			pos = is.readBlockPos();
			if(is.readBoolean())
			{
				int size = is.readInt();
				stacks = new SolidStack[size];
				for(int i = 0; i < size; ++i)
				{
					stacks[i] = is.readSolidStack();
				}			
			}
			
		}
		
		@Override
		public IMessage onMessage(CoderSolidTankUpdate message,
				MessageContext ctx)
		{
			if(message.pos.getBlockTile() instanceof ISolidTanks)
			{
				ISolidTanks inv = (ISolidTanks) message.pos.getBlockTile();
				for(int i = 0; i < message.stacks.length; ++i)
				{
					inv.setSolidStackInTank(i, message.stacks[i]);
				}
			}
			return null;
		}
	}
	
	public static class CoderTileUpdate extends FleAbstractPacket<CoderTileUpdate>
	{
		private BlockPos pos;
		private byte type;
		private Object contain;
		private String tile;
		
		public CoderTileUpdate() {}
		
		public CoderTileUpdate(ITEInWorld obj, INetEventHandler tile) 
		{
			this(obj, (byte) -1, tile.onEmmitNBT());
		}
		public CoderTileUpdate(ITEInWorld obj, short contain) 
		{
			this(obj, (byte) -1, contain);
		}
		public CoderTileUpdate(ITEInWorld obj, byte aType, Object aContain) 
		{
			pos = obj.getBlockPos();
			type = aType;
			contain = aContain;
			tile = obj.getTileEntity().getClass().toString();
		}
		public CoderTileUpdate(World world, int aX, short aY, int aZ, byte aType, Object aContain) 
		{
			pos = new BlockPos(world, aX, aY, aZ);
			type = aType;
			contain = aContain;
		}

		@Override
		protected void write(FleDataOutputStream os) throws IOException
		{
			os.writeBlockPos(pos);
			os.writeByte(type);
			os.write(contain);
		}

		@Override
		protected void read(FleDataInputStream is) throws IOException
		{
			pos = is.readBlockPos();
			type = is.readByte();
			contain = is.read();
		}

		@Override
		public IMessage onMessage(CoderTileUpdate message, MessageContext ctx)
		{
			if(message.pos.getBlockTile() instanceof INetEventListener)
			{
				((INetEventListener) message.pos.getBlockTile()).onReseave(message.type, message.contain);
			}
			if(message.pos.getBlockTile() instanceof INetEventHandler)
			{
				((INetEventHandler) message.pos.getBlockTile()).onReseaveNBT((NBTTagCompound) message.contain);
			}
			FleAPI.mod.getPlatform().getWorldInstance(message.pos.getDim()).markBlockForUpdate(message.pos.x, message.pos.y, message.pos.z);
			return null;
		}
	}
}