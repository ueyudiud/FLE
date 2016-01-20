package farcore.net;

import farcore.util.FleLog;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.GameData;

/**
 * The minecraft use packer buffer.<br>
 * The class is FleInputStream and FleOutputStream in 1.7.10.<br>
 * Use in packet during encode and decode.
 * @author ueyudiud
 * @see farcore.net.IPacket
 */
public class FlePacketBuffer extends PacketBuffer
{
	public FlePacketBuffer(ByteBuf wrapped)
	{
		super(wrapped);
	}
	
	public IBlockState readBlockStateFromBuffer() throws IOException
	{
		Block block = GameData.getBlockRegistry().getObjectById(readInt());
		int length = readInt();
		IBlockState state = block.getDefaultState();
		label:
		for(int i = 0; i < length; ++i)
		{
			String tag = readStringFromBuffer(2048);
			Object target = read();
			if(tag == null) continue;
			for(Object obj : block.getBlockState().getValidStates())
			{
				if(obj instanceof IProperty && tag.equals(((IProperty) obj).getName()))
				{
					IProperty property = (IProperty) obj;
					for(Object value : property.getAllowedValues())
						if(target.equals(value))
						{
							state = state.withProperty(property, (Comparable) value);
							continue label;
						}
				}
			}
		}
		return state;
	}
	
	public void writeBlockStateToBuffer(IBlockState state) throws IOException
	{
		writeInt(GameData.getBlockRegistry().getId(state.getBlock()));
		writeInt(state.getProperties().size());
		for(Object obj : state.getProperties().entrySet())
		{
			Entry<Object, Object> entry = (Entry<Object, Object>) obj;
			writeString(((IProperty) entry.getKey()).getName());
			write(entry.getValue());
		}
	}
	
	public FluidStack readFluidStackFromBuffer()
	{
		int amount = readInt();
		if(amount > 0)
		{
			Fluid fluid = FluidRegistry.getFluid(readInt());
			
			try
			{
				return new FluidStack(fluid, amount, readNBTTagCompoundFromBuffer());
			}
			catch (IOException e)
			{
				FleLog.error("Catching an exception during reseave message about fluid.", e);
				return new FluidStack(fluid, amount);
			}
		}
		return null;
	}
	
	public void writeFluidStackToBuffer(FluidStack stack)
	{
		if(stack == null || stack.getFluid() == null || stack.amount <= 0)
			writeInt(-1);
		writeInt(FluidRegistry.getFluidID(stack.getFluid()));
		writeNBTTagCompoundToBuffer(stack.tag);
	}
	
	public <T> void writeArray(Class<T> clazz, T...ts) throws IOException
	{
		writeInt(ts.length);
		for(int i = 0; i < ts.length; ++i)
		{
			write(clazz, ts[i]);
		}
	}
	
	public <T> T[] readArray(Class<T> clazz) throws IOException
	{
		int length = readInt();
		Object array = Array.newInstance(clazz, length);
		for(int i = 0; i < length; ++i)
		{
			((Object[]) array)[i] = read(clazz);
		}
		return (T[]) array;
	}
	
	public void write(Object obj) throws IOException
	{
		write(obj, true);
	}
	
