/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.data;

import java.util.function.Function;

import nebula.common.nbt.INBTCompoundReaderAndWritter;
import nebula.common.nbt.INBTReaderAndWritter;
import nebula.common.util.ItemStacks;
import nebula.common.util.NBTs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;

/**
 * @author ueyudiud
 */
public class NBTLSs
{
	public static final INBTCompoundReaderAndWritter<ItemStack> RW_ITEMSTACK = new INBTCompoundReaderAndWritter<ItemStack>()
	{
		@Override
		public ItemStack readFromNBT(NBTTagCompound nbt)
		{
			return ItemStack.loadItemStackFromNBT(nbt);
		}
		
		@Override
		public void writeToNBT(ItemStack target, NBTTagCompound nbt)
		{
			target.writeToNBT(nbt);
		}
		
		public Class<ItemStack> getTargetType()
		{
			return ItemStack.class;
		}
	};
	public static final INBTReaderAndWritter<ItemStack[], NBTTagList> RW_UNORDERED_ITEMSTACKS = NBTs.wrapAsUnorderedArrayWriterAndReader(RW_ITEMSTACK);
	public static final INBTReaderAndWritter<Integer, NBTTagInt> RW_INT = new INBTReaderAndWritter<Integer, NBTTagInt>()
	{
		public NBTTagInt writeToNBT(Integer target) { return new NBTTagInt(target); }
		
		public Integer readFromNBT(NBTTagInt nbt) { return nbt.getInt(); }
		
		public Class<Integer> getTargetType() { return int.class; }
	};
	public static final INBTReaderAndWritter<Float, NBTTagFloat> RW_FLOAT = new INBTReaderAndWritter<Float, NBTTagFloat>()
	{
		public NBTTagFloat writeToNBT(Float target) { return new NBTTagFloat(target); }
		
		public Float readFromNBT(NBTTagFloat nbt) { return nbt.getFloat(); }
		
		public Class<Float> getTargetType() { return float.class; }
	};
	
	public static final Function<ItemStack, NBTTagCompound> ITEMSTACK_WRITER = ItemStacks::writeItemStackToNBT;
	public static final Function<NBTTagCompound, ItemStack> ITEMSTACK_READER = ItemStack::loadItemStackFromNBT;
}