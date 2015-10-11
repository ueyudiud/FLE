package fle.api.util;

import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagEnd;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;
import cpw.mods.fml.common.registry.GameData;

public class ConfigInfomation
{
	public static ConfigInfomation instance() 
	{
		return new ConfigInfomation();
	}
	
	private boolean flag = false;
	private String[] strs;
	
	private ConfigInfomation()
	{
		flag = true;
		strs = new String[0];
	}
	public ConfigInfomation(String[] arg)
	{
		strs = arg.clone();
	}
	
	private String[] setup(String str)
	{
		str = str.replaceAll(" ", "");
		String[] s1;
		if(str.indexOf("[") != -1) s1 = str.split("[", 2);
		else s1 = new String[]{str};
		String str1;
		if(s1.length != 1)
		{
			String[] s2 = setup(s1[0]);
			s1[1].replaceFirst("]", "");
			s1 = s1[1].split("|");
			String[] ret = new String[s1.length + s2.length];
			System.arraycopy(s2, 0, ret, 0, s2.length);
			System.arraycopy(s1, 0, ret, s2.length, s1.length);
			return ret;
		}
		else
		{
			str1 = s1[0];
			return new String[]{str1};
		}
	}
	
	private void saveObj(int index, Object value)
	{
		if(strs.length <= index)
		{
			String[] s = new String[index + 1];
			System.arraycopy(strs, 0, s, 0, strs.length);
			strs = s;
		}
		strs[index] = value.toString();
	}
	
	public short readShort(int index, short defaultValue)
	{
		if(flag)
		{
			saveObj(index, (Short) defaultValue);
			return defaultValue;
		}
		try
		{
			String[] arg = setup(strs[index]);
			if(arg.length != 1) throw new RuntimeException("Can't read number with more than length 1.", new IOException());
			return new Short(arg[0]);
		}
		catch(Throwable e)
		{
			FleLog.getLogger().catching(e);
			saveObj(index, (Short) defaultValue);
			return defaultValue;
		}
	}
	
	public int readInteger(int index, int defaultValue)
	{
		if(flag)
		{
			saveObj(index, (Integer) defaultValue);
			return defaultValue;
		}
		try
		{
			String[] arg = setup(strs[index]);
			if(arg.length != 1) throw new RuntimeException("Can't read number with more than length 1.", new IOException());
			return new Integer(arg[0]);
		}
		catch(Throwable e)
		{
			FleLog.getLogger().catching(e);
			saveObj(index, (Integer) defaultValue);
			return defaultValue;
		}
	}
	
	public float readFloat(int index, float defaultValue)
	{
		if(flag)
		{
			saveObj(index, (Float) defaultValue);
			return defaultValue;
		}
		try
		{
			String[] arg = setup(strs[index]);
			if(arg.length != 1) throw new RuntimeException("Can't read number with more than length 1.", new IOException());
			return new Float(arg[0]);
		}
		catch(Throwable e)
		{
			FleLog.getLogger().catching(e);
			saveObj(index, (Float) defaultValue);
			return defaultValue;
		}
	}
	
	public double readDouble(int index, double defaultValue)
	{
		if(flag)
		{
			saveObj(index, (Double) defaultValue);
			return defaultValue;
		}
		try
		{
			String[] arg = setup(strs[index]);
			if(arg.length != 1) throw new RuntimeException("Can't read number with more than length 1.", new IOException());
			return new Double(arg[0]);
		}
		catch(Throwable e)
		{
			FleLog.getLogger().catching(e);
			saveObj(index, (Double) defaultValue);
			return defaultValue;
		}
	}
	
	public Item readItem(int index, Item defaultItem)
	{
		if(flag)
		{
			saveObj(index, GameData.getItemRegistry().getNameForObject(defaultItem));
			return defaultItem;
		}
		try
		{
			String[] arg = setup(strs[index]);
			if(arg.length != 1) throw new RuntimeException("Can't read item with more than length 1.", new IOException());
			return GameData.getItemRegistry().getObject(arg[0]);
		}
		catch(Throwable e)
		{
			FleLog.getLogger().catching(e);
			saveObj(index, GameData.getItemRegistry().getNameForObject(defaultItem));
			return defaultItem;
		}
	}
	
