package fla.core.util;

import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class NBTMaker 
{
	public static NBTTagCompound createCompoundTag(Map<String, Object> map)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		for(String name : map.keySet())
		{
			setObject(nbt, name, map.get(name));
		}
		return nbt;
	}

	private static void setObject(NBTTagCompound nbt, String str, Object obj)
	{
		if(obj == null) return;
		if(obj instanceof Boolean) nbt.setBoolean(str, (Boolean) obj);
		if(obj instanceof Byte) nbt.setByte(str, (Byte) obj);
		if(obj instanceof Short) nbt.setShort(str, (Short) obj);
		if(obj instanceof Integer) nbt.setInteger(str, (Integer) obj);
		if(obj instanceof Long) nbt.setLong(str, (Long) obj);
		if(obj instanceof Character) nbt.setString(str, ((Character) obj).toString());
		if(obj instanceof String) nbt.setString(str, (String) obj);
		if(obj instanceof ItemStack) 
		{
			NBTTagCompound nbt1 = new NBTTagCompound();
			nbt1.setString("Type", "ItemStack");
			((ItemStack) obj).writeToNBT(nbt1);
			nbt.setTag(str, nbt1);
		}
		if(obj instanceof ItemStack[]) 
		{
			NBTTagList list = new NBTTagList();
			ItemStack[] stacks = (ItemStack[]) obj;
			for(int i = 0; i < stacks.length; ++i)
			{
				ItemStack stack = stacks[i];
				if(stack != null)
				{
					NBTTagCompound nbt1 = new NBTTagCompound();
					nbt1.setString("Type", "ItemStack");
					nbt1.setByte("Slot", (byte) i);
					stack.writeToNBT(nbt1);
					list.appendTag(nbt1);
				}
			}
			nbt.setTag(str, list);
		}
	}
}