/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.data;

import java.util.function.Function;

import nebula.common.nbt.INBTCompoundReaderAndWritter;
import nebula.common.nbt.INBTReaderAndWritter;
import nebula.common.nbt.NBTTagCompoundEmpty;
import nebula.common.util.NBTs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
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
	
	public static final Function<ItemStack, NBTBase> ITEMSTACK_WRITER = stack -> stack == null ? NBTTagCompoundEmpty.INSTANCE : stack.writeToNBT(new NBTTagCompound());
	public static final Function<NBTBase, ItemStack> ITEMSTACK_READER = nbt -> ItemStack.loadItemStackFromNBT((NBTTagCompound) nbt);
}