	private void write(Object obj, boolean writeType) throws IOException
	{
		if(obj == null)
		{
			writeBoolean(false);
		}
		else
		{
			writeBoolean(true);
			if(obj.getClass().isArray())
			{
				if(writeType)
					writeShort(-1);
				Class clazz = obj.getClass().getComponentType();
				writeClass(clazz);
				writeArray(clazz, (Object[]) obj);
			}
			else if(obj instanceof Byte)
			{
				if(writeType)
					writeShort(1);
				writeByte((Byte) obj);
			}
			else if(obj instanceof Short)
			{
				if(writeType)
					writeShort(2);
				writeShort((Short) obj);
			}
			else if(obj instanceof Integer)
			{
				if(writeType)
					writeShort(3);
				writeInt((Integer) obj);
			}
			else if(obj instanceof Long)
			{
				if(writeType)
					writeShort(4);
				writeLong((Long) obj);
			}
			else if(obj instanceof Float)
			{
				if(writeType)
					writeShort(5);
				writeFloat((Float) obj);
			}
			else if(obj instanceof Double)
			{
				if(writeType)
					writeShort(6);
				writeDouble((Double) obj);
			}
			else if(obj instanceof String)
			{
				if(writeType)
					writeShort(7);
				writeString((String) obj);
			}
			else if(obj instanceof ItemStack)
			{
				if(writeType)
					writeShort(8);
				writeItemStackToBuffer((ItemStack) obj);
			}
			else if(obj instanceof FluidStack)
			{
				if(writeType)
					writeShort(9);
				writeFluidStackToBuffer((FluidStack) obj);
			}
			else if(obj instanceof Enum<?>)
			{
				if(writeType)
					writeShort(10);
				writeString(obj.getClass().getName());
				writeEnumValue((Enum) obj);
			}
			else if(obj instanceof BiomeGenBase)
			{
				if(writeType)
					writeShort(11);
				writeShort(((BiomeGenBase) obj).biomeID);
			}
			else if(obj instanceof Entity)
			{
				if(writeType)
					writeShort(12);
//				writeInt(((Entity) obj).worldObj.provider.getDimensionId());
				writeUuid(((Entity) obj).getPersistentID());
			}
			else if(obj instanceof IBlockState)
			{
				if(writeType)
					writeShort(13);
				writeBlockStateToBuffer((IBlockState) obj);
			}
			else if(obj instanceof NBTTagCompound)
			{
				if(writeType)
					writeShort(14);
				writeNBTTagCompoundToBuffer((NBTTagCompound) obj);
			}
			else throw new RuntimeException("Can not identify object " + obj.toString() + " to write to buffer!");
		}
	}

	public Object read() throws IOException
	{
		if(!readBoolean()) return null;
		switch(readShort())
		{
		case -1 : 
		{
			Class clazz = readClass();
			return readArray(clazz);
		}
		case 0 : throw new RuntimeException();
		case 1 : return readByte();
		case 2 : return readShort();
		case 3 : return readInt();
		case 4 : return readLong();
		case 5 : return readFloat();
		case 6 : return readDouble();
		case 7 : return readStringFromBuffer(2048);
		case 8 : return readItemStackFromBuffer();
		case 9 : return readFluidStackFromBuffer();
		case 10 :
		{
			try
			{
				return readEnumValue(Class.forName(readStringFromBuffer(2048)));
			}
			catch(ClassNotFoundException exception)
			{
				throw new IOException("Can not found class when read buffer, the mod in client side and server side may not have same version. Please check your mod wheather it need update.");
			}
		}
		case 11 : return BiomeGenBase.getBiome(readShort());
		case 12 : return readUuid();
		case 13 : return readBlockStateFromBuffer();
		case 14 : return readNBTTagCompoundFromBuffer();
		default : throw new IOException("Can not solve type id. This mod might need be update.");
		}
	}
	
	private void write(Class clazz, Object obj) throws IOException
	{
		if(obj == null)
		{
			writeBoolean(false);
		}
		else
		{
			writeBoolean(true);
			if(clazz.isArray())
				writeArray(clazz, (Object[]) obj);
			else if(Object.class.equals(clazz))
				write(obj);
			else if(Byte.class.equals(clazz))
				writeByte(((Number) obj).byteValue());
			else if(Short.class.equals(clazz))
				writeShort(((Number) obj).shortValue());
			else if(Integer.class.equals(clazz))
				writeInt(((Number) obj).intValue());
			else if(Long.class.equals(clazz))
				writeLong(((Number) obj).longValue());
			else if(Float.class.equals(clazz))
				writeFloat(((Number) obj).floatValue());
			else if(Double.class.equals(clazz))
				writeDouble(((Number) obj).doubleValue());
			else if(String.class.equals(clazz))
				writeString((String) obj);
			else if(ItemStack.class.equals(clazz))
				writeItemStackToBuffer((ItemStack) obj);
			else if(FluidStack.class.equals(clazz))
				writeFluidStackToBuffer((FluidStack) obj);
			else if(BiomeGenBase.class.equals(clazz))
				writeInt(((BiomeGenBase) obj).biomeID);
			else if(Enum.class.isAnnotationPresent(clazz))
			{
				writeString(clazz.getName());
				writeEnumValue((Enum) obj);
			}
			else if(Entity.class.equals(clazz))
				writeUuid(((Entity) obj).getPersistentID());
			else if(NBTTagCompound.class.equals(clazz))
				writeNBTTagCompoundToBuffer((NBTTagCompound) obj);
			else throw new IOException("Can not identify class type.");
		}
	}
	
