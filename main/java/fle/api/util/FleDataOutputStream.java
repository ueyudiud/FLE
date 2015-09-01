package fle.api.util;

import fle.api.gui.GuiCondition;
import fle.api.world.BlockPos;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.registry.GameData;

public class FleDataOutputStream 
{
	private ByteBuf stream;
	
	public FleDataOutputStream(ByteBuf aStream)
	{
		stream = aStream;
	}

	public void writeFloat(float f) throws IOException
	{
		stream.writeFloat(f);
	}

	public void writeDouble(double d) throws IOException
	{
		stream.writeDouble(d);
	}
	
	public void writeBoolean(boolean b) throws IOException
	{
		stream.writeBoolean(b);
	}
	
	public void writeByte(byte b) throws IOException
	{
		stream.writeByte(b);
	}

	public void writeShort(short s) throws IOException
	{
		stream.writeShort(s);
	}

	public void writeInt(int i) throws IOException
	{
		stream.writeInt(i);
	}

	public void writeLong(long l) throws IOException
	{
		stream.writeLong(l);
	}

	public void writeString(String s) throws IOException
	{
		ByteBufUtils.writeUTF8String(stream, s);
	}

	public void writeShortArray(short[] data) throws IOException
	{
		writeInt(data.length);
		for(int i = 0; i < data.length; ++i)
		{
			writeShort(data[i]);
		}
	}

	public void writeIntArray(int[] data) throws IOException
	{
		writeInt(data.length);
		for(int i = 0; i < data.length; ++i)
		{
			writeInt(data[i]);
		}
	}

	public void writeNBT(NBTTagCompound nbt) throws IOException
	{
		if(nbt != null)
		{
			stream.writeBoolean(true);
			stream.writeBytes(CompressedStreamTools.compress(nbt));
		}
		else
		{
			stream.writeBoolean(false);
		}
	}
	
	public void writeItem(Item item) throws IOException
	{
		writeString(GameData.getItemRegistry().getNameForObject(item));
	}
	
	public void writeBlock(Block block) throws IOException
	{
		writeString(GameData.getBlockRegistry().getNameForObject(block));
	}

	public void writeItemStack(ItemStack stack) throws IOException
	{
		if(stack != null)
		{
			stream.writeBoolean(true);
			writeItem(stack.getItem());
			stream.writeShort(stack.stackSize);
			stream.writeShort(stack.getItemDamage());
			writeNBT(stack.stackTagCompound);
		}
		else
		{
			stream.writeBoolean(false);
		}
	}

	public void writeFluidStack(FluidStack stack) throws IOException
	{
		if(stack != null && stack.getFluid() != null)
		{
			stream.writeBoolean(true);
			stream.writeInt(stack.getFluidID());
			stream.writeInt(stack.amount);
			writeNBT(stack.tag);
		}
		else
		{
			stream.writeBoolean(false);
		}
	}

	public void writeFluidTank(IFluidTank tank) throws IOException
	{
		stream.writeInt(tank.getCapacity());
		writeFluidStack(tank.getFluid());
	}
	
	public void writeUUID(UUID uuid) throws IOException
	{
		stream.writeLong(uuid.getMostSignificantBits());
		stream.writeLong(uuid.getLeastSignificantBits());
	}

	public void writeWorld(World world) throws IOException
	{
		stream.writeInt(world.provider.dimensionId);
	}

	public void writeBlockPos(int dimID, int x, int y, int z) throws IOException
	{
		stream.writeInt(dimID);
		stream.writeInt(x);
		stream.writeShort(y);
		stream.writeInt(z);
	}

	public void writeBlockPos(BlockPos pos) throws IOException
	{
		stream.writeInt(pos.getDim());
		stream.writeInt(pos.x);
		stream.writeShort(pos.y);
		stream.writeInt(pos.z);
	}

	public void writeTileEntity(TileEntity tile) throws IOException 
	{
		if(tile == null)
		{
			stream.writeBoolean(false);
		}
		else
		{
			stream.writeBoolean(true);
			writeWorld(tile.getWorldObj());
			stream.writeInt(tile.xCoord);
			stream.writeShort(tile.yCoord);
			stream.writeInt(tile.zCoord);
		}
	}

	public void write(Object contain) throws IOException
	{
		write(contain, true);
	}

	public void write(Object contain, boolean writeType) throws IOException
	{
		if(contain == null && writeType) writeByte(Byte.MAX_VALUE); 
		if(contain instanceof Boolean)
		{
			if(writeType) writeByte((byte) 0);
			writeBoolean(new Boolean((Boolean) contain).booleanValue());
		}
		else if(contain instanceof Byte)
		{
			if(writeType) writeByte((byte) 1);
			writeByte(new Byte((Byte) contain).byteValue());
		}
		else if(contain instanceof Short)
		{
			if(writeType) writeByte((byte) 2);
			writeShort(new Short((Short) contain).shortValue());
		}
		else if(contain instanceof Integer)
		{
			if(writeType) writeByte((byte) 3);
			writeInt(new Integer((Integer) contain).intValue());
		}
		else if(contain instanceof Long)
		{
			if(writeType) writeByte((byte) 4);
			writeLong(new Long((Long) contain).longValue());
		}
		else if(contain instanceof Float)
		{
			if(writeType) writeByte((byte) 5);
			writeFloat(new Float((Float) contain).floatValue());
		}
		else if(contain instanceof Double)
		{
			if(writeType) writeByte((byte) 6);
			writeDouble(new Double((Double) contain).doubleValue());
		}
		else if(contain instanceof String)
		{
			if(writeType) writeByte((byte) 7);
			writeString((String) contain);
		}
		else if(contain instanceof NBTTagCompound)
		{
			if(writeType) writeByte((byte) 8);
			writeNBT((NBTTagCompound) contain);
		}
		else if(contain instanceof ItemStack)
		{
			if(writeType) writeByte((byte) 9);
			writeItemStack((ItemStack) contain);
		}
		else if(contain instanceof Item)
		{
			if(writeType) writeByte((byte) 10);
			writeItem((Item) contain);
		}
		else if(contain instanceof Block)
		{
			if(writeType) writeByte((byte) 11);
			writeBlock((Block) contain);
		}
		else if(contain instanceof World)
		{
			if(writeType) writeByte((byte) 12);
			writeWorld((World) contain);
		}
		else if(contain instanceof BiomeGenBase)
		{
			if(writeType) writeByte((byte) 13);
			writeInt(((BiomeGenBase) contain).biomeID);
		}
		else if(contain instanceof GuiCondition)
		{
			if(writeType) writeByte((byte) 14);
			writeString(((GuiCondition) contain).getName());
		}
	}
	
	public void close() throws IOException
	{
		ByteBufOutputStream tStream = new ByteBufOutputStream(stream);
		tStream.close();
		this.stream = tStream.buffer();
	}
}