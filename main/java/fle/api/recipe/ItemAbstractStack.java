package fle.api.recipe;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;

public abstract class ItemAbstractStack 
{
	public abstract boolean isStackEqul(ItemStack item);

	public abstract boolean isStackEqul(FluidStack item);
	
	public abstract boolean isStackEqul(ItemAbstractStack stack);
	
	public abstract List<ItemStack> toArray();
	
	@Override
	public String toString()
	{
		ItemStack tStack = toArray().get(0);
		return "stack.abstract." + tStack.getUnlocalizedName() + "x" + tStack.stackSize;
	}

	public static String getStackTipInfo(Object obj)
	{
		if(obj == null) return null;
		EnumChatFormatting ecf = null;
		String str = "";
		if(obj instanceof Item)
		{
			ecf = EnumChatFormatting.WHITE;
			str = "*";
		}
		else if(obj instanceof Block)
		{
			ecf = EnumChatFormatting.WHITE;
			str = "*";
		}
		else if(obj instanceof ItemStack)
		{
			ItemStack stack = (ItemStack) obj;
			if(stack.stackSize > 1)
			{
				ecf = EnumChatFormatting.WHITE;
				str = Integer.toString(stack.stackSize);
			}
		}
		else if(obj instanceof ItemArrayStack)
		{
			List list = ((ItemArrayStack) obj).toArray();
			if(list.size() > 1)
			{
				ecf = EnumChatFormatting.WHITE;
			}
			else if(list.isEmpty())
			{
				ecf = EnumChatFormatting.GRAY;
			}
			str = "F";
		}
		else if(obj instanceof ItemBaseStack)
		{
			ItemStack stack = ((ItemBaseStack) obj).stack;
			if(stack.stackSize > 1)
			{
				ecf = EnumChatFormatting.WHITE;
				str = Integer.toString(stack.stackSize);
			}
		}
		else if(obj instanceof ItemOreStack)
		{
			ItemOreStack stack = (ItemOreStack) obj;
			if(stack.toArray().isEmpty())
			{
				ecf = EnumChatFormatting.DARK_GRAY;
			}
			else
			{
				ecf = EnumChatFormatting.WHITE;
			}
			str = "F";
		}
		else if(obj instanceof ItemFluidContainerStack)
		{
			ItemFluidContainerStack stack = (ItemFluidContainerStack) obj;
			if(stack.toArray().isEmpty())
			{
				ecf = EnumChatFormatting.DARK_GRAY;
			}
			else
			{
				ecf = EnumChatFormatting.WHITE;
			}
			str = "F";
		}
		return ecf == null ? null : ecf.toString() + str;
	}
}