	public ItemStack readItemStack(int index, ItemStack defaultStack)
	{
		if(flag)
		{
			String str = "";
			str += GameData.getItemRegistry().getNameForObject(defaultStack.getItem());
			str += "[";
			str += defaultStack.stackSize;
			str += "|";
			str += defaultStack.getItemDamage();
			if(defaultStack.hasTagCompound())
			{
				str += "|";
				for(Object obj : defaultStack.stackTagCompound.func_150296_c())
				{
					String tagName = (String) obj;
					NBTBase tag = defaultStack.stackTagCompound.getTag(tagName);
					String str1 = "";
					if(tag instanceof NBTTagDouble) str += tagName + "|D" + ((NBTTagDouble) tag).func_150286_g();
					else if(tag instanceof NBTTagFloat) str += tagName + "|F" + ((NBTTagFloat) tag).func_150288_h();
					else if(tag instanceof NBTTagLong) str += tagName + "|L" + ((NBTTagLong) tag).func_150291_c();
					else if(tag instanceof NBTTagInt) str += tagName + "|I" + ((NBTTagInt) tag).func_150287_d();
					else if(tag instanceof NBTTagShort) str += tagName + "|S" + ((NBTTagShort) tag).func_150289_e();
					else if(tag instanceof NBTTagString) str += tagName + "|U" + ((NBTTagString) tag).func_150285_a_();
					else if(tag instanceof NBTTagEnd) continue;
					else if(tag instanceof NBTTagList) continue;
					str += str1;
				}
			}
			str += "]";
			return defaultStack == null ? null : defaultStack.copy();
		}
		try
		{
			String[] arg = setup(strs[index]);
			Item item = GameData.getItemRegistry().getObject(arg[0]);
			int size;
			int metadata = 0;
			NBTTagCompound nbt = null;
			if(arg.length > 1)
			{
				size = new Integer(arg[1]);
				if(arg.length > 2)
				{
					metadata = new Integer(arg[2]);
					if(arg.length > 3)
					{
						int length = 3;
						nbt = new NBTTagCompound();
						while(arg.length > length + 1)
						{
							if(arg[length + 1].charAt(0) == 'S')
								nbt.setShort(arg[length], new Short(arg[length + 1].replaceAll("B", "")));
							else if(arg[length + 1].charAt(0) == 'I')
								nbt.setInteger(arg[length], new Integer(arg[length + 1].replaceAll("I", "")));
							else if(arg[length + 1].charAt(0) == 'L')
								nbt.setLong(arg[length], new Long(arg[length + 1].replaceAll("L", "")));
							else if(arg[length + 1].charAt(0) == 'U')
								nbt.setString(arg[length], arg[length + 1].replaceAll("U", ""));
							else if(arg[length + 1].charAt(0) == 'B')
								nbt.setBoolean(arg[length], new Boolean(arg[length + 1].replaceAll("B", "")));
							else if(arg[length + 1].charAt(0) == 'F')
								nbt.setFloat(arg[length], new Float(arg[length + 1].replaceAll("F", "")));
							else if(arg[length + 1].charAt(0) == 'D')
								nbt.setDouble(arg[length], new Double(arg[length + 1].replaceAll("D", "")));
							else throw new RuntimeException("Can't read nbt type with unknown spcies.", new IOException());
							length += 2;
						}
					}
				}
			}
			else
			{
				size = 1;
			}
			ItemStack ret = new ItemStack(item, size, metadata);
			ret.stackTagCompound = nbt;
			item.setDamage(ret, metadata);
			return ret;
		}
		catch(Throwable e)
		{
			FleLog.getLogger().catching(e);
			flag = true;
			ItemStack ret = readItemStack(index, defaultStack);
			flag = false;
			return ret;
		}
	}
	
	public Block readBlock(int index, Block defaultBlock)
	{
		if(flag)
		{
			saveObj(index, GameData.getBlockRegistry().getNameForObject(defaultBlock));
			return defaultBlock;
		}
		try
		{
			String[] arg = setup(strs[index]);
			if(arg.length != 1) throw new RuntimeException("Can't read block with more than length 1.", new IOException());
			return GameData.getBlockRegistry().getObject(arg[0]);
		}
		catch(Throwable e)
		{
			FleLog.getLogger().catching(e);
			saveObj(index, GameData.getBlockRegistry().getNameForObject(defaultBlock));
			return defaultBlock;
		}
	}
	
	public String readString(int index)
	{
		return strs[index];
	}

	@Override
	public String toString()
	{
		String ret = "";
		boolean flag = false;
		for(String str : strs)
		{
			ret += flag ? "," + str : str;
			flag = true;
		}
		return ret;
	}
}