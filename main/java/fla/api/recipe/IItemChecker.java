package fla.api.recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public interface IItemChecker 
{
	public boolean match(ItemStack itemstack);
	
	public List<ItemStack> getEqulStacks();
	
	public class OreChecker implements IItemChecker
	{
		String itemstack;
		
		public OreChecker(String i)
		{
			itemstack = i;
		}

		@Override
		public boolean match(ItemStack target) 
		{
			for(ItemStack stack : OreDictionary.getOres(itemstack))
			{
				if(OreDictionary.itemMatches(stack, target, false)) 
				{
					return true;
				}
			}
			return false;
		}

		public String toString()
		{
			return "checker.ore." + itemstack;
		}

		@Override
		public List<ItemStack> getEqulStacks() 
		{
			List<ItemStack> ret = new ArrayList();
			for(ItemStack stack : OreDictionary.getOres(itemstack))
			{
				if(stack.getItemDamage() == OreDictionary.WILDCARD_VALUE)
					stack.setItemDamage(0);
				ret.add(stack);
			}
			return ret;
		}
	}
	public class ItemChecker implements IItemChecker
	{
		ItemStack itemstack;
		
		public ItemChecker(ItemStack i)
		{
			itemstack = i;
		}
		public ItemChecker(Item i, int meta)
		{
			this(new ItemStack(i, 1, meta));
		}
		public ItemChecker(Item i)
		{
			this(i, 0);
		}
		public ItemChecker(Block i, int meta)
		{
			this(new ItemStack(i, 1, meta));
		}
		public ItemChecker(Block i)
		{
			this(i, 0);
		}

		@Override
		public boolean match(ItemStack target) 
		{
			return itemstack == null ? true : OreDictionary.itemMatches(itemstack, target, false);
		}

		public String toString()
		{
			return "checker." + itemstack.getItem().getUnlocalizedName() + ":" + itemstack.getItemDamage();
		}
		
		@Override
		public List<ItemStack> getEqulStacks() 
		{
			ItemStack ret = itemstack.copy();
			if(ret.getItemDamage() == OreDictionary.WILDCARD_VALUE)
				ret.setItemDamage(0);
			return Arrays.asList(ret);
		}
		
	}
}