	private Object read(Class clazz) throws IOException
	{
		if(!readBoolean()) return null;
		if(clazz.isArray())
			return readArray(clazz.getComponentType());
		else if(Object.class.equals(clazz))
			return read();
		else if(Byte.class.equals(clazz))
			return readByte();
		else if(Short.class.equals(clazz))
			return readShort();
		else if(Integer.class.equals(clazz))
			return readInt();
		else if(Long.class.equals(clazz))
			return readLong();
		else if(Float.class.equals(clazz))
			return readFloat();
		else if(Double.class.equals(clazz))
			return readDouble();
		else if(String.class.equals(clazz))
			return readStringFromBuffer(2048);
		else if(ItemStack.class.equals(clazz))
			return readItemStackFromBuffer();
		else if(FluidStack.class.equals(clazz))
			return readFluidStackFromBuffer();
		else if(BiomeGenBase.class.equals(clazz))
			return BiomeGenBase.getBiome(readShort());
		else if(Enum.class.isAnnotationPresent(clazz))
			try
			{
				Class clazz1 = Class.forName(readStringFromBuffer(2048));
				return readEnumValue(clazz1);
			}
			catch(ClassNotFoundException exception)
			{
				throw new IOException("Can not found class when read buffer, the mod in client side and server side may not have same version. Please check your mod wheather it need update.");
			}
		else if(Entity.class.equals(clazz))
			return readUuid();
		else if(NBTTagCompound.class.equals(clazz))
			return readNBTTagCompoundFromBuffer();
		else throw new IOException("Can not identify class type.");
	}
	
	private void writeClass(Class<?> clazz) throws IOException
	{
		if(Object[].class.equals(clazz))
			writeByte(-1);
		else if(Object.class.equals(clazz))
			writeByte(0);
		else if(Byte.class.equals(clazz))
			writeByte(1);
		else if(Short.class.equals(clazz))
			writeByte(2);
		else if(Integer.class.equals(clazz))
			writeByte(3);
		else if(Long.class.equals(clazz))
			writeByte(4);
		else if(Float.class.equals(clazz))
			writeByte(5);
		else if(Double.class.equals(clazz))
			writeByte(6);
		else if(String.class.equals(clazz))
			writeByte(7);
		else if(ItemStack.class.equals(clazz))
			writeByte(8);
		else if(FluidStack.class.equals(clazz))
			writeByte(9);
		else if(BiomeGenBase.class.equals(clazz))
			writeByte(11);
		else if(IBlockState.class.isAssignableFrom(clazz))
			writeByte(13);
		else if(NBTTagCompound.class.equals(clazz))
			writeByte(14);
		else 
		{
			writeByte(Byte.MAX_VALUE);
			writeString(clazz.getName());
		}		
	}
	
	private Class<?> readClass() throws IOException
	{
		switch(readByte())
		{
		case -1 : return Object[].class;
		case 0 : return Object.class;
		case 1 : return Byte.class;
		case 2 : return Short.class;
		case 3 : return Integer.class;
		case 4 : return Long.class;
		case 5 : return Float.class;
		case 6 : return Double.class;
		case 7 : return String.class;
		case 8 : return ItemStack.class;
		case 9 : return FluidStack.class;
		case 10 : return Enum.class;
		case 11 : return BiomeGenBase.class;
		case 12 : return Entity.class;
		case 13 : return IBlockState.class;
		case 14 : return NBTTagCompound.class;
		default : 
		try
		{
			return Class.forName(readStringFromBuffer(2048));
		}
		catch(ClassNotFoundException exception)
		{
			throw new IOException(exception);
		}
		}
	}
}