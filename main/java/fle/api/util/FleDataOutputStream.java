package fle.api.util;

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

import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.registry.GameData;
import fle.api.gui.GuiCondition;
import fle.api.soild.SolidRegistry;
import fle.api.soild.SolidStack;
import fle.api.world.BlockPos;

public class FleDataOutputStream 
{
	private ByteArrayDataOutput stream;

	public FleDataOutputStream(ByteArrayDataOutput aStream)
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
		stream.writeUTF(s);
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
	
	private class A extends OutputStream
	{
		@Override
		public void write(int b) throws IOException
		{
			stream.write(b);
		}	
	}

	public void writeNBT(NBTTagCompound nbt) throws IOException
	{
		if(nbt != null)
		{
			stream.writeBoolean(true);
			
			byte[] buf = CompressedStreamTools.compress(nbt);
			writeBytes(buf);
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

	public void writeSolidStack(SolidStack aStack) throws IOException
	{
		if(aStack != null)
		{
			stream.writeBoolean(true);
			writeString(SolidRegistry.getSolidName(aStack.getObj()));
			writeInt(aStack.getSize());
			writeNBT(aStack.getTagCompound());
		}
		else 
		{
			stream.writeBoolean(false);
		}
	}

	public void writeBytes(byte[] bs) throws IOException
	{
		stream.writeShort(bs.length);
		stream.write(bs);
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
		else if(contain instanceof int[])
		{
			if(writeType) writeByte((byte) 15);
			writeIntArray((int[]) contain);
		}
		else if(contain instanceof Enum<?>)
		{
			if(writeType) writeByte((byte) 3);
			writeInt(((Enum) contain).ordinal());
		}
	}
	
	public ByteArrayDataOutput getBuf()
	{
		return stream;
	}
	
	public void close() throws IOException
	{
		OutputStream tStream = new OutputStream()
		{			
			@Override
			public void write(int b) throws IOException 
			{
				stream.write(b);
			}
		};
		tStream.close();
	}
}