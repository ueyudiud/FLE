package fle.api.util;

import fle.FLE;
import fle.api.FleAPI;
import fle.api.gui.GuiCondition;
import fle.api.soild.Solid;
import fle.api.soild.SolidRegistry;
import fle.api.soild.SolidStack;
import fle.api.world.BlockPos;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.registry.GameData;

public class FleDataInputStream 
{
	private ByteArrayDataInput stream;
	
	public FleDataInputStream(ByteArrayDataInput aStream)
	{
		stream = aStream;
	}

	public float readFloat() throws IOException
	{
		return stream.readFloat();
	}

	public double readDouble() throws IOException
	{
		return stream.readDouble();
	}
	
	public boolean readBoolean() throws IOException
	{
		return stream.readBoolean();
	}
	
	public byte readByte() throws IOException
	{
		return stream.readByte();
	}

	public short readShort() throws IOException
	{
		return stream.readShort();
	}

	public int readInt() throws IOException
	{
		return stream.readInt();
	}

	public long readLong() throws IOException
	{
		return stream.readLong();
	}

	public String readString() throws IOException
	{
		return stream.readUTF();
	}

	public short[] readShortArray() throws IOException
	{
		int length = readInt();
		short[] t = new short[length];
		for(int i = 0; i < length; ++i)
			t[i] = readShort();
		return t;
	}

	public int[] readIntArray() throws IOException
	{
		int length = readInt();
		int[] t = new int[length];
		for(int i = 0; i < length; ++i)
			t[i] = readInt();
		return t;
	}

	public NBTTagCompound readNBT() throws IOException
	{
		if(stream.readBoolean())
		{
			InputStream is = new InputStream()
			{				
				@Override
				public int read() throws IOException
				{
					return stream.readInt();
				}
			};
			return CompressedStreamTools.readCompressed(is);
		}
		else
		{
			return new NBTTagCompound();
		}
	}
	
	public Item readItem() throws IOException
	{
		return GameData.getItemRegistry().getObject(readString());
	}
	
	public Block readBlock() throws IOException
	{
		return GameData.getBlockRegistry().getObject(readString());
	}

	public ItemStack readItemStack() throws IOException
	{
		if(stream.readBoolean())
		{
			Item item = readItem();
			int size = readShort();
			int meta = readShort();
			NBTTagCompound nbt = readNBT();
			ItemStack ret = new ItemStack(item, size, meta);
			if(nbt != null) ret.setTagCompound(nbt);
			return ret;
		}
		else return null;
	}

	public SolidStack readSolidStack() throws IOException
	{
		if(stream.readBoolean())
		{
			Solid solid = SolidRegistry.getSolidFromName(readString());
			int size = readInt();
			NBTTagCompound nbt = readNBT();
			return new SolidStack(solid, size, nbt);
		}
		else return null;
	}

	public FluidStack readFluidStack() throws IOException
	{
		if(stream.readBoolean())
		{
			Fluid fluid = FluidRegistry.getFluid(stream.readInt());
			int amount = stream.readInt();
			NBTTagCompound nbt = readNBT();
			FluidStack ret = new FluidStack(fluid, amount, nbt);
			return ret;
		}
		else return null;
	}

	public FluidTank readFluidTank() throws IOException
	{
		int c = stream.readInt();
		FluidStack stack = readFluidStack();
		return stack == null ? new FluidTank(c) : new FluidTank(stack, c);
	}
	
	public UUID readUUID() throws IOException
	{
		long mostSignficant = stream.readLong();
		long leastSignficant = stream.readLong();
		return new UUID(mostSignficant, leastSignficant);
	}

	public World readWorld() throws IOException
	{
		return FLE.fle.getPlatform().getWorldInstance(stream.readInt());
	}

	public BlockPos readBlockPos() throws IOException
	{
		int dimID = readInt();
		int x = readInt();
		int y = readShort();
		int z = readInt();
		return new BlockPos(FleAPI.mod.getPlatform().getWorldInstance(dimID), x, y, z);
	}

	public byte[] readBytes() throws IOException
	{
		int length = readShort();
		byte[] ba = new byte[length];
		stream.readFully(ba);
		return ba;
	}
	
	public Object read() throws IOException
	{
		byte type = stream.readByte();
		switch(type)
		{
		case Byte.MAX_VALUE : return null;
		case 0 : return new Boolean(readBoolean());
		case 1 : return new Byte(readByte());
		case 2 : return new Short(readShort());
		case 3 : return new Integer(readInt());
		case 4 : return new Long(readLong());
		case 5 : return new Float(readFloat());
		case 6 : return new Double(readDouble());
		case 7 : return readString();
		case 8 : return readNBT();
		case 9 : return readItemStack();
		case 10 : return readItem();
		case 11 : return readBlock();
		case 12 : return readWorld();
		case 13 : return BiomeGenBase.getBiomeGenArray()[readInt()];
		case 14 : return GuiCondition.register.get(readString());
		case 15 : return readIntArray();
		default : return null;
		}
	}

	public TileEntity readTileEntity() throws IOException 
	{
		if(stream.readBoolean())
		{
			World world = readWorld();
			int x = stream.readInt();
			int y = stream.readShort();
			int z = stream.readInt();
			return world.getTileEntity(x, y, z);
		}
		return null;
	}
	
	public ByteArrayDataInput getInput()
	{
		return stream;
	}
}