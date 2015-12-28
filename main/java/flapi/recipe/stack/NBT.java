package flapi.recipe.stack;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class NBT
{
	@Expose
	NBTCompound[] subs;
	@Expose
	NBTList[] lists;
	@Expose
	NBTArray[] arrays;
	@Expose
	NBTNumber[] numbers;
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		for(NBTCompound sub : subs)
		{
			sub.saveToNBT(nbt);
		}
		for(NBTNumber num : numbers)
		{
			num.saveToNBT(nbt);
		}
		for(NBTArray array : arrays)
		{
			array.saveToNBT(nbt);
		}
		for(NBTList list : lists)
		{
			list.saveToNBT(nbt);
		}
		return nbt;
	}

	public void readFromNBT(NBTTagCompound nbt)
	{
		List<NBTNumber> list1 = new ArrayList();
		List<NBTCompound> list2 = new ArrayList();
		List<NBTArray> list3 = new ArrayList();
		List<NBTList> list4 = new ArrayList();
		for(Object obj : nbt.func_150296_c())
		{
			String name = (String) obj;
			NBTBase nbt1 = nbt.getTag(name);
			if(nbt1 instanceof NBTTagByte)
			{
				list1.add(new NBTNumber(name, ((NBTTagByte) nbt1).func_150290_f()));
			}
			else if(nbt1 instanceof NBTTagShort)
			{
				list1.add(new NBTNumber(name, ((NBTTagShort) nbt1).func_150289_e()));
			}
			else if(nbt1 instanceof NBTTagInt)
			{
				list1.add(new NBTNumber(name, ((NBTTagInt) nbt1).func_150287_d()));
			}
			else if(nbt1 instanceof NBTTagLong)
			{
				list1.add(new NBTNumber(name, ((NBTTagLong) nbt1).func_150291_c()));
			}
			else if(nbt1 instanceof NBTTagFloat)
			{
				list1.add(new NBTNumber(name, ((NBTTagFloat) nbt1).func_150288_h()));
			}
			else if(nbt1 instanceof NBTTagDouble)
			{
				list1.add(new NBTNumber(name, ((NBTTagDouble) nbt1).func_150286_g()));
			}
			else if(nbt1 instanceof NBTTagString)
			{
				list1.add(new NBTNumber(name, ((NBTTagString) nbt1).func_150285_a_()));
			}
			else if(nbt1 instanceof NBTTagCompound)
			{
				list2.add(new NBTCompound(name, (NBTTagCompound) nbt1));
			}
			else if(nbt1 instanceof NBTTagList)
			{
				list4.add(new NBTList(name, (NBTTagList) nbt1));
			}
			else if(nbt1 instanceof NBTTagIntArray)
			{
				list3.add(new NBTArray(name, ((NBTTagIntArray) nbt1).func_150302_c()));
			}
		}
		numbers = list1.toArray(new NBTNumber[list1.size()]);
		subs = list2.toArray(new NBTCompound[list2.size()]);
		arrays = list3.toArray(new NBTArray[list3.size()]);
		lists = list4.toArray(new NBTList[list4.size()]);
	}
	
	static abstract class NBTElement
	{
		@Expose
		public String name;
		
		public NBTElement()
		{
			
		}
		public NBTElement(String tag)
		{
			name = tag;
		}
		final NBTTagCompound saveToNBT(NBTTagCompound nbt)
		{
			writeToNBT(nbt);
			return nbt;
		}
		
		abstract void writeToNBT(NBTTagCompound nbt);

		abstract void readFromNBT(NBTTagCompound nbt);
	}
	
	static class NBTCompound extends NBTElement
	{
		@Expose
		NBTCompound[] subs;
		@Expose
		NBTList[] lists;
		@Expose
		NBTArray[] arrays;
		@Expose
		NBTNumber[] numbers;
		
		public NBTCompound()
		{
			
		}
		public NBTCompound(String tag)
		{
			super(tag);
		}
		
		public NBTCompound(String name, NBTTagCompound nbt)
		{
			super(name);
			writeToNBT(nbt);
		}
		
		void writeToNBT(NBTTagList list)
		{
			NBTTagCompound nbt1 = new NBTTagCompound();
			for(NBTCompound sub : subs)
			{
				sub.saveToNBT(nbt1);
			}
			for(NBTNumber num : numbers)
			{
				num.saveToNBT(nbt1);
			}
			for(NBTArray array : arrays)
			{
				array.saveToNBT(nbt1);
			}
			for(NBTList list1 : lists)
			{
				list1.saveToNBT(nbt1);
			}
			list.appendTag(nbt1);
		}
		
		@Override
		void writeToNBT(NBTTagCompound nbt)
		{
			NBTTagCompound nbt1 = new NBTTagCompound();
			for(NBTCompound sub : subs)
			{
				sub.saveToNBT(nbt1);
			}
			for(NBTNumber num : numbers)
			{
				num.saveToNBT(nbt1);
			}
			for(NBTArray array : arrays)
			{
				array.saveToNBT(nbt1);
			}
			for(NBTList list : lists)
			{
				list.saveToNBT(nbt1);
			}
			nbt.setTag(name, nbt1);
		}
		
		void loadFromNBT(NBTTagCompound compound)
		{
			List<NBTNumber> list1 = new ArrayList();
			List<NBTCompound> list2 = new ArrayList();
			List<NBTArray> list3 = new ArrayList();
			List<NBTList> list4 = new ArrayList();
			for(Object obj : compound.func_150296_c())
			{
				String name = (String) obj;
				NBTBase nbt1 = compound.getTag(name);
				if(nbt1 instanceof NBTTagByte)
				{
					list1.add(new NBTNumber(name, ((NBTTagByte) nbt1).func_150290_f()));
				}
				else if(nbt1 instanceof NBTTagShort)
				{
					list1.add(new NBTNumber(name, ((NBTTagShort) nbt1).func_150289_e()));
				}
				else if(nbt1 instanceof NBTTagInt)
				{
					list1.add(new NBTNumber(name, ((NBTTagInt) nbt1).func_150287_d()));
				}
				else if(nbt1 instanceof NBTTagLong)
				{
					list1.add(new NBTNumber(name, ((NBTTagLong) nbt1).func_150291_c()));
				}
				else if(nbt1 instanceof NBTTagFloat)
				{
					list1.add(new NBTNumber(name, ((NBTTagFloat) nbt1).func_150288_h()));
				}
				else if(nbt1 instanceof NBTTagDouble)
				{
					list1.add(new NBTNumber(name, ((NBTTagDouble) nbt1).func_150286_g()));
				}
				else if(nbt1 instanceof NBTTagString)
				{
					list1.add(new NBTNumber(name, ((NBTTagString) nbt1).func_150285_a_()));
				}
				else if(nbt1 instanceof NBTTagCompound)
				{
					list2.add(new NBTCompound(name, (NBTTagCompound) nbt1));
				}
				else if(nbt1 instanceof NBTTagList)
				{
					list4.add(new NBTList(name, (NBTTagList) nbt1));
				}
				else if(nbt1 instanceof NBTTagIntArray)
				{
					list3.add(new NBTArray(name, ((NBTTagIntArray) nbt1).func_150302_c()));
				}
			}
			numbers = list1.toArray(new NBTNumber[list1.size()]);
			subs = list2.toArray(new NBTCompound[list2.size()]);
			arrays = list3.toArray(new NBTArray[list3.size()]);
			lists = list4.toArray(new NBTList[list4.size()]);
		}
		
		@Override
		void readFromNBT(NBTTagCompound nbt)
		{
			NBTTagCompound compound = nbt.getCompoundTag(name);
			loadFromNBT(compound);
		}	
	}
	static class NBTList extends NBTElement
	{
		@Expose
		NBTCompound[] values;
		public NBTList()
		{
			
		}
		public NBTList(String name, NBTTagList list)
		{
			super(name);
			readFromNBT(list);
		}		
		@Override
		void writeToNBT(NBTTagCompound nbt)
		{
			NBTTagList list = new NBTTagList();
			for(NBTCompound nbt1 : values)
				nbt1.writeToNBT(list);
			nbt.setTag(name, list);
		}
		@Override
		void readFromNBT(NBTTagCompound nbt)
		{
			readFromNBT(nbt.getTagList(name, 10));
		}
		void readFromNBT(NBTTagList nbt)
		{
			List<NBTCompound> list = new ArrayList<NBT.NBTCompound>();
			for(int i = 0; i < nbt.tagCount(); ++i)
			{
				NBTCompound nbt1 = new NBTCompound();
				nbt1.loadFromNBT(nbt.getCompoundTagAt(i));
				list.add(nbt1);
			}
			values = list.toArray(new NBTCompound[list.size()]);
		}
	}
	static class NBTArray extends NBTElement
	{
		@Expose
		int[] values;
		
		public NBTArray()
		{
			
		}
		public NBTArray(String name, int...is)
		{
			super(name);
			values = is;
		}

		@Override
		void writeToNBT(NBTTagCompound nbt)
		{
			nbt.setIntArray(name, values);
		}

		@Override
		void readFromNBT(NBTTagCompound nbt)
		{
			values = nbt.getIntArray(name);
		}
	}
	
	static class NBTNumber extends NBTElement
	{
		@Expose
		@SerializedName("type")
		String nbtType;
		@Expose
		@SerializedName("value")
		String number;
		
		public NBTNumber()
		{
			
		}
		private NBTNumber(String tag, String type, String value)
		{
			super(tag);
			nbtType = type;
			number = value;
		}
		public NBTNumber(String tag, byte value)
		{
			this(tag, "byte", String.valueOf(value));
		}
		public NBTNumber(String tag, short value)
		{
			this(tag, "short", String.valueOf(value));
		}
		public NBTNumber(String tag, int value)
		{
			this(tag, "int", String.valueOf(value));
		}
		public NBTNumber(String tag, long value)
		{
			this(tag, "long", String.valueOf(value));
		}
		public NBTNumber(String tag, float value)
		{
			this(tag, "float", String.valueOf(value));
		}
		public NBTNumber(String tag, double value)
		{
			this(tag, "double", String.valueOf(value));
		}
		public NBTNumber(String tag, String value)
		{
			this(tag, "string", value);
		}
		public NBTNumber(String tag, boolean value)
		{
			this(tag, "boolean", String.valueOf(value));
		}
		
		@Override
		void writeToNBT(NBTTagCompound nbt)
		{
			if("byte".equals(nbtType))
				nbt.setByte(name, Byte.valueOf(number));
			else if("short".equals(nbtType))
				nbt.setShort(name, Short.valueOf(number));
			else if("int".equals(nbtType))
				nbt.setInteger(name, Integer.valueOf(number));
			else if("long".equals(nbtType))
				nbt.setLong(name, Long.valueOf(number));
			else if("float".equals(nbtType))
				nbt.setFloat(name, Float.valueOf(number));
			else if("double".equals(nbtType))
				nbt.setDouble(name, Double.valueOf(number));
			else if("string".equals(nbtType))
				nbt.setString(name, number);
			else if("boolean".equals(nbtType))
				nbt.setBoolean(name, Boolean.valueOf(number));
		}
		@Override
		void readFromNBT(NBTTagCompound nbt)
		{
			;//Read in compound.
		}
	}
}