package fle.api.util;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import cpw.mods.fml.common.registry.GameData;
import fle.FLE;

public class FleDataInputStream 
{
	private DataInputStream stream;
	
	public FleDataInputStream(DataInputStream aStream)
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

	public NBTTagCompound readNBT() throws IOException
	{
		if(stream.readBoolean())
		{
			return CompressedStreamTools.read(stream);
		}
		else
		{
			return new NBTTagCompound();
		}
	}
	
	public Item readItem() throws IOException
	{
		return GameData.getItemRegistry().getObject(stream.readUTF());
	}
	
	public Block readBlock(Block block) throws IOException
	{
		return GameData.getBlockRegistry().getObject(stream.readUTF());
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
}