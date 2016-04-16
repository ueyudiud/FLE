package farcore.lib.recipe;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Random;

import farcore.lib.stack.AbstractStack;
import net.minecraft.item.ItemStack;

public class DropHandler
{
	public static final DropHandler EMPTY = new DropHandler(0F, 0, 0);
	
	private float chance;
	private int maxWeight;
	private int minWeight;
	private int allWeight;
	private AbstractStack[] stacksList;
	private int[] weightList;

	public DropHandler(int size, Entry<AbstractStack, Integer>...stacks)
	{
		this(size, size, stacks);
	}
	public DropHandler(int max, int min, Entry<AbstractStack, Integer>...stacks)
	{
		this(1F, max, min, stacks);
	}
	public DropHandler(float chance, int max, int min, Entry<AbstractStack, Integer>...stacks)
	{
		this.chance = chance;
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
		allWeight = size;
	}
	
	public ArrayList<ItemStack> randomDropsWithCast(Random random)
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
		ArrayList<ItemStack> ret = new ArrayList();
		if(random.nextFloat() < chance)
		{
			while(weight-- > 0)
			{
				ret.add(randomDrop(random).instance());
			}
		}
		return ret;
	}
	
	public AbstractStack randomDrop(Random random)
	{
		if(this == EMPTY) return null;
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