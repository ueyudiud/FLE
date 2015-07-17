package fla.api.recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public interface RecipeInput 
{
	public String getInputName();
	
	/**
	 * Using it when recipe
	 * 
	 * @param target
	 * @param sizeCheck
	 * @return
	 */
	public boolean match(ItemStack target, boolean sizeCheck);
	
	/**
	 * Get list of this stack to show in NEI.
	 * @return
	 */
	public List<ItemStack> getInputs();
	
	public static class RecipeDefaultInput implements RecipeInput
	{
		ItemStack itemstack;
		
		public RecipeDefaultInput(ItemStack i)
		{
			itemstack = i;
		}

		@Override
		public String getInputName() 
		{
			return "stack.item." + itemstack.getUnlocalizedName() + "@" + itemstack.stackSize;
		}

		@Override
		public boolean match(ItemStack target, boolean sizeCheck) 
		{
			return OreDictionary.itemMatches(itemstack, target, false);
		}

		@Override
		public List<ItemStack> getInputs() 
		{
			ItemStack ret = itemstack.copy();
			if(ret.getItemDamage() == OreDictionary.WILDCARD_VALUE) ret.setItemDamage(0);
			return Arrays.asList(ret);
		}
		
	}
	
	public static class RecipeOreInput implements RecipeInput
	{
		String inputName;
		int size;
		
		public RecipeOreInput(String name, int size)
		{
			inputName = name;
			this.size = size;
		}

		@Override
		public String getInputName() 
		{
			return "stack.ore." + inputName + "@" + size;
		}

		@Override
		public boolean match(ItemStack target, boolean sizeCheck) 
		{
			Iterator<ItemStack> itr = OreDictionary.getOres(inputName).iterator();
			while(itr.hasNext())
			{
				if(OreDictionary.itemMatches(itr.next(), target, false))
				{
					if(sizeCheck)
					{
						if(target.stackSize >= size) return true;
					}
					else return true;
				}
			}
			return false;
		}

		@Override
		public List<ItemStack> getInputs() 
		{
			List<ItemStack> ret = new ArrayList();
			List<ItemStack> list= OreDictionary.getOres(inputName);
			for(ItemStack i : list)
			{
				ItemStack i0 = i.copy();
				if(i0.getItemDamage() == OreDictionary.WILDCARD_VALUE)
				{
					i0.setItemDamage(0);
				}
				ret.add(i0);
			}
			return ret;
		}
	}
}
