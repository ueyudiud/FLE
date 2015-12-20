package fle.core.item.behavior;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import flapi.item.ItemFleMetaBase;
import flapi.item.interfaces.IBagable;

public abstract class BehaviorBag extends BehaviorBase implements IBagable
{
	public abstract int getSize(ItemStack aStack);

	@Override
	public ItemStack getItemContain(ItemStack aStack, int i)
	{
		return ItemStack.loadItemStackFromNBT(setupNBT(aStack).getCompoundTag("BagSlot_" + i));
	}

	@Override
	public void setItemContain(ItemStack aStack, int i, ItemStack aInput)
	{
		setupNBT(aStack).removeTag("BagSlot_" + i);
		setupNBT(aStack).setTag("BagSlot_" + i, aInput == null ? new NBTTagCompound() : aInput.writeToNBT(new NBTTagCompound()));
	}

	public abstract boolean isItemValid(ItemStack aStack, ItemStack aInput);
	
	private NBTTagCompound setupNBT(ItemStack aStack)
	{
		if(!aStack.hasTagCompound()) aStack.stackTagCompound = new NBTTagCompound();
		return aStack.stackTagCompound;
	}
	
	@Override
	public void getAdditionalToolTips(ItemFleMetaBase item, List<String> list,
			ItemStack itemstack)
	{
		super.getAdditionalToolTips(item, list, itemstack);
		list.add(StatCollector.translateToLocal("info.contain"));
		for(int i = 0; i < getSize(itemstack); ++i)
		{
			if(getItemContain(itemstack, i) != null)
			{
				ItemStack tStack = getItemContain(itemstack, i);
				list.add(EnumChatFormatting.WHITE.toString() + tStack.getDisplayName() + "x" + tStack.stackSize);
			}
		}
	}
}