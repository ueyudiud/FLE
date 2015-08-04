package fle.core.recipe;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import fle.api.recipe.ItemAbstractStack;
import fle.api.recipe.ItemBaseStack;
import fle.api.util.DropInfo;
import fle.core.item.ItemFleSub;

public class WashingRecipe
{
	private static final Map<ItemAbstractStack, String> inputList = new HashMap();
	private static final Map<String, DropInfo> outputList = new HashMap();

	static
	{
		Map<ItemStack, Integer> map = new HashMap();
		map.put(ItemFleSub.a("flint_a"), 8);
		map.put(ItemFleSub.a("flint_b"), 4);
		map.put(ItemFleSub.a("flint_c"), 1);
		map.put(new ItemStack(Items.flint), 3);
		registryDust(new ItemBaseStack(Blocks.gravel), new DropInfo(map));
	}
	
	public static void registryDust(ItemAbstractStack aStack, DropInfo aInfo)
	{
		inputList.put(aStack, aInfo.toString());
		outputList.put(aInfo.toString(), aInfo);
	}

	public static String getRecipeName(ItemStack input)
	{
		for (ItemAbstractStack tStack : inputList.keySet())
		{
			if(tStack.isStackEqul(input))
			{
				return inputList.get(tStack);
			}			
		}
		return null;
	}
	
	public static ItemStack[] outputRecipe(String recipe)
	{
		DropInfo info = outputList.get(recipe);
		List<ItemStack> list = info.getDrops();
		return list.toArray(new ItemStack[list.size()]);
	}
}
