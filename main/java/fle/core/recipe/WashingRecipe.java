package fle.core.recipe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import flapi.recipe.DropInfo;
import flapi.recipe.stack.BaseStack;
import flapi.recipe.stack.ItemAbstractStack;
import fle.core.item.ItemFleSub;

public class WashingRecipe
{
	private static final Map<ItemAbstractStack, String> inputList = new HashMap();
	private static final Map<String, DropInfo> outputList = new HashMap();

	static
	{
		Map<ItemStack, Integer> map = new HashMap();
		map.put(ItemFleSub.a("flint_a"), 24);
		map.put(ItemFleSub.a("flint_b"), 12);
		map.put(ItemFleSub.a("flint_c"), 5);
		map.put(new ItemStack(Items.flint), 8);
		map.put(ItemFleSub.a("chip_stone"), 2);
		map.put(ItemFleSub.a("crystal_opal"), 3);
		map.put(ItemFleSub.a("chip_quartz"), 1);
		registryDust(new BaseStack(ItemFleSub.a("pile_gravel")), new DropInfo(0.95F, map));
		map.clear();
		map.put(ItemFleSub.a("dust_sand"), 36);
		map.put(ItemFleSub.a("flint_b"), 8);
		map.put(ItemFleSub.a("chip_sandstone"), 12);
		map.put(ItemFleSub.a("chip_stone"), 1);
		map.put(ItemFleSub.a("crushed_bone"), 2);
		map.put(ItemFleSub.a("crystal_quartz"), 3);
		registryDust(new BaseStack(ItemFleSub.a("pile_sand")), new DropInfo(0.95F, map));
		map.clear();
		map.put(ItemFleSub.a("dust_sand"), 11);
		map.put(ItemFleSub.a("pile_sludge"), 15);
		map.put(ItemFleSub.a("flint_b"), 2);
		map.put(ItemFleSub.a("chip_sandstone"), 12);
		registryDust(new BaseStack(ItemFleSub.a("pile_dirt")), new DropInfo(0.95F, map));
		map.put(ItemFleSub.a("chip_stone"), 3);
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
			if(tStack.equal(input))
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

	public static Map<ItemAbstractStack, DropInfo> getRecipes()
	{
		Map<ItemAbstractStack, DropInfo> map = new HashMap();
		for(ItemAbstractStack tStack : inputList.keySet())
		{
			map.put(tStack, outputList.get(inputList.get(tStack)));
		}
		return map;
	}
}