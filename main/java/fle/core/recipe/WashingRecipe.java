package fle.core.recipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import flapi.recipe.DropInfo;
import flapi.recipe.stack.BaseStack;
import flapi.recipe.stack.ItemAbstractStack;
import flapi.recipe.stack.JsonStack;
import flapi.recipe.stack.JsonStack.StackInfomation;
import flapi.util.FleLog;
import flapi.util.io.IJsonLoader;
import flapi.util.io.ItemDropData;
import flapi.util.io.JsonHandler;
import fle.core.item.ItemFleSub;

public class WashingRecipe implements IJsonLoader
{
	private static boolean postload = false;
	private static Map<ItemAbstractStack, DropInfo> recipeCache = new HashMap();
	private static final Map<ItemAbstractStack, String> inputList = new HashMap();
	private static final Map<String, DropInfo> outputList = new HashMap();

	private static class WashingInfo
	{
		@Expose
		@SerializedName("name")
		public String name;
		@Expose
		@SerializedName("input")
		public StackInfomation input;
		@Expose
		@SerializedName("outputs")
		public ItemDropData info;
	}
	
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
		if(postload)
		{
			inputList.put(aStack, aInfo.toString());
			outputList.put(aInfo.toString(), aInfo);
		}
		else
		{
			recipeCache.put(aStack, aInfo);
		}
	}

	public static void reloadDust(ItemAbstractStack aStack, String name, DropInfo aInfo)
	{
		inputList.put(aStack, name);
		outputList.put(name, aInfo);
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
	
	public static void postInit(JsonHandler handler)
	{
		WashingRecipe recipe = new WashingRecipe();
		if(handler.read(recipe))
		{
			FleLog.getLogger().info("Detected the recipes from json.");
			postload = true;
		}
		else
		{
			List<WashingInfo> ret = new ArrayList();
			for(Entry<ItemAbstractStack, DropInfo> recipe1 : recipeCache.entrySet())
			{
				WashingInfo info = new WashingInfo();
				info.input = new JsonStack(recipe1.getKey()).getInfomation();
				info.name = recipe1.getValue().toString();
				info.info = ItemDropData.toDropData(recipe1.getValue());
				ret.add(info);
			}
			handler.register(ret);
			postload = true;
			for(Entry<ItemAbstractStack, DropInfo> e : recipeCache.entrySet())
				registryDust(e.getKey(), e.getValue());
			handler.write(recipe);
		}
	}

	@Override
	public void readJson(Gson gson, List<String> list) throws IOException
	{
		recipeCache.clear();
		for(String json : list)
		{
			WashingInfo info = gson.fromJson(json, WashingInfo.class);
			reloadDust(info.input.toStack(), info.name, info.info.toDropInfo());
		}
	}

	@Override
	public void writeJson(Gson gson, List<String> list) throws IOException
	{
		;
	}

	@Override
	public Class<?> getSaveClass() 
	{
		return WashingInfo.class;
	}
}