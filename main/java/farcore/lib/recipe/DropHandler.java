package farcore.lib.recipe;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import farcore.lib.stack.AbstractStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class DropHandler
{
	private int maxWeight;
	private int minWeight;
	private int allWeight;
	private AbstractStack[] stacksList;
	private int[] weightList;
	
	public DropHandler(int max, int min, Entry<AbstractStack, Integer>...stacks)
	{
		this.maxWeight = max;
		this.minWeight = min;
		int size = 0;
		stacksList = new AbstractStack[stacks.length];
		weightList = new int[stacks.length];
		for(int i = 0; i < stacks.length; ++i)
		{
			Entry<AbstractStack, Integer> entry = stacks[i];
			size += entry.getValue().intValue();
			stacksList[i] = entry.getKey();
			weightList[i] = entry.getValue().intValue();
		}
	}
	
	public List<ItemStack> randomDropsWithCast(Random random)
	{
		int weight;
		if(minWeight == maxWeight)
		{
			weight = minWeight;
		}
		else
		{
			weight = minWeight + random.nextInt(maxWeight - minWeight);
		}
		List<ItemStack> ret = new ArrayList();
		while(weight-- > 0)
		{
			ret.add(randomDrop(random).instance());
		}
		return ret;
	}
	
	public AbstractStack randomDrop(Random random)
	{
		int i = random.nextInt(allWeight);
		int j = 0;
		int k = 0;
		do
		{
			j += weightList[k]; 
			if(j > i)
			{
				return stacksList[k];
			}
		}
		while(++k < stacksList.length);
		return null;
	}
}