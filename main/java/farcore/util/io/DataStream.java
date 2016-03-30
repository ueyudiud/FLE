package farcore.util.io;

import java.io.IOException;

import com.google.common.base.Charsets;

import cpw.mods.fml.common.registry.GameData;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class DataStream
{
	private ByteBuf buf;
	
	public DataStream(ByteBuf buf)
	{
		this.buf = buf;
	}
	
	public byte readByte()
	{
		return buf.readByte();
	}
	
	public short readShort()
	{
		return buf.readShort();
	}
	
	public int readInt()
	{
		return buf.readInt();
	}
	
	public long readLong()
	{
		return buf.readLong();
	}
	
	public float readFloat()
	{
		return buf.readFloat();
	}
	
	public double readDouble()
	{
		return buf.readDouble();
	}
	
	public String readString()
	{
		return new String(readBytes(), Charsets.UTF_8);
	}
	
	public byte[] readBytes()
	{
		int length = readInt();
		return buf.readBytes(length).array();
	}

	public int[] readIntArray()
	{
		int length = readInt();
		int[] array = new int[length];
		for(int i = 0; i < length; array[i++] = buf.readInt());
		return array;
	}
		
	public NBTTagCompound readNBT() throws IOException
	{
		return buf.readBoolean() ?
				CompressedStreamTools.func_152457_a(readBytes(), new NBTSizeTracker(2097152L)) :
					null;
	}
	
	public ItemStack readItemStack() throws IOException
	{
		if(!buf.readBoolean())
			return null;
		Item item = GameData.getItemRegistry().getObjectById(readShort());
		int size = readByte();
		int meta = readShort();
		NBTTagCompound nbt = null;
		if(item.getShareTag())
		{
			nbt = readNBT();
		}
		ItemStack ret = new ItemStack(item, size, meta);
		ret.stackTagCompound = nbt;
		return ret;
	}
	
	public FluidStack readFluidStack() throws IOException
	{
		if(!buf.readBoolean())
			return null;
		Fluid fluid = FluidRegistry.getFluid(readShort());
		int amount = readInt();
		NBTTagCompound nbt = readNBT();
		return new FluidStack(fluid, amount, nbt);
	}
	
	public void writeByte(byte value)
	{
		buf.writeByte(value);
	}
	
	public void writeShort(int value)
	{
		buf.writeShort(value);
	}
	
	public void writeInt(int value)
	{
		buf.writeInt(value);
	}
	
	public void writeLong(long value)
	{
		buf.writeLong(value);
	}
	
	public void writeFloat(float value)
	{
		buf.writeFloat(value);
	}
	
	public void writeDouble(double value)
	{
		buf.writeDouble(value);
	}
	
	public void writeString(String value)
	{
		writeBytes(value.getBytes(Charsets.UTF_8));
	}
	
	public void writeBytes(byte...value)
	{
		writeInt(value.length);
		buf.writeBytes(value);
	}

	public void writeIntArray(int...value)
	{
		writeInt(value.length);
		for(int i = 0; i < value.length; ++i)
			writeInt(i);
	}
	
	public void writeNBT(NBTTagCompound value) throws IOException
	{
		if(value != null)
		{
			buf.writeBoolean(true);
			writeBytes(CompressedStreamTools.compress(value));
		}
		else
		{
			buf.writeBoolean(false);
		}
	}
	
	public void writeItemStack(ItemStack stack) throws IOException
	{
		if(stack == null)
		{
			buf.writeBoolean(false);
			return;
		}
		buf.writeBoolean(true);
		writeShort(GameData.getItemRegistry().getId(stack.getItem()));
		writeByte((byte) stack.stackSize);
		writeShort(stack.getItemDamage());
		if(stack.getItem().getShareTag())
		{
			writeNBT(stack.getTagCompound());
		}
	}
	
	public void writeFluidStack(FluidStack stack)throws IOException
	{
		if(stack == null)
		{
			buf.writeBoolean(false);
			return;
		}
		buf.writeBoolean(true);
		writeShort(stack.getFluidID());
		writeInt(stack.amount);
		writeNBT(stack.tag);
	}
}