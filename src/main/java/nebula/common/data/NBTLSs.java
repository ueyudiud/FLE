/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.data;

import java.util.function.Function;

import nebula.common.nbt.INBTReaderAndWritter;
import nebula.common.nbt.NBTTagCompoundEmpty;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author ueyudiud
 */
public class NBTLSs
{
	public static final INBTReaderAndWritter<ItemStack> RW_ITEMSTACK = new INBTReaderAndWritter<ItemStack>()
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
	};
	
	public static final Function<ItemStack, NBTBase> ITEMSTACK_WRITER = stack -> stack == null ? NBTTagCompoundEmpty.INSTANCE : stack.writeToNBT(new NBTTagCompound());
	public static final Function<NBTBase, ItemStack> ITEMSTACK_READER = nbt -> ItemStack.loadItemStackFromNBT((NBTTagCompound) nbt);
}