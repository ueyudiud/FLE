package flapi.util.io;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import net.minecraft.item.ItemStack;

import com.google.gson.annotations.Expose;

import flapi.collection.CollectionUtil;
import flapi.collection.abs.Stack;
import flapi.recipe.DropInfo;
import flapi.recipe.stack.JsonStack;
import flapi.recipe.stack.JsonStack.StackInfomation;

public class ItemDropData
{
	@Expose
	public int minSize;
	@Expose
	public int maxSize;
	@Expose
	public double chance;
	@Expose
	public Stack<StackInfomation>[] stacks;
	
	public DropInfo toDropInfo()
	{
		Map<ItemStack, Integer> wMap = new HashMap();
		for(Stack<StackInfomation> stack : stacks)
			if(stack != null)
				wMap.put(stack.get().getStack(), stack.size);
		return new DropInfo(maxSize, minSize, (float) chance, wMap);
	}
	
	public static ItemDropData toDropData(DropInfo info)
	{
		ItemDropData data = new ItemDropData();
		data.maxSize = info.maxSize;
		data.minSize = info.minSize;
		data.chance = info.chance;
		Map<StackInfomation, Integer> map = new WeakHashMap();
		for(Stack<ItemStack> stack : info.drops)
		{
			map.put(new JsonStack(stack.get()).getInfomation(), stack.size);
		}
		data.stacks = CollectionUtil.asArray(map);
		return data;
	}
}